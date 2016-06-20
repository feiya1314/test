package com.example.feiya.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by feiya on 2016/5/14.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private SurfaceView surfaceView;
    private Canvas mCanvas;

    public MySurfaceView(Context context){
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public MySurfaceView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
