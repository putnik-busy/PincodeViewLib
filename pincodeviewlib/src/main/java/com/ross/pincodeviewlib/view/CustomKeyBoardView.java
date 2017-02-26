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
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ross.pincodeviewlib.R;
import com.ross.pincodeviewlib.utils.DimensionUtils;
import com.ross.pincodeviewlib.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Rodionov
 */
public class CustomKeyBoardView extends View {
    private final int KEYS_COUNT = 12;
    private final String eraseChar = "\u232B";
    private final int KEY_PAD_COLS = 3;
    private final int KEY_PAD_ROWS = 4;
    private final double eraseIconWidth = 19.36;
    private final double eraseIconHeight = 15.36;
    private final double fingerIconWidth = 15.36;
    private final double fingerIconHeight = 15.36;
    private Paint paint;
    private int kpStartX;
    private int kpStartY;
    private ArrayList<KeyRect> keyRects = new ArrayList<>();
    private int keyWidth;
    private int keyHeight;
    private TextChangeListener textChangeListener;
    private int digits;
    private boolean fingerAuth;
    private int errorChar = -1;
    private boolean clearText;

    private Map<Integer, Integer> touchXMap = new HashMap<>();
    private Map<Integer, Integer> touchYMap = new HashMap<>();

    private Typeface typeFace;
    private TextPaint textPaint;
    private float keyTextSize;
    private Paint circlePaint;


    private boolean dividerVisible;
    private boolean fingerIconVisible;
    //private Context context;
    private String passCodeText = "";
    private boolean blockInputChar;

    private Handler handler = new Handler();
    private boolean mLongPressed;
    private Runnable mLongPress;

    public CustomKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        //this.context = context;
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.KeyBoardView, 0, 0);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        values.recycle();
        preparePaint();
    }

    private void preparePaint() {
        paint = new Paint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.argb(255, 0, 0, 0));
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setTextSize(keyTextSize);
    }

    public void setTypeFace(Typeface typeFace) {
        if (this.typeFace != typeFace) {
            this.typeFace = typeFace;
            textPaint.setTypeface(typeFace);
            requestLayout();
            postInvalidate();
        }
    }

    public void setKeyTextColor(int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        textPaint.setColor(colorStateList.getColorForState(getDrawableState(), 0));
        postInvalidate();
    }

    public void setKeyTextSize(float size) {
        textPaint.setTextSize(size);
        requestLayout();
        postInvalidate();
    }

    private void initialiseKeyRects() {
        keyRects.clear();
        int x = kpStartX, y = kpStartY;
        for (int i = 1; i <= KEYS_COUNT; i++) {
            keyRects.add(
                    new KeyRect(this,
                            new Rect(x, y, x + keyWidth, y + keyHeight),
                            String.valueOf(i)));
            x += keyWidth;
            if (i % 3 == 0) {
                y += keyHeight;
                x = kpStartX;
            }
        }

        if (fingerIconVisible) {
            keyRects.get(9).setBitmapValue(
                    returnScaledBitmapFromResource(R.drawable.finger, fingerIconWidth, fingerIconHeight));
        }

        keyRects.get(9).setValue("");
        keyRects.get(10).setValue("0");
        keyRects.get(11).setValue(eraseChar);
        keyRects.get(11).setBitmapValue(
                returnScaledBitmapFromResource(R.drawable.erase, eraseIconWidth, eraseIconHeight));
    }

    private Bitmap returnScaledBitmapFromResource(int id, double width, double height) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), id);
        return Bitmap.createScaledBitmap(icon, DimensionUtils.dpToPx(getContext(), width),
                DimensionUtils.dpToPx(getContext(), height), true);
    }

    private void computeKeyboardStartXY() {
        kpStartX = 0;
        kpStartY = 0;
        keyWidth = getMeasuredWidth() / KEY_PAD_COLS;
        keyHeight = getMeasuredHeight() / KEY_PAD_ROWS;
        initialiseKeyRects();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawKeyPad(canvas);
    }

    private void drawDividerHorizontal(Canvas canvas, KeyRect rect) {
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setAlpha(40);
        canvas.drawLine(0, rect.rect.bottom, getMeasuredWidth(), rect.rect.bottom, paint);
    }

    private void drawDividerVertical(Canvas canvas, KeyRect rect) {
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setAlpha(40);
        canvas.drawLine(rect.rect.right, 0, rect.rect.right, getMeasuredHeight(), paint);
    }

    private void drawKeyPad(Canvas canvas) {
        for (KeyRect rect : keyRects) {

          /*  if (rect == keyRects.get(9) || rect == keyRects.get(11)) {
                Paint tempPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                tempPaint.setColor(Color.GRAY);
                canvas.drawRect(rect.rect, tempPaint);
            }*/

            //textPaint.setColor(Color.argb(255, 0, 0, 0));
            if (rect == keyRects.get(11) || (rect == keyRects.get(9) && fingerIconVisible &&
                    rect.mBitmapValue != null)) {
                float iconWidth = rect.mBitmapValue.getScaledWidth(canvas);
                float iconHeight = rect.mBitmapValue.getHeight();
                canvas.drawBitmap(rect.mBitmapValue, rect.rect.exactCenterX() - iconWidth / 2,
                        (rect.rect.exactCenterY() - iconHeight / 5), paint);
            } else {
                textPaint.setColor(Color.WHITE);
                float textWidth = textPaint.measureText(rect.value);
                canvas.drawText(rect.value, rect.rect.exactCenterX() - textWidth / 2,
                        rect.rect.exactCenterY() + textPaint.getTextSize() / 2, textPaint);
                if (rect.hasRippleEffect) {
                    circlePaint.setAlpha(rect.circleAlpha);
                    canvas.drawCircle(rect.rect.exactCenterX(), rect.rect.exactCenterY(),
                            rect.rippleRadius, circlePaint);
                }
            }

            if (dividerVisible) {
                if (rect == keyRects.get(2) || rect == keyRects.get(5) || rect == keyRects.get(8)) {
                    drawDividerHorizontal(canvas, rect);
                }

                if (rect == keyRects.get(0) || rect == keyRects.get(1)) {
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
            double height = MeasureSpec.getSize(heightMeasureSpec) - (keyHeight * KEY_PAD_ROWS);
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
                touchXMap.put(pointerDownId, (int) event.getX());
                touchYMap.put(pointerDownId, (int) event.getY());
                longPressed(pointerDownId);
                break;

            case MotionEvent.ACTION_UP:
                int pointerUpId = event.getPointerId(event.getActionIndex());
                int pointerUpIndex = event.findPointerIndex(pointerUpId);
                int eventX = (int) event.getX(pointerUpIndex);
                int eventY = (int) event.getY(pointerUpIndex);
                handler.removeCallbacks(mLongPress);
                mLongPressed = false;
                findKeyPressed(touchXMap.get(pointerUpId), touchYMap.get(pointerUpId), eventX, eventY);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("Pointer", "down");
                int pointerActionDownId = event.getPointerId(event.getActionIndex());
                touchXMap.put(pointerActionDownId, (int) event.getX(event.getActionIndex()));
                touchYMap.put(pointerActionDownId, (int) event.getY(event.getActionIndex()));
                longPressed(pointerActionDownId);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerActionUpIndex = event.getActionIndex();
                int pointerActionUpId = event.getPointerId(pointerActionUpIndex);
                int eventPointerX = (int) event.getX(pointerActionUpIndex);
                int eventPointerY = (int) event.getY(pointerActionUpIndex);
                handler.removeCallbacks(mLongPress);
                mLongPressed = false;
                findKeyPressed(touchXMap.get(pointerActionUpId),
                        touchYMap.get(pointerActionUpId), eventPointerX, eventPointerY);
                break;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    private void findKeyPressed(int downEventX, int downEventY, int upEventX, int upEventY) {
        for (final KeyRect keyRect : keyRects) {
            if (keyRect.rect.contains(downEventX, downEventY)
                    && keyRect.rect.contains(upEventX, upEventY)) {
                keyRect.playRippleAnim(new KeyRect.RippleAnimListener() {
                    @Override
                    public void onStart() {
                        if (keyRect.value.equals("") && fingerIconVisible) {
                            ToastUtil.display(getContext(), R.string.finger_auth_toast);
                        }
                        if (passCodeText.length() == digits && isClearText() &&
                                !keyRect.value.equals(eraseChar) && getErrorChar() == -1) {
                            passCodeText = "";
                            setBlockInputChar(false);
                        }
                        int length = passCodeText.length();
                        if (keyRect.value.equals(eraseChar)) {
                            if (isClearText()) {
                                passCodeText = "";
                                setBlockInputChar(false);
                                setClearText(false);
                            } else if (length > 0) {
                                setBlockInputChar(false);
                                passCodeText = passCodeText.substring(0, passCodeText.length() - 1);
                            }
                        } else if (!keyRect.value.isEmpty() && length <= digits && getErrorChar() == -1) {
                            if (!isBlockInputChar()) {
                                setClearText(false);
                                passCodeText += keyRect.value;
                            }
                            if (passCodeText.length() == digits) setBlockInputChar(true);
                        }
                        if (!keyRect.value.isEmpty()) {
                            notifyListener();
                        }
                    }

                    @Override
                    public void onEnd() {

                    }
                });
            } else if (keyRect.rect.contains(downEventX, downEventY) && upEventX == 0 && upEventY == 0 && keyRect.value.equals(eraseChar)) {
                int length = passCodeText.length();
                if (length > 0) {
                    while (mLongPressed) {
                        if (passCodeText.length() > 0) {
                            setBlockInputChar(false);
                            passCodeText = passCodeText.substring(0, passCodeText.length() - 1);
                            notifyListener();
                        } else {
                            mLongPressed = false;
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
                    mLongPressed = true;
                    findKeyPressed(touchXMap.get(downEventId), touchYMap.get(downEventId), 0, 0);
                }
            };
        }

        handler.postDelayed(mLongPress, 1000);
    }

    public void reset() {
        this.passCodeText = "";
        invalidateAndNotifyListener();
    }

    public interface TextChangeListener {
        void onTextChanged(String text);
    }

    private void invalidateAndNotifyListener() {
        Log.i("New text", passCodeText);
        if (textChangeListener != null) {
            textChangeListener.onTextChanged(passCodeText);
        }
    }

    private void notifyListener() {
        if (textChangeListener != null) {
            textChangeListener.onTextChanged(passCodeText);
        }
    }

    public void setOnTextChangeListener(TextChangeListener listener) {
        this.textChangeListener = listener;
    }

    public void removeOnTextChangeListener() {
        this.textChangeListener = null;
    }

    public void setError(boolean reset) {
        if (reset) {
            reset();
            setBlockInputChar(false);
        }
        for (KeyRect keyRect : keyRects) {
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
        for (KeyRect rect : keyRects) {
            if (rect == keyRects.get(9) && fingerIconVisible) {
                rect.setBitmapValue(returnScaledBitmapFromResource(R.drawable.form_filled,
                        fingerIconWidth, fingerIconHeight));
            }
        }
        invalidate();
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
