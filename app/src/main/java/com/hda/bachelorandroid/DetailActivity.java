package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import com.hda.bachelorandroid.services.activity.ApiUserRestClient;
import com.hda.bachelorandroid.services.network.RetrofitEventListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;

public class DetailActivity extends AppCompatActivity {
    private EditText detailTitle;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;
    private Button resumeButton;
    private Button saveButton;
    private Chronometer chronometer;
    private boolean isRunning = false;
    private boolean hasBeenPaused = false;
    private long pastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("detailName");
        detailTitle = (EditText) findViewById(R.id.DetailTitle);
        startButton = (Button) findViewById(R.id.button_start);
        stopButton = (Button) findViewById(R.id.button_stop);
        resetButton = (Button) findViewById(R.id.button_reset);
        resumeButton = (Button) findViewById(R.id.button_resume);
        saveButton = (Button) findViewById(R.id.save_button);

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
                startChronometer(v, intent.getLongExtra("detailDuration", 0));
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    long timeMeasured = SystemClock.elapsedRealtime() - chronometer.getBase();
                    updateActivityCall(intent.getStringExtra("detailId"), detailTitle.getText().toString(), String.valueOf(timeMeasured), true);
                } else {
                    long timeMeasured = pastTime;
                    updateActivityCall(intent.getStringExtra("detailId"), detailTitle.getText().toString(), String.valueOf(timeMeasured), true);
                }

                Intent intent = new Intent(v.getContext(), RoomSrollViewActivity.class);
                startActivity(intent);

            }
        });


        chronometer.setBase(SystemClock.elapsedRealtime() - intent.getLongExtra("detailDuration", 0));

        };



    public void resetChronometer(View view) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        pastTime = 0;
    }

    public void resumeChronometer(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime() - pastTime);
        chronometer.start();
        isRunning = true;
    }


    public void stopChronometer(View view) {
        if(isRunning){
            pastTime = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
        }
        isRunning = false;
        hasBeenPaused = true;
    }


    public void startChronometer(View view, long elapsedTime){
        if(hasBeenPaused){
            resumeChronometer(view);
        } else {
            chronometer.setBase(SystemClock.elapsedRealtime() - elapsedTime);
            chronometer.start();
        }
        isRunning = true;

    }


    public void updateActivityCall(String activityId, String name, String duration, Boolean finished) {
        ApiUserRestClient.Companion.getInstance().updateActivity((RetrofitEventListener)(new RetrofitEventListener() {
            @Override
            public void onSuccess(@NotNull Call call, @NotNull Object response) {
                System.out.println("RESPONSEEEEEEEEEEEEEEE " + response);

            }
            @Override
            public void onError(@NotNull Call call, @NotNull Throwable t) {
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRR"  + t);
            }
        }),activityId, name, duration, finished);
    }
}