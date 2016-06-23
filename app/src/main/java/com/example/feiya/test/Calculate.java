package com.example.feiya.test;

import android.util.Log;

import com.example.feiya.fft.Complex1D;

/**
 *
 * Created by feiya on 2016/5/15.
 */
public class Calculate {
    public static int[] getAm(Complex1D complex1D){
        int length=complex1D.x.length;
        int[] Am=new int[length];
        for(int i=0;i<length;i++){
            Am[i]=(int)Math.round(Math.sqrt(complex1D.x[i]*complex1D.x[i]+complex1D.y[i]*complex1D.y[i]));
        }
        return Am;
    }

    public static int[] getIntPhase(Complex1D complex1D){
        int length=complex1D.x.length;
        double rad=Math.PI/180;
        int[] Phase=new int[length];
        for(int i=0;i<length;i++){
            Phase[i]=(int)Math.round(Math.atan2(complex1D.y[i],complex1D.x[i])/rad);
            // Log.e("phase_i",String.valueOf(Phase[i]));
        }
        return Phase;
    }
    public static double[] getDoublePhase(Complex1D complex1D){
        int length=complex1D.x.length;
        double rad=Math.PI/180;
        double[] Phase=new double[length];
        for(int i=0;i<length;i++){
            Phase[i]=Math.atan2(complex1D.y[i],complex1D.x[i])/rad;
        }
        return Phase;
    }

    public static double[] getdoubleShift(Complex1D complex1D){
        double rad=Math.PI/180;
        double pi=Math.PI;
        double c=299792458;
        double fs=1000000;
        double tempConst=c/(2*pi*fs);

        double up_80;
        double up_90;
        double up_100;
        double low_90;
        double low_80;

        double[] shift=new double[2];

        up_80=Math.atan2(complex1D.y[80],complex1D.x[80])/rad;
        up_90=Math.atan2(complex1D.y[90],complex1D.x[90])/rad;
        up_100=Math.atan2(complex1D.y[100],complex1D.x[100])/rad;
        low_90=Math.atan2(complex1D.y[110],complex1D.x[110])/rad;
        low_80=Math.atan2(complex1D.y[120],complex1D.x[120])/rad;

        shift[0]=((up_80+low_80)/2-up_100)*tempConst;
        shift[1]=((up_90+low_90)/2-up_100)*tempConst;

        return shift;
    }


}
