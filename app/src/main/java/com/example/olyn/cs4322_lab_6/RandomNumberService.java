package com.example.olyn.cs4322_lab_6;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by Olyn on 9/30/2017.
 */

public class RandomNumberService extends Service {
    Random r = new Random();
    private int randomNum;
    private boolean isGenerating;

    class RandomNumServBinder extends Binder {
        public RandomNumberService getService() {
            return RandomNumberService.this;
        }
    }

    private IBinder binder = new RandomNumServBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Start", "Starting service on thread number: " + Thread.currentThread().getId());
        isGenerating = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startNumberGen();
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Random Number", "Service Destroy.");

        stopRandomGen();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Bind", "Binding Service");
        return binder;
    }

    private void startNumberGen(){
        while(isGenerating){
            try{
                Thread.sleep(1000);
                if(isGenerating){
                    randomNum = new Random().nextInt(100);
                    Log.i("Random Number", "In thread " + Thread.currentThread().getId() + " Random Number is " + randomNum);
                }
            }
            catch(InterruptedException e){
                Log.i("Random Number", "Thread Interrupt");
            }
        }
    }

    public int getRandomNum(){
        return randomNum;
    }

    private void stopRandomGen(){
        isGenerating = false;
    }

}
