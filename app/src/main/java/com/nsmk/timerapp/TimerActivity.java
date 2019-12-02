package com.nsmk.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimerActivity extends AppCompatActivity {
    private static final String TAG = "TimerActivity";
    private int intentPassedHour, intentPassedMin, intentPassedSec;
    private long totalMillisSecond;
    private long timerRemainingMilliSecond;
    private int remainingHour, remainingMin, remainingSecond;
    private long countDownInterval = 1000;
    private CountDownTimer timer;
    private boolean timerIsRunning=false;

    @BindView(R.id.tvTimeCountdown)
    TextView tvShowRemainingTime;
    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.btnStop)
    Button btnStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);


        if (!TimerService.IS_SERVICE_RUNNING) {
            intentPassedHour = getIntent().getExtras().getInt("HOUR");
            intentPassedMin = getIntent().getExtras().getInt("MINUTE");
            intentPassedSec = getIntent().getExtras().getInt("SECOND");
            tvShowRemainingTime.setText(intentPassedHour + " : " + intentPassedMin + " : " + intentPassedSec);
            Toast.makeText(this, intentPassedHour + ":" + intentPassedMin + ":" + intentPassedSec, Toast.LENGTH_LONG).show();

            totalMillisSecond = (intentPassedHour * 3600 + intentPassedMin * 60 + intentPassedSec) * 1000;



        } else if (TimerService.IS_SERVICE_RUNNING) {

            Intent intent = new Intent(this, TimerService.class);
            stopService(intent);
            totalMillisSecond = TimerService.REMAINING_MILLI_SECONDS;
            setTimer(totalMillisSecond);
            timer.start();

        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timerIsRunning){
                    timerIsRunning=true;
                    setTimer(totalMillisSecond);
                    timer.start();
                }

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerIsRunning=false;

                Log.e(TAG, "onClick: "+ (TimerService.IS_SERVICE_RUNNING));

                tvShowRemainingTime.setText("Time Up....");
                timer.cancel();

            }
        });


    }

    public void setTimer(long milliSecond) {
        timerIsRunning=true;
        timer = new CountDownTimer(milliSecond, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerRemainingMilliSecond = millisUntilFinished;
                remainingSecond = (int) (millisUntilFinished / 1000);
                remainingHour = (remainingSecond / 3600);
                remainingMin = (remainingSecond % 3600) / 60;
                remainingSecond = (remainingSecond % 3600) % 60;
                Toast.makeText(getBaseContext(), remainingHour + " : " + remainingMin + " : " + remainingSecond, Toast.LENGTH_SHORT).show();

                tvShowRemainingTime.setText(remainingHour + " : " + remainingMin + " : " + remainingSecond);
            }

            @Override
            public void onFinish() {
                timerIsRunning=false;
                tvShowRemainingTime.setText("Time Up....");
                timer.cancel();

            }
        };
    }

    public void makeServiceStop(){
        stopService(new Intent(this, TimerService.class));
        totalMillisSecond = TimerService.REMAINING_MILLI_SECONDS;
        setTimer(totalMillisSecond);
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " );
        timer.cancel();
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("REMAINING_MILLISECOND", timerRemainingMilliSecond);
        startService(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: "+TimerService.IS_SERVICE_RUNNING);
        if (TimerService.IS_SERVICE_RUNNING) {
            makeServiceStop();
        }
    }

}
