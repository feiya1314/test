package com.example.feiya.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
/**
 * Created by feiya on 2016/5/14.
 */
public class WiFiStatus {
    public WiFiStatus(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    }

    public static String getHost(Context context){
        String host=null;
        int serviceAddress;
        WifiManager wifiManager=((WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        DhcpInfo dhcpInfo=wifiManager.getDhcpInfo();
        serviceAddress=dhcpInfo.serverAddress;
        host=(serviceAddress&0xFF)+"."+(0xFF&serviceAddress >> 8)+"."+(0xFF&serviceAddress >> 16)+"."+(0xFF&serviceAddress >> 24);
        return host;
    }
    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false ;
    }
}
