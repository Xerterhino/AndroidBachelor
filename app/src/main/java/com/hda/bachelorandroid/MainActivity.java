package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Button buttonToRooms;

    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            scheduleNotification(getNotification( "15 second delay" ) , 0 );

            handler.postDelayed(this, 15000);
        }
    };

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

        handler.post(runnableCode);
    }

    /** Called when the user taps the Send button */
    public void startRoomViewActivity(View view) {
        Intent intent = new Intent(this, RoomSrollViewActivity.class);
        startActivity(intent);
    }


    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, NotificationPublisher. class ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }


}