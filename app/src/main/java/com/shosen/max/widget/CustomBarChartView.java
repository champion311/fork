package com.shosen.max.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBarChartView extends View {

    private Paint mPaint, mChartPaint, mBitmapPaint;
    private Rect mBound;

    private float mStartWidth, mHeight, mWidth, mChartWidth, mSize;
    private int lineColor, leftColor, lefrColorBottom, selectLeftColor;
    private int mColumnCount = 8;
    private String[] dataArray;
    private String[] amountsArray;
    private int[] list;
    private BitmapShader mBitmapShader;

    private int headPos = -1;

    private Bitmap mBitmap;


    public static float MAX_VALUE = 210;

    public CustomBarChartView(Context context) {
        this(context, null);
    }

    public CustomBarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().
                obtainStyledAttributes(attrs, R.styleable.CustomBarChartView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attrIndex = array.getIndex(i);
            switch (attrIndex) {
                case R.styleable.CustomBarChartView_xyColor:
                    lineColor = array.getColor(attrIndex, Color.BLACK);
                    break;
                case R.styleable.CustomBarChartView_leftColor:
                    // 默认颜色设置为黑色
                    leftColor = array.getColor(attrIndex, Color.BLACK);
                    break;
                case R.styleable.CustomBarChartView_leftColorBottom:
                    lefrColorBottom = array.getColor(attrIndex, Color.BLACK);
                    break;
                case R.styleable.CustomBarChartView_selectLeftColor:
                    // 默认颜色设置为黑色
                    selectLeftColor = array.getColor(attrIndex, Color.BLACK);
                    break;
                case R.styleable.CustomBarChartView_columnCount:
                    mColumnCount = array.getInteger(attrIndex, 8);
                    break;
                default:
                    bringToFront();
            }
        }
        dataArray = context.getResources().getStringArray(R.array.level);
        amountsArray = context.getResources().getStringArray(R.array.level_count);
        list = context.getResources().getIntArray(R.array.level_scale);
        array.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize / 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize / 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        mStartWidth = getWidth() / (calculationValue());
        mSize = mWidth / 26;
        mChartWidth = getWidth() / (calculationValue()) - mSize / 2;
        super.onLayout(changed, left, top, right, bottom);
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_text_color));
        mPaint.setTextSize(DensityUtils.sp2px(getContext(), 12));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mBound = new Rect();
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataArray != null && list != null) {
            for (int i = 0; i < mColumnCount; i++) {
                //画坐标
                mPaint.getTextBounds(dataArray[i], 0, dataArray[i].length(), mBound);
                //- mBound.width() * 1 / 2
                canvas.drawText(dataArray[i],
                        mStartWidth,
                        mHeight - 60 + mBound.height() * 1 / 2, mPaint);
                //画数字
                mPaint.getTextBounds(amountsArray[i], 0, amountsArray[i].length(), mBound);
                canvas.drawText(amountsArray[i],
                        mStartWidth,
                        getAmountTop(i), mPaint);
                mStartWidth += getWidth() / (calculationValue());
            }
        }

        for (int i = 0; i < mColumnCount; i++) {
            mChartPaint.setStyle(Paint.Style.FILL);
            if (list != null && list.length > 0) {
                LinearGradient lg = new LinearGradient(mChartWidth, mChartWidth + mSize, mHeight - 100,
                        getItemHeight(list[i]), lefrColorBottom, leftColor, Shader.TileMode.MIRROR);
                mChartPaint.setShader(lg);
                //}
                //画柱状图
                RectF rectF = new RectF();
                rectF.left = mChartWidth;
                rectF.right = mChartWidth + mSize;
                rectF.bottom = mHeight - 100;
                rectF.top = getItemHeight(list[i]);
                canvas.drawRoundRect(rectF, 20, 20, mChartPaint);
                mChartWidth += getWidth() / (calculationValue());
            }
        }
        drawHeadImg(canvas);

    }

    /**
     * 绘制头像
     *
     * @param canvas
     */
    public void drawHeadImg(Canvas canvas) {
        if (headPos < 0 || headPos > mColumnCount) {
            return;
        }
        if (mBitmap != null) {
            float left = getWidth() / calculationValue() * (headPos + 1) - mBitmap.getWidth() / 2;
            float top = getAmountTop(headPos) - DensityUtils.dip2px(getContext(), 15) -
                    mBitmap.getHeight();
            canvas.drawBitmap(mBitmap, left, top, mBitmapPaint);
        }
    }


    public void setList() {
        this.setList(-1, null);
    }

    public void setList(int headPost, Bitmap mBitmap) {
        this.headPos = headPost;
        this.mBitmap = mBitmap;
        mSize = getWidth() / 26;
        mStartWidth = getWidth() / calculationValue();
        mChartWidth = (getWidth() / calculationValue()) - mSize / 2;
        if (mBitmap != null) {
            setBitmapShader(mBitmap);
        }
        invalidate();
    }

    private void setBitmapShader(Bitmap mBitmap) {
        if (mBitmap == null) {
            return;
        }
        mBitmapPaint = new Paint();
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
    }

    public float calculationValue() {
        return 9;
    }

    public float getItemHeight(float value) {
        if (value == 0) {
            value = 16666f;
        }
        return (mHeight - 100) * (1 - (value / MAX_VALUE));
    }

    public float getAmountTop(int i) {
        return getItemHeight(list[i]) - DensityUtils.dip2px(getContext(), 6);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {

        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            mSize = getWidth() / 26;
            mStartWidth = getWidth() / calculationValue();
            mChartWidth = getWidth() / calculationValue() - mSize / 2;
        }
    }
}
