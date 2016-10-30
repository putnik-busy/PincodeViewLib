package com.example.pincodeviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Sergey Rodionov
 */
public class CircleView extends View {
    private int numberFilled;
    private int numberAll;
    private Bitmap filledDrawable;
    private Bitmap emptyDrawable;
    private Bitmap errorDrawable;
    private int drawableSpacing;

    private Paint paint;
    private int drawableWidth;
    private int drawableHeight;
    private int drawableStartX;
    private int drawableStartY;

    private Paint circlePaint;

    private Context context;
    private boolean mErrorChar;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.context = context;
        TypedArray values = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleView, defStyleAttr, defStyleRes);
        try {
            numberAll = values.getInteger(R.styleable.CircleView_count_all, 4);
            numberFilled = values.getInteger(R.styleable.CircleView_count_filled, 0);
            mErrorChar = values.getBoolean(R.styleable.CircleView_error_char, false);

            float drawableSize = values.getDimension(R.styleable.CircleView_drawable_size,
                    getResources().getDimension(R.dimen.circle_size));
            drawableWidth = (int) drawableSize;
            drawableHeight = (int) drawableSize;

            filledDrawable = getBitmap(values.getResourceId(R.styleable.CircleView_filled_drawable, -1));
            emptyDrawable = getBitmap(values.getResourceId(R.styleable.CircleView_empty_drawable, -1));
            drawableSpacing = (int) values.getDimension(R.styleable.CircleView_drawable_spacing,
                    getResources().getDimension(R.dimen.circle_space));
            errorDrawable = getBitmap(values.getResourceId(R.styleable.CircleView_error_drawable, -1));

        } catch (Exception e) {
            e.printStackTrace();
        }
        values.recycle();
        preparePaint();
    }

    private void preparePaint() {
        paint = new Paint(TextPaint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL);
    }

    private void computeDrawableStartXY() {
        int totalReqWidth = getTotalWidth();
        drawableStartX = getMeasuredWidth() / 2 - totalReqWidth / 2;
        drawableStartY = (drawableHeight + drawableSpacing) / 2 - drawableHeight / 2;
    }

    private int getTotalWidth() {
        int totalDrawableWidth = numberAll * drawableWidth;
        int totalPaddingWidth = drawableSpacing * (numberAll - 1);
        int totalReqWidth = totalDrawableWidth + totalPaddingWidth;
        return totalReqWidth;
    }

    private Bitmap getBitmap(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDrawable(canvas);
    }

    private void drawDrawable(Canvas canvas) {
        paint.setAlpha(255);
        int x = drawableStartX, y = drawableStartY;
        int totalContentWidth = drawableWidth + drawableSpacing;
        for (int i = 1; i <= numberFilled; i++) {
            canvas.drawBitmap(filledDrawable, x, y, paint);
            x += totalContentWidth;
            if (isErrorChar()) {
                canvas.drawBitmap(errorDrawable, x, y, paint);
                x += totalContentWidth;
                break;
            }
        }
        for (int i = 1; i <= (numberAll - numberFilled); i++) {
            canvas.drawBitmap(emptyDrawable, x, y, paint);
            x += totalContentWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredWidth = 0, measuredHeight = 0;
        int totalReqWidth = getTotalWidth();
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = totalReqWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            double height = drawableHeight + drawableSpacing;
            measuredHeight = (int) height;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
        computeDrawableStartXY();
    }

    private void setFilledCount(int count) {
        numberFilled = count > numberAll ? numberFilled : count;
        invalidate(drawableStartX, drawableStartX,
                drawableStartX + getMeasuredWidth(),
                drawableStartY + getMeasuredHeight());
    }

    public int getNumberFilled() {
        return numberFilled;
    }

    public void setNumberFilled(int numberFilled) {
        this.numberFilled = numberFilled;
        invalidate();
    }

    public int getNumberAll() {
        return numberAll;
    }

    public void setNumberAll(int numberAll) {
        this.numberAll = numberAll;
        invalidate();
    }

    public void setNumberAll(CircleView customView, int numberAll) {
        setNumberAll(numberAll);
    }

    @BindingAdapter("count_all")
    public static void numberAll(CircleView view, int number_all) {
        view.setNumberAll(number_all);
    }

    @BindingAdapter("count_filled")
    public static void numberFilled(CircleView view, int number_filled) {
        view.setNumberFilled(number_filled);
    }

    @BindingAdapter("error_char")
    public static void errorChar(CircleView view, boolean errorChar) {
        view.setErrorChar(errorChar);
    }

    public boolean isErrorChar() {
        return mErrorChar;
    }

    public void setErrorChar(boolean errorChar) {
        mErrorChar = errorChar;
    }

}