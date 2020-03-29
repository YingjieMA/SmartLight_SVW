package com.csvw.myj.smartlight.LightView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.csvw.myj.smartlight.R;

public class ReaadingLampView extends View {
    public float bitmapX;
    public float bitmapY;
    public Bitmap bitmap;
    public Paint paint;
    public Matrix matrix;
    public int width;
    public int height;
    public ReaadingLampView(Context context) {
        super(context);
        bitmapX= (float) 0;
        bitmapY=0;
        init();

    }
    private void init(){
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.reading_lamp_2);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawBitmap(bitmap,bitmapX+getPaddingLeft(),bitmapY+getPaddingTop(),paint);
        invalidate();
        matrix = new Matrix();
        matrix.reset();
        matrix.postScale(0.5f, 0.5f);
        bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        if (bitmap.isRecycled()){
            bitmap.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthResult = 0;
        //view根据xml中layout_width和layout_height测量出对应的宽度和高度值，
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthSpecMode){
            case MeasureSpec.UNSPECIFIED:
                widthResult = widthSpecSize;
//                widthResult = 1080;
                break;
            case MeasureSpec.AT_MOST://wrap_content时候
                widthResult = getContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                //当xml布局中是准确的值，比如200dp是，判断一下当前view的宽度和准确值,取两个中大的，这样的好处是当view的宽度本事超过准确值不会出界
                //其实可以直接使用准确值
                widthResult = Math.max(getContentWidth(), widthSpecSize);
                break;
        }
        int heightResult = 0;
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightSpecMode){
            case MeasureSpec.UNSPECIFIED:
                heightResult = heightSpecSize;
                break;
            case MeasureSpec.AT_MOST://wrap_content时候
                heightResult = getContentHeight();
                break;
            case MeasureSpec.EXACTLY:
                heightResult = Math.max(getContentHeight(), heightSpecSize);
                break;
        }
        setMeasuredDimension(widthResult, heightResult);
        width = widthResult;
        height = heightResult;
    }

    private int getContentWidth(){
        float contentWidth = bitmap.getWidth()+getPaddingLeft()+getPaddingRight();
        Log.d("TAG", "getContentWidth: contentWidth="+contentWidth);
        return (int)contentWidth;
    }

    int getContentHeight(){
        float contentHeight = bitmap.getHeight()+getPaddingTop()+getPaddingBottom();
        Log.d("TAG", "getContentWidth: contentHeight="+contentHeight);
        return (int)contentHeight;
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        startX = w/2 - thumbUp.getWidth()/2 - getPaddingLeft()/2 -  getPaddingRight()/2;
//        startY = h/2 - thumbUp.getHeight()/2 - getPaddingTop()/2 -  getPaddingBottom()/2;
        Log.d("TAG", "onSizeChanged: w="+w+",h="+h+",startX="+bitmapX+",startY="+bitmapY);
    }



}
