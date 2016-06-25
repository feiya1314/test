package com.example.feiya.test;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * 连接服务端
 * Created by feiya on 2016/6/25.
 */
public class ConnectThread implements Runnable{
    private Socket socket;
    private Context context;
    private Handler handler;
    private int port = 30000;
    private int timeout = 5000;

    Message message=new Message();

    public ConnectThread(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.setSendBufferSize(1024 * 16);
            socket.setReceiveBufferSize(1024 * 16);
            socket.connect(new InetSocketAddress(WiFiStatus.getHost(context), port), timeout);
           // IsConnceted = true;
            //invalidateOptionsMenu();
            message.what=0x00;
            handler.sendMessage(message);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            message.what=0x11;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            message.what=0x22;
            handler.sendMessage(message);
        }
    }
}
