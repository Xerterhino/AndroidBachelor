package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
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
    }

    /** Called when the user taps the Send button */
    public void startRoomViewActivity(View view) {
        Intent intent = new Intent(this, RoomSrollViewActivity.class);
        startActivity(intent);
    }

}