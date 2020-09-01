package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.hda.bachelorandroid.services.network.RetrofitEventListener;
import com.hda.bachelorandroid.services.activity.ApiUserRestClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;

@SuppressWarnings("ALL")
public class RoomSrollViewActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Button buttonToRooms;

    Handler handler = new Handler();

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            scheduleNotification(getNotification( "Log something today!" ) , 0 );
            handler.postDelayed(this, 300000);
        }
    };


    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, NotificationPublisher. class ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent,
                PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, futureInMillis, 60000, pendingIntent );
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Check your activities!" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private RecyclerView recyclerView;
    private TextInputEditText textInput;
    private Button addButton;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private Activity createdActivity;


    private ArrayList<Activity> data = new ArrayList<Activity>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_sroll_view);
        recyclerView = (RecyclerView) findViewById(R.id.RoomRecyclerView);
        addButton = (Button) findViewById(R.id.add_activity_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItem(v);
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter(data);
        recyclerView.setAdapter(mAdapter);

        handler.post(runnableCode);

    }

    @Override
    public void onResume() {
        TextInputEditText textInput = (TextInputEditText) findViewById(R.id.add_input);
        textInput.setText("");
        callActivityList();
        super.onResume();

    }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public final void callActivityList() {
        ApiUserRestClient.Companion.getInstance().getUserList((RetrofitEventListener)(new RetrofitEventListener() {
            @Override
            public void onSuccess(@NotNull Call call, @NotNull Object response) {

                List var10002 = ((List<Activity>)response);
                addItemsAfterFetch(var10002);
            }

            public void onError(@NotNull Call call, @NotNull Throwable t) {
                Intrinsics.checkParameterIsNotNull(call, "call");
                Intrinsics.checkParameterIsNotNull(t, "t");
            }
        }));
    }

    public void createActivityCall(String name, String duration, final View v) {
    ApiUserRestClient.Companion.getInstance().savePostActivity((RetrofitEventListener)(new RetrofitEventListener() {
        @Override
        public void onSuccess(@NotNull Call call, @NotNull Object response) {
            com.hda.bachelorandroid.services.activity.model.Activity ac = (com.hda.bachelorandroid.services.activity.model.Activity) response;

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("detailName", ac.getName());
            intent.putExtra("detailDuration", ac.getDuration());
            intent.putExtra("detailId", ac.getId());
            v.getContext().startActivity(intent);

        }
        @Override
        public void onError(@NotNull Call call, @NotNull Throwable t) {
            System.out.println("ERRORRRRRRRRRRRRRRRRRRRRR"  + t);
        }
    }),duration, name);
}



    public void addItemsAfterFetch(List<com.hda.bachelorandroid.services.activity.model.Activity> items){

        data.clear();
        mAdapter.notifyDataSetChanged();

        for (com.hda.bachelorandroid.services.activity.model.Activity item : items){
            data.add(new Activity(item.getName(), item.getDuration().toString(), item.getId()));
            mAdapter.notifyItemInserted(data.size() - 1 > -1 ? data.size() - 1 : 0 );
        }
        recyclerView.scrollToPosition(data.size() - 1);


    }

    public void onAddItem(View v) {

        TextInputEditText textInput = (TextInputEditText) findViewById(R.id.add_input);
        String itemText = Objects.requireNonNull(textInput.getText()).toString();

        if(itemText.length() > 0){
            createActivityCall(itemText, "0", v);
            closeKeyBoard();
        }
    }

//
//    private void scheduleNotification (Notification notification , int delay) {
//        Intent notificationIntent = new Intent( this, NotificationPublisher. class ) ;
//        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
//        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
//        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
//        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
//        assert alarmManager != null;
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, futureInMillis, 60000, pendingIntent );
//    }
//    private Notification getNotification (String content) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
//        builder.setContentTitle( "Check your activities!" ) ;
//        builder.setContentText(content) ;
//        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
//        builder.setAutoCancel( true ) ;
//        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//        return builder.build() ;
//    }
}