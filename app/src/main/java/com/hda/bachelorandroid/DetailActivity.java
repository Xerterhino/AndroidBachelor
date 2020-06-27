package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView detailTitle;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button resumeButton;
    private Chronometer chronometer;
    private boolean isRunning = false;
    private boolean hasBeenPaused = false;
    private long lastTimeStopped = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("detailName");
        detailTitle = (TextView) findViewById(R.id.DetailTitle);
        startButton = (Button) findViewById(R.id.button_start);
        stopButton = (Button) findViewById(R.id.button_stop);
        resetButton = (Button) findViewById(R.id.button_reset);
        resumeButton = (Button) findViewById(R.id.button_resume);

        detailTitle.setText(name);

        chronometer = findViewById(R.id.chronometer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                chronometer = chronometerChanged;
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);
            }
        });
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeChronometer(v);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChronometer(v);
            }
        });


        chronometer.setBase(SystemClock.elapsedRealtime() - intent.getLongExtra("detailDuration", 0));

        };



    public void resetChronometer(View view) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        lastTimeStopped = 0;
    }

    public void resumeChronometer(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime() - lastTimeStopped);
        chronometer.start();
        isRunning = true;
    }


    public void stopChronometer(View view) {
        if(isRunning){
            lastTimeStopped = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
        }
        isRunning = false;
        hasBeenPaused = true;
    }


    public void startChronometer(View view){
        if(hasBeenPaused){
            resumeChronometer(view);
        } else {
            chronometer.start();
        }
        isRunning = true;

    }
}