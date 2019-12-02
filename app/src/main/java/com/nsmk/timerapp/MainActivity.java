package com.nsmk.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnSet)
    Button btnSet;

    @BindView(R.id.hourPicker)
    NumberPicker hourPicker;

    @BindView(R.id.minutePicker)
    NumberPicker minutePicker;

    @BindView(R.id.secondPicker)
    NumberPicker secondPicker;

    private int hour,min,sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(60);

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
                Intent intent=new Intent(MainActivity.this,TimerActivity.class);
                intent.putExtra("HOUR",hour);
                intent.putExtra("MINUTE",min);
                intent.putExtra("SECOND",sec);
                startActivity(intent);
            }
        });

    }


}
