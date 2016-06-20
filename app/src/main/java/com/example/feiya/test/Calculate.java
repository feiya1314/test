package com.example.feiya.test;

import com.example.feiya.fft.Complex1D;

/**
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
}
