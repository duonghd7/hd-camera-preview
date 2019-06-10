package com.duonghd.test.kdb.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.duonghd.test.kdb.R;

/**
 * Create on 2019-06-01
 *
 * @author duonghd
 */
public class GlideView extends View {
    private GMode gMode;
    private Paint mPaint;
    private Bitmap glBitmap;
    private Rect glRect, mPointRect0, mPointRect1, mPointRect2, mPointRect3;
    private int mCurSelectPoint = -1;
    private float m_fScreenScale = 0;

    public GlideView(Context context) {
        super(context);
        init(context);
    }

    public GlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        m_fScreenScale = Math.abs(displayMetrics.density / 1);

        gMode = GMode.MODE_SELECTION;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#F8B621"));
        mPaint.setAntiAlias(true);

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_pain_glide);
        glBitmap = drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

        mPointRect0 = new Rect();
        mPointRect1 = new Rect();
        mPointRect2 = new Rect();
        mPointRect3 = new Rect();
        glRect = new Rect();

        addDot();
    }

    private Bitmap bmDot;

    private void addDot() {
        bmDot = Bitmap.createBitmap(9, 9, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmDot);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setAntiAlias(true);

        canvas.drawCircle(
                canvas.getWidth() / 2f, // cx
                canvas.getHeight() / 2f, // cy
                canvas.getWidth() / 2f, // Radius
                paint // Paint
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        initPointer(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPointer(int sizeWidth, int sizeHeight) {
        int size = Math.min(sizeWidth, sizeHeight);
        int size40 = 120;
        int left = size / 4 - size40 / 2;
        int top = left;
        int right = left + size40;
        int bottom = top + size40;

        if (mPointRect0 != null && mPointRect0.isEmpty())
            mPointRect0.set(left, top, right, bottom);

        if (mPointRect1 != null && mPointRect1.isEmpty())
            mPointRect1.set(left + size / 2, top, right + size / 2, bottom);

        if (mPointRect2 != null && mPointRect2.isEmpty())
            mPointRect2.set(left, top + size / 2, right, bottom + size / 2);

        if (mPointRect3 != null && mPointRect3.isEmpty())
            mPointRect3.set(left + size / 2, top + size / 2, right + size / 2, bottom + size / 2);

        initGlRect(mPointRect0, mPointRect3);
    }

    private void initGlRect(Rect rect0, Rect rect1) {
        glRect.set(
                Math.min(rect0.centerX(), rect1.centerX()),
                Math.min(rect0.centerY(), rect1.centerY()),
                Math.max(rect0.centerX(), rect1.centerX()),
                Math.max(rect0.centerY(), rect1.centerY()));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (glBitmap != null && !glBitmap.isRecycled() && glRect.width() > 0 && glRect.height() > 0) {
            canvas.drawBitmap(
                    Bitmap.createScaledBitmap(glBitmap, glRect.width(), glRect.height(), false),
                    glRect.left, glRect.top, mPaint);
        }

        //showDot(canvas);
        showPointer(canvas);
    }

    private void showDot(Canvas canvas) {
        int halfWidthP0 = bmDot.getWidth() / 2;
        int halfHeight0 = bmDot.getHeight() / 2;

        if (bmDot != null && !bmDot.isRecycled()) {
            canvas.drawBitmap(bmDot, mPointRect0.left - halfWidthP0, mPointRect0.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect0.right - halfWidthP0, mPointRect0.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect0.left - halfWidthP0, mPointRect0.bottom - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect0.right - halfWidthP0, mPointRect0.bottom - halfHeight0, mPaint);

            canvas.drawBitmap(bmDot, mPointRect1.left - halfWidthP0, mPointRect1.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect1.right - halfWidthP0, mPointRect1.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect1.left - halfWidthP0, mPointRect1.bottom - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect1.right - halfWidthP0, mPointRect1.bottom - halfHeight0, mPaint);

            canvas.drawBitmap(bmDot, mPointRect2.left - halfWidthP0, mPointRect2.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect2.right - halfWidthP0, mPointRect2.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect2.left - halfWidthP0, mPointRect2.bottom - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect2.right - halfWidthP0, mPointRect2.bottom - halfHeight0, mPaint);

            canvas.drawBitmap(bmDot, mPointRect3.left - halfWidthP0, mPointRect3.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect3.right - halfWidthP0, mPointRect3.top - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect3.left - halfWidthP0, mPointRect3.bottom - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect3.right - halfWidthP0, mPointRect3.bottom - halfHeight0, mPaint);
        }
    }

    private void showPointer(Canvas canvas) {
        int halfWidthP0 = bmDot.getWidth() / 2;
        int halfHeight0 = bmDot.getHeight() / 2;

        if (bmDot != null && !bmDot.isRecycled()) {
            canvas.drawBitmap(bmDot, mPointRect0.centerX() - halfWidthP0, mPointRect0.centerY() - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect1.centerX() - halfWidthP0, mPointRect1.centerY() - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect2.centerX() - halfWidthP0, mPointRect2.centerY() - halfHeight0, mPaint);
            canvas.drawBitmap(bmDot, mPointRect3.centerX() - halfWidthP0, mPointRect3.centerY() - halfHeight0, mPaint);
        }
    }

    public void setGMode(GMode gMode) {
        this.gMode = gMode;
    }

    public enum GMode {
        MODE_NONE,
        MODE_SELECTION,
        MODE_RESULT
    }

    private int lastX, lastY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gMode != GMode.MODE_SELECTION) return true;

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mPointRect0.contains(x, y)) {
                    mCurSelectPoint = 0;
                    int min = Math.min(mPointRect3.centerX() - x, mPointRect3.centerY() - y);
                    mPointRect0.set(mPointRect3.left - min, mPointRect3.top - min, mPointRect3.right - min, mPointRect3.bottom - min);
                    mPointRect1.set(mPointRect3.left, mPointRect3.top - min, mPointRect3.right, mPointRect3.bottom - min);
                    mPointRect2.set(mPointRect3.left - min, mPointRect3.top, mPointRect3.right - min, mPointRect3.bottom);
                    initGlRect(mPointRect0, mPointRect3);
                    invalidate();
                } else if (mPointRect1.contains(x, y)) {
                    mCurSelectPoint = 1;
                    int min = Math.min(x - mPointRect2.centerX(), mPointRect2.centerY() - y);
                    mPointRect0.set(mPointRect2.left, mPointRect2.top - min, mPointRect2.right, mPointRect2.bottom - min);
                    mPointRect1.set(mPointRect2.left + min, mPointRect2.top - min, mPointRect2.right + min, mPointRect2.bottom - min);
                    mPointRect3.set(mPointRect2.left + min, mPointRect2.top, mPointRect2.right + min, mPointRect2.bottom);
                    initGlRect(mPointRect1, mPointRect2);
                    invalidate();
                } else if (mPointRect2.contains(x, y)) {
                    mCurSelectPoint = 2;
                    int min = Math.min(mPointRect1.centerX() - x, y - mPointRect1.centerY());
                    mPointRect0.set(mPointRect1.left - min, mPointRect1.top, mPointRect1.right - min, mPointRect1.bottom);
                    mPointRect2.set(mPointRect1.left - min, mPointRect1.top + min, mPointRect1.right - min, mPointRect1.bottom + min);
                    mPointRect3.set(mPointRect1.left, mPointRect1.top + min, mPointRect1.right, mPointRect1.bottom + min);
                    initGlRect(mPointRect1, mPointRect2);
                    invalidate();
                } else if (mPointRect3.contains(x, y)) {
                    mCurSelectPoint = 3;
                    int min = Math.min(x - mPointRect0.centerX(), y - mPointRect0.centerY());
                    mPointRect1.set(mPointRect0.left + min, mPointRect0.top, mPointRect0.right + min, mPointRect0.bottom);
                    mPointRect2.set(mPointRect0.left, mPointRect0.top + min, mPointRect0.right, mPointRect0.bottom + min);
                    mPointRect3.set(mPointRect0.left + min, mPointRect0.top + min, mPointRect0.right + min, mPointRect0.bottom + min);
                    initGlRect(mPointRect0, mPointRect3);
                    invalidate();
                } else if (glRect.contains(x, y)) {
                    mCurSelectPoint = 10;
                    lastX = x;
                    lastY = y;
                } else {
                    mCurSelectPoint = -1;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (x < m_fScreenScale * 10 || y < m_fScreenScale * 10 || this.getWidth() - m_fScreenScale * 10 < x || this.getHeight() - m_fScreenScale * 10 < y)
                    break;
                switch (mCurSelectPoint) {
                    case -1:
                        break;

                    case 0: {
                        int min = Math.min(mPointRect3.centerX() - x, mPointRect3.centerY() - y);
                        mPointRect0.set(mPointRect3.left - min, mPointRect3.top - min, mPointRect3.right - min, mPointRect3.bottom - min);
                        mPointRect1.set(mPointRect3.left, mPointRect3.top - min, mPointRect3.right, mPointRect3.bottom - min);
                        mPointRect2.set(mPointRect3.left - min, mPointRect3.top, mPointRect3.right - min, mPointRect3.bottom);
                        initGlRect(mPointRect0, mPointRect3);
                        invalidate();
                    }
                    break;

                    case 1: {
                        int min = Math.min(x - mPointRect2.centerX(), mPointRect2.centerY() - y);
                        mPointRect0.set(mPointRect2.left, mPointRect2.top - min, mPointRect2.right, mPointRect2.bottom - min);
                        mPointRect1.set(mPointRect2.left + min, mPointRect2.top - min, mPointRect2.right + min, mPointRect2.bottom - min);
                        mPointRect3.set(mPointRect2.left + min, mPointRect2.top, mPointRect2.right + min, mPointRect2.bottom);
                        initGlRect(mPointRect1, mPointRect2);
                        invalidate();
                    }
                    break;

                    case 2: {
                        int min = Math.min(mPointRect1.centerX() - x, y - mPointRect1.centerY());
                        mPointRect0.set(mPointRect1.left - min, mPointRect1.top, mPointRect1.right - min, mPointRect1.bottom);
                        mPointRect2.set(mPointRect1.left - min, mPointRect1.top + min, mPointRect1.right - min, mPointRect1.bottom + min);
                        mPointRect3.set(mPointRect1.left, mPointRect1.top + min, mPointRect1.right, mPointRect1.bottom + min);
                        initGlRect(mPointRect1, mPointRect2);
                        invalidate();
                    }
                    break;

                    case 3: {
                        int min = Math.min(x - mPointRect0.centerX(), y - mPointRect0.centerY());
                        mPointRect1.set(mPointRect0.left + min, mPointRect0.top, mPointRect0.right + min, mPointRect0.bottom);
                        mPointRect2.set(mPointRect0.left, mPointRect0.top + min, mPointRect0.right, mPointRect0.bottom + min);
                        mPointRect3.set(mPointRect0.left + min, mPointRect0.top + min, mPointRect0.right + min, mPointRect0.bottom + min);
                        initGlRect(mPointRect0, mPointRect3);
                        invalidate();
                    }
                    break;

                    case 10: {
                        int moveX = lastX - x;
                        int moveY = lastY - y;
                        mPointRect0.set(mPointRect0.left - moveX, mPointRect0.top - moveY, mPointRect0.right - moveX, mPointRect0.bottom - moveY);
                        mPointRect1.set(mPointRect1.left - moveX, mPointRect1.top - moveY, mPointRect1.right - moveX, mPointRect1.bottom - moveY);
                        mPointRect2.set(mPointRect2.left - moveX, mPointRect2.top - moveY, mPointRect2.right - moveX, mPointRect2.bottom - moveY);
                        mPointRect3.set(mPointRect3.left - moveX, mPointRect3.top - moveY, mPointRect3.right - moveX, mPointRect3.bottom - moveY);
                        initGlRect(mPointRect0, mPointRect3);
                        invalidate();
                        lastX = x;
                        lastY = y;
                    }
                    break;
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
