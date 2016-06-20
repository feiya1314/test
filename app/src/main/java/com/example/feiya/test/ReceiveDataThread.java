package com.example.feiya.test;

import android.os.StrictMode;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by feiya on 2016/6/9.
 */
public class ReceiveDataThread implements Runnable {
    private StrogeData strogeData;
    private Socket socket;
    private InputStream inputStream=null;
    private byte[] buffer=new byte[8];
    private boolean IsReceiving=true;
    private double[] data=new double[1024];

    public ReceiveDataThread(StrogeData strogeData,Socket socket)throws IOException{
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        this.strogeData=strogeData;
        this.socket=socket;
        inputStream=socket.getInputStream();
        Logger.init();
    }

    @Override
    public void run() {
        try{

            int i = 0;    //接收的数据存放在data【】中
            int count=8;
            int readcount=0;


            while(IsReceiving){

                while(readcount<count){
                    readcount+=inputStream.read(buffer,readcount,count-readcount);   //确保每次读出8个字节
                }
                data[i]=byteTodouble(buffer);
                readcount=0;
                Log.i("data",String.valueOf(i)+" "+String.valueOf(data[i]));
                i++;
               /* while(offset<8192&&(numReadBytes=inputStream.read(buffer))>0){
                    data[i]=byteTodouble(buffer);
                    Log.e("data",String.valueOf(data[i]));
                    Log.e("data",String.valueOf(i));
                    System.out.println(data[i]);

                    i++;
                    if(i==1024){
                        i=0;
                        break;
                    }
                    offset+=numReadBytes;
                }*/
                if(i==1024){
                    Log.i("data","saved");
                    strogeData.writedata(data);   //如果i==1024说明data满了，将数据写入strogedata
                    i=0;
                }
            }
        }catch(IOException e){
            try{socket.close();}catch (IOException el){el.printStackTrace();};
            e.printStackTrace();
        }
    }
    public void stop(){
        IsReceiving=false;
    }
    private double byteTodouble(byte[] recData){
        double trf;
        trf=ByteUtil.getDouble(recData);
        return trf;
    }

}
