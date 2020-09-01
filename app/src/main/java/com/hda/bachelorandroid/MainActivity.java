package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
//    private final static String default_notification_channel_id = "default" ;
    Button buttonToRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonToRooms = (Button) findViewById(R.id.button1);

        buttonToRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoomViewActivity(v);
            }
        });

//        handler.post(runnableCode);
    }

    /** Called when the user taps the Send button */
    public void startRoomViewActivity(View view) {
        Intent intent = new Intent(this, RoomSrollViewActivity.class);
        startActivity(intent);
    }


}