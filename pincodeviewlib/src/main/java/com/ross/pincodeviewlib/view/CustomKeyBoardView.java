package com.ross.pincodeviewlib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.ross.pincodeviewlib.R;
import com.ross.pincodeviewlib.constants.Constants;
import com.ross.pincodeviewlib.utils.DimensionUtils;
import com.ross.pincodeviewlib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.ross.pincodeviewlib.constants.Constants.KEY_PAD_ROWS;

/**
 * @author Sergey Rodionov
 */
public class CustomKeyBoardView extends View {

    private double eraseIconWidth;
    private double eraseIconHeight;
    private double fingerIconWidth;
    private double fingerIconHeight;

    private float keyTextSize;

    private int keyViewStartX;
    private int keyViewStartY;
    private int keyViewWidth;
    private int keyViewHeight;
    private int digits;
    private int errorChar = -1;

    private SparseIntArray mTouchXMap;
    private SparseIntArray mTouchYMap;

    private Typeface mTypeface;
    private TextPaint mTextPaint;
    private Paint mCirclePaint;
    private Paint mPaint;

    private boolean dividerVisible;
    private boolean fingerIconVisible;
    private boolean blockInputChar;
    private boolean clearText;
    private boolean longPressed;
    private boolean fingerAuth;

    private String passCodeText = "";

    private Handler mHandler;
    private Runnable mLongPress;
    private List<KeyRect> mListKeyRectView;
    private TextChangeListener mTextChangeListener;

    public CustomKeyBoardView(Context context) {
        this(context, null);
    }

    public CustomKeyBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.KeyBoardView, defStyleAttr, 0);
        try {

            digits = values.getInteger(R.styleable.KeyBoardView_digits, 0);

            passCodeText = values.getString(R.styleable.KeyBoardView_input_password);
            passCodeText = "";

            keyTextSize = values.getDimension(R.styleable.KeyBoardView_key_text_size,
                    getResources().getDimension(R.dimen.key_text_size));

            dividerVisible = values.getBoolean(R.styleable.KeyBoardView_divider_visible, false);

            fingerIconVisible = values.getBoolean(R.styleable.KeyBoardView_finger_visible, false);

            int digitHorizontalPadding = (int) values.getDimension(R.styleable.KeyBoardView_digit_spacing,
                    getResources().getDimension(R.dimen.digit_horizontal_padding));

            int digitVerticalPadding = (int) values.getDimension(R.styleable.KeyBoardView_digit_vertical_padding,
                    getResources().getDimension(R.dimen.digit_vertical_padding));
            eraseIconWidth = (int) values.getDimension(R.styleable.KeyBoardView_erase_icon_width,
                    getResources().getDimension(R.dimen.erase_icon_width));
            eraseIconHeight = (int) values.getDimension(R.styleable.KeyBoardView_erase_icon_height,
                    getResources().getDimension(R.dimen.erase_icon_height));

            fingerIconWidth = (int) values.getDimension(R.styleable.KeyBoardView_finger_icon_width,
                    getResources().getDimension(R.dimen.finger_icon_width));
            fingerIconHeight = (int) values.getDimension(R.styleable.KeyBoardView_finger_icon_height,
                    getResources().getDimension(R.dimen.finger_icon_height));

            mListKeyRectView = new ArrayList<>();
            mTouchXMap = new SparseIntArray();
            mTouchYMap = new SparseIntArray();
            mHandler = new Handler();

        } catch (Exception e) {
            e.printStackTrace();
        }
        values.recycle();
        preparePaint();
    }

    private void preparePaint() {
        mPaint = new Paint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.argb(255, 0, 0, 0));
        mTextPaint.density = getResources().getDisplayMetrics().density;
        mTextPaint.setTextSize(keyTextSize);
    }

    public void setTypeface(Typeface typeface) {
        if (mTypeface != typeface) {
            mTypeface = typeface;
            mTextPaint.setTypeface(typeface);
            requestLayout();
            postInvalidate();
        }
    }

    public void setKeyTextColor(int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        mTextPaint.setColor(colorStateList.getColorForState(getDrawableState(), 0));
        postInvalidate();
    }

    public void setKeyTextSize(float size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        postInvalidate();
    }

    private void initialiseKeyRects() {
        mListKeyRectView.clear();
        int x = keyViewStartX, y = keyViewStartY;
        int keysCount = getKeysCount();
        for (int i = 1; i <= keysCount; i++) {
            mListKeyRectView.add(
                    new KeyRect(this,
                            new Rect(x, y, x + keyViewWidth, y + keyViewHeight),
                            String.valueOf(i)));
            x += keyViewWidth;
            if (i % 3 == 0) {
                y += keyViewHeight;
                x = keyViewStartX;
            }
        }

        if (fingerIconVisible) {
            mListKeyRectView.get(9).setBitmapValue(
                    returnScaledBitmapFromResource(R.drawable.finger, fingerIconWidth, fingerIconHeight));
        }

        mListKeyRectView.get(9).setValue("");
        mListKeyRectView.get(10).setValue("0");
        mListKeyRectView.get(11).setValue(Constants.ERASE_CHAR);
        mListKeyRectView.get(11).setBitmapValue(
                returnScaledBitmapFromResource(R.drawable.erase, eraseIconWidth, eraseIconHeight));
    }

    private Bitmap returnScaledBitmapFromResource(int id, double width, double height) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), id);
        return Bitmap.createScaledBitmap(icon, DimensionUtils.dpToPx(getContext(), width),
                DimensionUtils.dpToPx(getContext(), height), true);
    }

    private void computeKeyboardStartXY() {
        keyViewStartX = 0;
        keyViewStartY = 0;
        keyViewWidth = getMeasuredWidth() / Constants.KEY_PAD_COLS;
        keyViewHeight = getMeasuredHeight() / Constants.KEY_PAD_ROWS;
        initialiseKeyRects();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawKeyPad(canvas);
    }

    private void drawDividerHorizontal(Canvas canvas, KeyRect rect) {
        mPaint.setColor(Color.argb(255, 0, 0, 0));
        mPaint.setAlpha(40);
        canvas.drawLine(0, rect.getRect().bottom, getMeasuredWidth(), rect.getRect().bottom, mPaint);
    }

    private void drawDividerVertical(Canvas canvas, KeyRect rect) {
        mPaint.setColor(Color.argb(255, 0, 0, 0));
        mPaint.setAlpha(40);
        canvas.drawLine(rect.getRect().right, 0, rect.getRect().right, getMeasuredHeight(), mPaint);
    }

    private void drawKeyPad(Canvas canvas) {
        for (KeyRect rect : mListKeyRectView) {

          /*  if (mRect == keyRects.get(9) || mRect == keyRects.get(11)) {
                Paint tempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                tempPaint.setColor(Color.GRAY);
                canvas.drawRect(mRect.mRect, tempPaint);
            }*/

            //mTextPaint.setColor(Color.argb(255, 0, 0, 0));
            if (rect == mListKeyRectView.get(11) || (rect == mListKeyRectView.get(9) &&
                    fingerIconVisible && rect.getBitmapValue() != null)) {

                float iconWidth = rect.getBitmapValue().getScaledWidth(canvas);
                float iconHeight = rect.getBitmapValue().getHeight();
                canvas.drawBitmap(rect.getBitmapValue(), rect.getRect().exactCenterX() - iconWidth / 2,
                        (rect.getRect().exactCenterY() - iconHeight / 5), mPaint);
            } else {
                mTextPaint.setColor(Color.WHITE);
                float textWidth = mTextPaint.measureText(rect.getValue());
                canvas.drawText(rect.getValue(), rect.getRect().exactCenterX() - textWidth / 2,
                        rect.getRect().exactCenterY() + mTextPaint.getTextSize() / 2, mTextPaint);
                if (rect.isHasRippleEffect()) {
                    mCirclePaint.setAlpha(rect.getCircleAlpha());
                    canvas.drawCircle(rect.getRect().exactCenterX(), rect.getRect().exactCenterY(),
                            rect.getRippleRadius(), mCirclePaint);
                }
            }

            if (dividerVisible) {
                if (rect == mListKeyRectView.get(2) || rect == mListKeyRectView.get(5) ||
                        rect == mListKeyRectView.get(8)) {
                    drawDividerHorizontal(canvas, rect);
                }

                if (rect == mListKeyRectView.get(0) || rect == mListKeyRectView.get(1)) {
                    drawDividerVertical(canvas, rect);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredWidth = 0, measuredHeight = 0;
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            double height = MeasureSpec.getSize(heightMeasureSpec) - (keyViewHeight * KEY_PAD_ROWS);
            measuredHeight = (int) height;
        } else {
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        measuredHeight = (int) Math.max(measuredHeight, getResources().getDimension(R.dimen.key_pad_min_height));
        setMeasuredDimension(measuredWidth, measuredHeight);
        computeKeyboardStartXY();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return processTouch(event);
    }

    private boolean processTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int pointerDownId = event.getPointerId(event.getActionIndex());
                mTouchXMap.put(pointerDownId, (int) event.getX());
                mTouchYMap.put(pointerDownId, (int) event.getY());
                longPressed(pointerDownId);
                break;

            case MotionEvent.ACTION_UP:
                int pointerUpId = event.getPointerId(event.getActionIndex());
                int pointerUpIndex = event.findPointerIndex(pointerUpId);
                int eventX = (int) event.getX(pointerUpIndex);
                int eventY = (int) event.getY(pointerUpIndex);
                mHandler.removeCallbacks(mLongPress);
                longPressed = false;
                findKeyPressed(mTouchXMap.get(pointerUpId), mTouchYMap.get(pointerUpId), eventX, eventY);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("Pointer", "down");
                int pointerActionDownId = event.getPointerId(event.getActionIndex());
                mTouchXMap.put(pointerActionDownId, (int) event.getX(event.getActionIndex()));
                mTouchYMap.put(pointerActionDownId, (int) event.getY(event.getActionIndex()));
                longPressed(pointerActionDownId);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerActionUpIndex = event.getActionIndex();
                int pointerActionUpId = event.getPointerId(pointerActionUpIndex);
                int eventPointerX = (int) event.getX(pointerActionUpIndex);
                int eventPointerY = (int) event.getY(pointerActionUpIndex);
                mHandler.removeCallbacks(mLongPress);
                longPressed = false;
                findKeyPressed(mTouchXMap.get(pointerActionUpId),
                        mTouchYMap.get(pointerActionUpId), eventPointerX, eventPointerY);
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    private void findKeyPressed(int downEventX, int downEventY, int upEventX, int upEventY) {
        for (final KeyRect keyRect : mListKeyRectView) {
            if (keyRect.getRect().contains(downEventX, downEventY)
                    && keyRect.getRect().contains(upEventX, upEventY)) {
                keyRect.playRippleAnim(new KeyRect.RippleAnimListener() {
                    @Override
                    public void onStart() {
                        if (keyRect.getValue().equals("") && fingerIconVisible) {
                            ToastUtil.display(getContext(), R.string.finger_auth_toast);
                        }
                        if (passCodeText.length() == digits && isClearText() &&
                                !keyRect.getValue().equals(Constants.ERASE_CHAR) && getErrorChar() == -1) {
                            passCodeText = "";
                            setBlockInputChar(false);
                        }
                        int length = passCodeText.length();
                        if (keyRect.getValue().equals(Constants.ERASE_CHAR)) {
                            if (isClearText()) {
                                passCodeText = "";
                                setBlockInputChar(false);
                                setClearText(false);
                            } else if (length > 0) {
                                setBlockInputChar(false);
                                passCodeText = passCodeText.substring(0, passCodeText.length() - 1);
                            }
                        } else if (!keyRect.getValue().isEmpty() && length <= digits && getErrorChar() == -1) {
                            if (!isBlockInputChar()) {
                                setClearText(false);
                                passCodeText += keyRect.getValue();
                            }
                            if (passCodeText.length() == digits) setBlockInputChar(true);
                        }
                        if (!keyRect.getValue().isEmpty()) {
                            notifyListener();
                        }
                    }

                    @Override
                    public void onEnd() {

                    }
                });
            } else if (keyRect.getRect().contains(downEventX, downEventY) && upEventX == 0 &&
                    upEventY == 0 && keyRect.getRect().equals(Constants.ERASE_CHAR)) {
                int length = passCodeText.length();
                if (length > 0) {
                    while (longPressed) {
                        if (passCodeText.length() > 0) {
                            setBlockInputChar(false);
                            passCodeText = passCodeText.substring(0, passCodeText.length() - 1);
                            notifyListener();
                        } else {
                            longPressed = false;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void longPressed(final int downEventId) {
        if (mLongPress == null) {
            mLongPress = new Runnable() {
                public void run() {
                    longPressed = true;
                    findKeyPressed(mTouchXMap.get(downEventId), mTouchYMap.get(downEventId), 0, 0);
                }
            };
        }

        mHandler.postDelayed(mLongPress, 1000);
    }

    public void reset() {
        this.passCodeText = "";
        invalidateAndNotifyListener();
    }

    interface TextChangeListener {
        void onTextChanged(String text);
    }

    private void invalidateAndNotifyListener() {
        Log.i("New text", passCodeText);
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChanged(passCodeText);
        }
    }

    private void notifyListener() {
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChanged(passCodeText);
        }
    }

    public void setOnTextChangeListener(TextChangeListener listener) {
        mTextChangeListener = listener;
    }

    public void removeOnTextChangeListener() {
        mTextChangeListener = null;
    }

    public void setError(boolean reset) {
        if (reset) {
            reset();
            setBlockInputChar(false);
        }
        for (KeyRect keyRect : mListKeyRectView) {
            keyRect.setError();
        }
    }

    public void setPassCode(String code) {
        this.passCodeText = code;
        invalidate();
    }

    public String getPassCodeText() {
        return passCodeText;
    }

    public void setDigitLength(int length) {
        this.digits = length;
        invalidate();
    }

    public void setFingerAuth(boolean fingerAuth) {
        this.fingerAuth = fingerAuth;
        for (KeyRect rect : mListKeyRectView) {
            if (rect == mListKeyRectView.get(9) && fingerIconVisible) {
                rect.setBitmapValue(returnScaledBitmapFromResource(R.drawable.form_filled,
                        fingerIconWidth, fingerIconHeight));
            }
        }
        invalidate();
    }

    public int getKeysCount() {
        return Constants.KEY_PAD_ROWS * Constants.KEY_PAD_COLS;
    }

    public int getDigitLength() {
        return digits;
    }

    public boolean isClearText() {
        return clearText;
    }

    public void setClearText(boolean clearText) {
        this.clearText = clearText;
    }

    public boolean isBlockInputChar() {
        return blockInputChar;
    }

    public void setBlockInputChar(boolean blockInputChar) {
        this.blockInputChar = blockInputChar;
    }

    @BindingAdapter("digits")
    public static void setDigits(CustomKeyBoardView view, int number_all) {
        view.setDigitLength(number_all);
    }

    @BindingAdapter("fingerAuth")
    public static void setFingerAuth(CustomKeyBoardView view, boolean fingerAuth) {
        view.setFingerAuth(fingerAuth);
    }

    @BindingAdapter("error")
    public static void setError(CustomKeyBoardView view, boolean error) {
        view.setError(error);
    }

    @BindingAdapter("clearText")
    public static void setClearText(CustomKeyBoardView view, boolean clearText) {
        view.setClearText(clearText);
    }

    @BindingAdapter("errorChar")
    public static void setErrorChar(CustomKeyBoardView view, int errorChar) {
        view.setErrorChar(errorChar);
    }

    public int getErrorChar() {
        return errorChar;
    }

    public void setErrorChar(int errorChar) {
        this.errorChar = errorChar;
    }

    @BindingAdapter(value = {"input_password", "bind:input_passwordAttrChanged"},
            requireAll = false)
    public static void setInputPasspord(final CustomKeyBoardView view, String password,
                                        final InverseBindingListener passwordTextAttrChanged) {
        view.setOnTextChangeListener(new TextChangeListener() {
            @Override
            public void onTextChanged(final String text) {
                passwordTextAttrChanged.onChange();
            }
        });
    }

    @InverseBindingAdapter(attribute = "bind:input_password", event = "bind:input_passwordAttrChanged")
    public static String getInputPassword(CustomKeyBoardView view) {
        return view.getPassCodeText();
    }
}
