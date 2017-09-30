/*
  Class: 	 	          CS 4322/section01
  Term:		              Fall 2017
  Name: 		          Olyn Dabbs
  Instructor:             Dr. Selena He
  Lab 6:                  Random Number Service
 */

package com.example.olyn.cs4322_lab_6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RandomNumberActivity extends AppCompatActivity {

    Button start, stop, bind, unbind, ranNumBtn;
    TextView tv;

    Intent serviceIntent;
    RandomNumberService ranNumServ;
    boolean isBound;
    ServiceConnection servConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number);

        start = (Button)findViewById(R.id.startButton);
        stop = (Button)findViewById(R.id.stopButton);
        bind = (Button)findViewById(R.id.bindButton);
        unbind = (Button)findViewById(R.id.unbindButton);
        ranNumBtn = (Button)findViewById(R.id.ranNumButton);
        tv = (TextView)findViewById(R.id.textView);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.startButton:
                serviceIntent = new Intent(getApplicationContext(), RandomNumberService.class);
                startService(serviceIntent);
                break;
            case R.id.bindButton:
                bindRandomGen();
                break;
            case R.id.stopButton:
                stopService(serviceIntent);
                break;
            case R.id.unbindButton:
                unbindRandomGen();
                break;
            case R.id.ranNumButton:
                getRandomNum();
        }
    }

    private void bindRandomGen(){
        if(servConnection == null){
            servConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    RandomNumberService.RandomNumServBinder servBinder = (RandomNumberService.RandomNumServBinder)iBinder;
                    ranNumServ = servBinder.getService();
                    isBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isBound = false;
                }
            };
        }
        bindService(serviceIntent, servConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindRandomGen(){
        if(isBound){
            unbindService(servConnection);
            isBound = false;
        }
    }

    private void getRandomNum(){
        if(isBound){
            tv.setText("Random Number: " + ranNumServ.getRandomNum());
        }
        else{
            tv.setText("Service not bound.");
        }
    }
}
