package com.example.feiya.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.feiya.fft.Complex1D;
import com.example.feiya.fft.RealDoubleFFT;

import java.util.ArrayList;

/**
 *
 * Created by feiya on 2016/6/12.
 */
public class DrawThread implements Runnable {
    private StrogeData strogeData;

    private ArrayList<int[]> AfterFFT=new ArrayList<>();
    private ArrayList<int[]> Phase = new ArrayList<>();
    private ArrayList<double[]>Shift=new ArrayList<>();

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Paint paint;

    private RealDoubleFFT realDoubleFFT=new RealDoubleFFT(1024);
    private Complex1D complex1D=new Complex1D();

    private boolean Isdrawing=true;
    private int type=0x00;

    private boolean shiftswitch=true;

    public DrawThread(StrogeData strogeData,SurfaceView surfaceView) {
        this.strogeData=strogeData;
        this.surfaceView=surfaceView;
        this.surfaceHolder=this.surfaceView.getHolder();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
    }
    public void startAm(){
        type=0x00;
    }
    public void startPhase(){
        type=0x11;
    }
    public void startSignal(){
        type=0x22;
    }
    public void startShift(){
        type=0x33;
    }
    public boolean getShiftSwitch(){
        return shiftswitch;
    }

    public void setShiftSwitch(boolean change){
        shiftswitch=change;
    }




    @Override
    public void run() {
        int positon=0;
        double[] tempRecdatas;
        double[] formershift;
        double[] currentshift;
        //int oldX=0;
        int oldY=0;
        int currentX=0;
        int currentY;
        while(Isdrawing){
            if(strogeData.IsOverRecDatas(positon)){

                tempRecdatas=strogeData.readdata(positon);
                realDoubleFFT.ft(tempRecdatas,complex1D);
                AfterFFT.add(Calculate.getAm(complex1D));
                Phase.add(Calculate.getIntPhase(complex1D));
                Shift.add(Calculate.getdoubleShift(complex1D));

                switch (type){
                    case 0x00:
                        Log.e("drawAm,position",String.valueOf(positon));
                        drawAM(AfterFFT.get(positon));
                        break;
                    case 0x11:
                        drawPhase(Phase.get(positon));
                        break;
                    case 0x22:
                        drawSignal(strogeData.readdata(positon));
                        break;
                    case 0x33:
                        if(Shift.size()>1){
                            if(shiftswitch) {
                                if (currentX == 0) {
                                    drawXY_shift("观测点一形变量");
                                }
                                formershift = Shift.get(positon-1);
                                currentshift=Shift.get(positon);
                                //currentX=oldX+2;
                                currentY = (int) (currentshift[0]-formershift[0]);
                                currentY=Math.abs(currentY)*100;
                                Log.e("shift",String.valueOf(currentY));
                                drawShift(currentX,oldY,currentX,currentY);
                                currentX+=2;
                                //oldY=currentY;
                                if(currentX>1000){
                                    currentX=0;
                                }
                            }
                            else {
                                if (currentX == 0) {
                                    drawXY_shift("观测点二形变量");
                                }
                                formershift = Shift.get(positon-1);
                                currentshift=Shift.get(positon);
                                currentY = (int) (currentshift[1]-formershift[1]);
                                currentY=Math.abs(currentY);
                                Log.e("shift",String.valueOf(currentY));
                                drawShift(currentX,oldY,currentX,currentY);
                                currentX+=2;
                                //oldY=currentY;
                                if(currentX>1000){
                                    currentX=0;
                                }
                            }
                        }
                        break;
                }
                positon++;
            }
            else{
                try {Thread.sleep(100);}catch (InterruptedException e){e.printStackTrace();}
                //continue;
            }
        }

    }

    public void stop(){
        Isdrawing=false;
    }

    private void drawAM(int[] am){
        surfaceHolder=surfaceView.getHolder();
        Canvas canvas=surfaceHolder.lockCanvas();
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(40);
        paint.setStrokeWidth(2);

        canvas.drawColor(Color.rgb(238, 238, 238));
        canvas.drawText("幅度值", 0, 3, 25, 40, paint);
        canvas.drawLine(20, surfaceView.getHeight()  - 30, surfaceView.getWidth() - 30, surfaceView.getHeight() - 30, paint);
        canvas.drawLine(20,surfaceView.getHeight()-30,20,15,paint);

        for(int i=0;i<am.length;i++){
            canvas.drawLine(40+2*i, surfaceView.getHeight()  - 30,40+2*i, surfaceView.getHeight()-30-am[i], paint);
        }
        canvas.save();
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawPhase(int[] paramArrayOfInt) {
        surfaceHolder = surfaceView.getHolder();
        Canvas localCanvas = surfaceHolder.lockCanvas();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(40.0F);
        paint.setStrokeWidth(2.0F);
        localCanvas.drawColor(Color.rgb(238, 238, 238));
        localCanvas.drawText("相位", 0, 2, 25.0F, 40.0F, this.paint);
        localCanvas.drawLine(20.0F, surfaceView.getHeight() / 2, -30 + surfaceView.getWidth(), surfaceView.getHeight() / 2, this.paint);
        localCanvas.drawLine(20.0F, -30 + surfaceView.getHeight(), 20.0F, 15.0F, paint);
        int oldX=25;
        int oldY=surfaceView.getHeight() / 2 - paramArrayOfInt[0];
        int currentX;
        int currentY;
        // Log.e("phase",String.valueOf(paramArrayOfInt[0]));
        for (int i = 1; i < paramArrayOfInt.length; i++)
        {
            // Log.e("pahse",String.valueOf(paramArrayOfInt[i]));
            currentX=25+i*2;
            currentY=surfaceView.getHeight() / 2 - paramArrayOfInt[i];
            localCanvas.drawLine(oldX, oldY, currentX, currentY, this.paint);
            oldX=currentX;
            oldY=currentY;
        }
        localCanvas.save();
        surfaceHolder.unlockCanvasAndPost(localCanvas);
    }

    private void drawSignal(double[] paramArrayOfDouble) {
        this.surfaceHolder = this.surfaceView.getHolder();
        Canvas localCanvas = this.surfaceHolder.lockCanvas();
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.paint.setTextSize(40.0F);
        this.paint.setStrokeWidth(2.0F);
        localCanvas.drawColor(Color.rgb(238, 238, 238));
        localCanvas.drawText("信号", 0, 2, 25.0F, 40.0F, this.paint);
        localCanvas.drawLine(20.0F, this.surfaceView.getHeight() / 2, -30 + this.surfaceView.getWidth(), this.surfaceView.getHeight() / 2, this.paint);
        localCanvas.drawLine(20.0F, -30 + this.surfaceView.getHeight(), 20.0F, 15.0F, this.paint);
        int i = 25;
        int j = this.surfaceView.getHeight() / 2 - (int)(200.0D * paramArrayOfDouble[0]);
        for (int k = 1; k < paramArrayOfDouble.length; k++)
        {
            int m = this.surfaceView.getHeight() / 2 - (int)(200.0D * paramArrayOfDouble[k]);
            int n = k + 25;
            localCanvas.drawLine(i, j, n, m, this.paint);
            i = n;
            j = m;
        }
        localCanvas.save();
        this.surfaceHolder.unlockCanvasAndPost(localCanvas);
    }

    private void drawXY_shift(String witch){
        this.surfaceHolder = this.surfaceView.getHolder();
        Canvas localCanvas = this.surfaceHolder.lockCanvas();
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.paint.setTextSize(40.0F);
        this.paint.setStrokeWidth(2.0F);
        localCanvas.drawColor(Color.rgb(238, 238, 238));
        localCanvas.drawText("形变量", 0, 3, 25.0F, 40.0F, this.paint);
        localCanvas.drawText(witch,0,witch.length(),220,40,this.paint);
        localCanvas.drawLine(20.0F, this.surfaceView.getHeight() -50, -30 + this.surfaceView.getWidth(), this.surfaceView.getHeight()-50, this.paint);
        localCanvas.drawLine(20.0F, -50 + this.surfaceView.getHeight(), 20.0F, 15.0F, this.paint);

        this.surfaceHolder.unlockCanvasAndPost(localCanvas);
    }

    private void drawShift(int oldX,int oldY,int currentX,int currentY){
        this.surfaceHolder = this.surfaceView.getHolder();
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.paint.setTextSize(40.0F);
        this.paint.setStrokeWidth(2.0F);
        // localCanvas.drawColor(Color.rgb(238, 238, 238));
        //  localCanvas.drawText("形变量", 0, 3, 25.0F, 40.0F, this.paint);
        // localCanvas.drawLine(20.0F, this.surfaceView.getHeight() / 2, -30 + this.surfaceView.getWidth(), this.surfaceView.getHeight() / 2, this.paint);
        // localCanvas.drawLine(20.0F, -30 + this.surfaceView.getHeight(), 20.0F, 15.0F, this.paint);
        oldX+=20;
        currentX+=20;
        oldY= this.surfaceView.getHeight()-50-oldY;
        currentY= this.surfaceView.getHeight()-50-currentY;
        Canvas localCanvas = this.surfaceHolder.lockCanvas(new Rect(oldX-1,oldY+1,currentX+1,currentY-1));
        localCanvas.drawLine(oldX,oldY,currentX,currentY,paint);
        this.surfaceHolder.unlockCanvasAndPost(localCanvas);
    }


}
