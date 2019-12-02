package com.nsmk.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TimerActivity extends AppCompatActivity {
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
    @BindView(R.id.btnSet)
    Button btnSet;

    @BindView(R.id.btnReset)
    Button btnReset;

    @BindView(R.id.hourPicker)
    NumberPicker hourPicker;

    @BindView(R.id.minutePicker)
    NumberPicker minutePicker;

    @BindView(R.id.secondPicker)
    NumberPicker secondPicker;


    @BindView(R.id.timePickerLayout)
    LinearLayout timePickerLayout;

    @BindView(R.id.countdownLayout)
    LinearLayout countdownLayout;

    private int hour,min,sec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(60);

        if (!TimerService.IS_SERVICE_RUNNING) {
            timePickerLayout.setVisibility(View.VISIBLE);
            countdownLayout.setVisibility(View.GONE);


            hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    hour=hourPicker.getValue();
                }
            });

            minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    min=minutePicker.getValue();
                }
            });

            secondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    sec=secondPicker.getValue();

                }
            });

            btnSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countdownLayout.setVisibility(View.VISIBLE);
                    timePickerLayout.setVisibility(View.GONE);
                    tvShowRemainingTime.setText(hour + " : " + min + " : " + sec);
                    totalMillisSecond = (hour * 3600 + min * 60 + sec) * 1000;
                }
            });





        } else  {

            countdownLayout.setVisibility(View.VISIBLE);
            timePickerLayout.setVisibility(View.GONE);
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
                if(timerIsRunning) {

                    timerIsRunning = false;
                    tvShowRemainingTime.setText(R.string.TimerOnFinish);
                    timer.cancel();
                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerLayout.setVisibility(View.VISIBLE);
                countdownLayout.setVisibility(View.GONE);
                if(timerIsRunning) {
                    timer.cancel();
                    timerIsRunning = false;
                }

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
                remainingHour = remainingSecond / 3600;
                remainingMin = (remainingSecond % 3600) / 60;
                remainingSecond = (remainingSecond % 3600) % 60;
                Toast.makeText(getBaseContext(), remainingHour + " : " + remainingMin + " : " + remainingSecond, Toast.LENGTH_SHORT).show();

                tvShowRemainingTime.setText(remainingHour + " : " + remainingMin + " : " + remainingSecond);
            }

            @Override
            public void onFinish() {
                timerIsRunning=false;
                tvShowRemainingTime.setText(R.string.TimerOnFinish);
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

    public void stopTimerAndStartService(){
        timer.cancel();
        timerIsRunning=false;
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("REMAINING_MILLISECOND", timerRemainingMilliSecond);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timerIsRunning) {
            stopTimerAndStartService();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TimerService.IS_SERVICE_RUNNING) {
            makeServiceStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(TimerService.IS_SERVICE_RUNNING){
            stopService(new Intent(TimerActivity.this, TimerService.class));
        }

    }

}
