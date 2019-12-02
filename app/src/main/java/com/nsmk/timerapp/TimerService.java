package com.nsmk.timerapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TimerService extends Service {
    long remainingMilliSecond;
    static long REMAINING_MILLI_SECONDS;
    static boolean IS_SERVICE_RUNNING=false;

    CountDownTimer timer;
    long countDownInterval=1000;
    private static final String TAG = "TimerService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IS_SERVICE_RUNNING=true;



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IS_SERVICE_RUNNING=true;
        Bundle bundle=intent.getExtras();
        remainingMilliSecond=bundle.getLong("REMAINING_MILLISECOND");
        //REMAINING_MILLI_SECONDS=remainingMilliSecond;
        //  remainingMilliSecond=100000;
        timer= new CountDownTimer(remainingMilliSecond,countDownInterval){

            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(getBaseContext(), "remaining seconds" + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                REMAINING_MILLI_SECONDS=millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Toast.makeText(getBaseContext(), "Finish.." , Toast.LENGTH_SHORT).show();
                timer.cancel();

            }
        };
        timer.start();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onStartCommand: " );
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
        IS_SERVICE_RUNNING = false;
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        timer.cancel();

    }

    @Override
    public boolean stopService(Intent name) {
        IS_SERVICE_RUNNING = false;
        Log.e(TAG, "stopService: ");
        timer.cancel();
        stopSelf();
        return super.stopService(name);
    }


}
