package com.example.feiya.test;


import android.content.Context;
import android.view.SurfaceView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by feiya on 2016/5/15.
 */
public class Process {

    private ArrayList GC_phase1=new ArrayList();
    private ArrayList GC_phase2=new ArrayList();


    private DrawThread drawThread;
    private ReceiveDataThread receiveDataThread;

    private SurfaceView surfaceView;

    private StrogeData strogeData;
    Context mContext;

    public Process(Context context,SurfaceView surfaceView){
        this.mContext=context;
        strogeData=new StrogeData();
        //ClearScreen(surfaceView);
    }

    public void start(SurfaceView surfaceView, Socket socket)throws IOException {
        receiveDataThread=new ReceiveDataThread(strogeData,socket);
        drawThread=new DrawThread(strogeData,surfaceView);

        new Thread(receiveDataThread).start();
        new Thread(drawThread).start();

    }

    public void stop(){
        if(receiveDataThread!=null&&drawThread!=null) {
            receiveDataThread.stop();
            drawThread.stop();
        }
    }
    public boolean startAM(){
        if(drawThread!=null){
            drawThread.startAm();
            return true;
        }else {
            return false;
        }
    }
    public boolean startPhase(){
        if(drawThread!=null){
            drawThread.startPhase();
            return true;
        }else {
            return false;
        }
    }
    public boolean startShift(){
        if(drawThread!=null){
            drawThread.startShift();
            return true;
        }else {
            return false;
        }
    }
    public boolean startSignal(){
        if(drawThread!=null){
            drawThread.startSignal();
            return true;
        }else {
            return false;
        }
    }
    public void setWitchshift(){
        if(drawThread.getShiftSwitch()){
            drawThread.setShiftSwitch(false);
        }
        else{
            drawThread.setShiftSwitch(true);
        }
    }



    ////从这开始删除的

}
