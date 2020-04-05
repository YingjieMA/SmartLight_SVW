package com.csvw.myj.smartlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.annotation.SuppressLint;

@SuppressLint("AppCompatCustomView")
public class LightView extends ImageView {
    private float bitmapX;
    private float bitmapY;
    public Bitmap bitmap;
    public Paint paint;
    public Matrix matrix;
    public int width;
    public int height;
    private float ratio;
    private int ResourecId;
    private Integer color;
    Bitmap ScaledBitmap;
//    private Drawable drawable;
    public LightView(Context context,int ResourceId,float bitmapX,float bitmapY) {
        super(context);
        this.bitmapX= bitmapX;
        this.bitmapY=bitmapY;
        this.ResourecId = ResourceId;
        init();

    }
    public LightView(Context context,int ResourceId,float bitmapX,float bitmapY,int color) {
        super(context);
        this.bitmapX= bitmapX;
        this.bitmapY=bitmapY;
        this.ResourecId = ResourceId;
        this.color = color;
        init();

    }
    private void init(){
        bitmap = BitmapFactory.decodeResource(this.getResources(), ResourecId);
        Log.d("TAG", "ratio="+ bitmap.getWidth()+"/"+bitmap.getHeight());
        ratio = (float) bitmap.getWidth()/bitmap.getHeight();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.init();
        if (color != null){
            Bitmap newBitmap = makeTintBitmap(bitmap,color);
            ScaledBitmap = Bitmap.createScaledBitmap(newBitmap,width,height,false);
        }else{
            ScaledBitmap = Bitmap.createScaledBitmap(bitmap,width,height,false);
        }
        canvas.drawBitmap(ScaledBitmap,bitmapX+getPaddingLeft(),bitmapY+getPaddingTop(),paint);
        if (bitmap.isRecycled()){
            bitmap.recycle();
        }
        if (ScaledBitmap.isRecycled()){
            ScaledBitmap.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthResult = 0;
        //view根据xml中layout_width和layout_height测量出对应的宽度和高度值，
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightResult = 0;
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthSpecMode){
            case MeasureSpec.UNSPECIFIED:
                widthResult = widthSpecSize;
//                widthResult = 1080;
                break;
            case MeasureSpec.AT_MOST://wrap_content时候
//                widthResult = getContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                //当xml布局中是准确的值，比如200dp是，判断一下当前view的宽度和准确值,取两个中大的，这样的好处是当view的宽度本事超过准确值不会出界
                //其实可以直接使用准确值
                widthResult = Math.min(getContentWidth(), widthSpecSize);
//                widthResult = widthSpecSize;
                Log.d("TAG", "ratio="+ ratio);
                heightResult = (int) (widthResult/ratio + 0.5f);

                break;
        }

//        switch (heightSpecMode){
//            case MeasureSpec.UNSPECIFIED:
//                heightResult = heightSpecSize;
//                break;
//            case MeasureSpec.AT_MOST://wrap_content时候
//                heightResult = getContentHeight();
//                break;
//            case MeasureSpec.EXACTLY:
//                heightResult = Math.max(getContentHeight(), heightSpecSize);
//                break;
//        }
//        setMeasuredDimension(widthResult, heightResult);
        setMeasuredDimension(widthSpecSize, (int) (widthSpecSize/1.7778+0.5f));
        width = widthResult;
        height = heightResult;
        Log.d("TAG", String.valueOf(height));
    }

    private int getContentWidth(){
        float contentWidth = bitmap.getWidth()+getPaddingLeft()+getPaddingRight();
        Log.d("TAG", "getContentWidth: contentWidth="+contentWidth);
        return (int)contentWidth;
    }
//
//    int getContentHeight(){
//        float contentHeight = bitmap.getHeight()+getPaddingTop()+getPaddingBottom();
//        Log.d("TAG", "getContentWidth: contentHeight="+contentHeight);
//        return (int)contentHeight;
//    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        startX = w/2 - thumbUp.getWidth()/2 - getPaddingLeft()/2 -  getPaddingRight()/2;
//        startY = h/2 - thumbUp.getHeight()/2 - getPaddingTop()/2 -  getPaddingBottom()/2;
        Log.d("TAG", "onSizeChanged: w="+w+",h="+h+",startX="+bitmapX+",startY="+bitmapY);
    }

    public static Bitmap makeTintBitmap(Bitmap inputBitmap, int tintColor) {
        if (inputBitmap == null) {
            return null;
        }

        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inputBitmap, 0, 0, paint);
        return outputBitmap;
    }


}
