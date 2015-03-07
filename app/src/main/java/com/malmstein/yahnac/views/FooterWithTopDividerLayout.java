package com.malmstein.yahnac.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.malmstein.yahnac.R;

public class FooterWithTopDividerLayout extends LinearLayout {

    private static final int HEIGHT_NONE = 0;

    private final Paint dividerPaint;

    public FooterWithTopDividerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterWithTopDividerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dividerPaint = new Paint();
        dividerPaint.setStrokeCap(Paint.Cap.SQUARE);

        initFrom(context, attrs);
        setWillNotDraw(false);
    }

    private void initFrom(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FooterWithTopDividerLayout);
        if (a != null) {
            try {
                int dividerColor = a.getColor(R.styleable.FooterWithTopDividerLayout_dividerColor, Color.TRANSPARENT);
                setDividerColor(dividerColor);

                int dividerHeight = a.getDimensionPixelSize(R.styleable.FooterWithTopDividerLayout_dividerHeight, HEIGHT_NONE);
                setDividerHeight(dividerHeight);
            } finally {
                a.recycle();
            }
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")  // The divider height is the stroke width.
    public void setDividerHeight(int heightPx) {
        dividerPaint.setStrokeWidth(heightPx);
    }

    public float getDividerHeight() {
        return dividerPaint.getStrokeWidth();
    }


    public void setDividerColor(int color) {
        dividerPaint.setColor(color);
    }

    public int getDividerColor() {
        return dividerPaint.getColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = dividerPaint.getStrokeWidth() / 2;
        canvas.drawLine(getLeft(), top, getRight(), top, dividerPaint);
    }

}
