package com.example.feiya.test;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * function 作用作为数据的存储仓，实现读者写者模式
 * Created by feiya on 2016/6/9.
 */
public class StrogeData {
    private ArrayList<double[]> RecDatas = new ArrayList<double[]>();
    private ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();  //读写锁，写数据的的线写时，读线程需要等写完
    private double[] temp=new double[1024];

    /**
     *
     * @param recdata  接收的原始数据
     * @function 作用 写数据
     */
    public  void writedata(double[] recdata){
        readWriteLock.writeLock().lock();   //给数据加锁，此时只能写数据，不能读数据
        try{
            RecDatas.add(recdata);
        }catch (Exception e){
            Log.e("write","fail");
        }
        finally{
            readWriteLock.writeLock().unlock();// ！！！一定要释放锁，不管读数据是否成功
        }

    }

    /**
     * 读取接收的数据
     * @param position 去读的数据位置
     * @return
     */
    public  double[] readdata(int position){
        readWriteLock.readLock().lock();         //加读锁，此时不能写数据
        try{
            System.arraycopy(RecDatas.get(position),0,temp,0,1024);
        }catch (Exception e){
            Log.e("read","fail");
        }finally {
            readWriteLock.readLock().unlock();
        }
        return temp;
    }

    /**
     * 判读当前读取数据的位置是否超出了已经接收的数据
     * @param position
     * @return
     */
    public boolean IsOverRecDatas(int position){
        readWriteLock.readLock().lock();
        try{
            return (position<RecDatas.size());
        }finally {
            readWriteLock.readLock().unlock();
        }
    }


}
