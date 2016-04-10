package com.example.tomsdeath.filemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tomsdeath on 2016/1/25.
 */
public class Drawing extends View {
    Paint paint;
    Path path;
    Bitmap cacheBitmap;
    Canvas mCanvas;

    public Drawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.DITHER_FLAG);
        mCanvas = new Canvas();
        path = new Path();
        cacheBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(cacheBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = (int)event.getX();
        float y = (int)event.getY();
        float prex = 0;
        float prey = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                prex = x;
                prey = y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(prex ,prey, x, y);
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }
}
