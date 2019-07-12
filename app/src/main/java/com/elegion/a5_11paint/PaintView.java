package com.elegion.a5_11paint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class PaintView extends View {

    private int mCurrentColor = Color.BLACK;
    private int mBrushSize = 10;
    private int mBackgroundColor = Color.WHITE;

    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Path mPath;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private List<FingerPath> mPaths = new ArrayList<>(0);


    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(DisplayMetrics metrics) {

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(mBackgroundColor);
        for (FingerPath fp : mPaths) {
            mPaint.setColor(fp.mColor);
            mPaint.setStrokeWidth(fp.mStrokeWidth);
            mCanvas.drawPath(fp.mPath, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(mCurrentColor, mBrushSize, mPath);
        mPaths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void clear() {
        mBackgroundColor = Color.WHITE;
        mPaths.clear();
        invalidate();
    }

    public void brush() {
        String[] mChoose = {"10", "20", "30", "40"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Brush")
                .setCancelable(false)
                .setSingleChoiceItems(mChoose, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        mBrushSize = 10;
                                        break;
                                    case 1:
                                        mBrushSize = 20;
                                        break;
                                    case 2:
                                        mBrushSize = 30;
                                        break;
                                    case 3:
                                        mBrushSize = 40;
                                        break;
                                }
                            }
                        })
                .setPositiveButton("Ok", null);
        Dialog dialog = builder.create();
        dialog.show();
        invalidate();
    }

    public void color() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), mCurrentColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mCurrentColor = color;
            }
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        dialog.show();
        invalidate();
    }

    public void back() {
        if (mPaths.size() > 0) {
            mPaths.remove(mPaths.size() - 1);
        }
        invalidate();
    }
}
