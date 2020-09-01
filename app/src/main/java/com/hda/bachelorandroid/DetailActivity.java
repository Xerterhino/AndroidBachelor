package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hda.bachelorandroid.services.activity.ApiUserRestClient;
import com.hda.bachelorandroid.services.network.RetrofitEventListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;

public class DetailActivity extends AppCompatActivity {
    private EditText detailTitle;
    private Button startButton;
    private Button resetButton;
    private Button saveButton;
    private Button stopButton;
    private TextView TimerText;
    private boolean isRunning = false;


    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;

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
        saveButton = (Button) findViewById(R.id.save_button);

        detailTitle.setText(name);
        startButton.setText("Start");
        handler = new Handler() ;
        TimerText = findViewById(R.id.textViewTime);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                TimerText.setText("00:00:00");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    long timeMeasured = MilliSeconds;
                    updateActivityCall(intent.getStringExtra("detailId"), detailTitle.getText().toString(), String.valueOf(timeMeasured), true);
                } else {
                    long timeMeasured = MilliSeconds;
                    updateActivityCall(intent.getStringExtra("detailId"), detailTitle.getText().toString(), String.valueOf(timeMeasured), true);
                }

                Intent intent = new Intent(v.getContext(), RoomSrollViewActivity.class);
                startActivity(intent);

            }
        });



        };

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            String s = String.valueOf(MilliSeconds);
//
//            s = s.substring(0, s.length() -1);
            if (s.length() > 1){
                s = s.substring(0, s.length() -1);
            }

            TimerText.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + s);

            handler.postDelayed(this, 0);
        }

    };




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