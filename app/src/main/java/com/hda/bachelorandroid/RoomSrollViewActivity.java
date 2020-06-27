package com.hda.bachelorandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hda.bachelorandroid.services.activity.model.BaseActivity;
import com.hda.bachelorandroid.services.model.Post;
import com.hda.bachelorandroid.services.network.RetrofitEventListener;
import com.hda.bachelorandroid.services.activity.ApiUserRestClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;

public class RoomSrollViewActivity extends AppCompatActivity {
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
                System.out.println("RESPONSEEEEEEEEEEEEEEE " + response);

                List var10002 = ((List<Activity>)response);
                System.out.println(var10002);
                addItemsAfterFetch(var10002);
            }

            public void onError(@NotNull Call call, @NotNull Throwable t) {
                Intrinsics.checkParameterIsNotNull(call, "call");
                Intrinsics.checkParameterIsNotNull(t, "t");
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRR"  + t);
            }
        }));
    }

    public void createActivityCall(String name, String duration, final View v) {
    ApiUserRestClient.Companion.getInstance().savePostActivity((RetrofitEventListener)(new RetrofitEventListener() {
        @Override
        public void onSuccess(@NotNull Call call, @NotNull Object response) {
            System.out.println("RESPONSEEEEEEEEEEEEEEE " + response);
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
//          data.add(new Activity(itemText, "14:11:32"));
//          mAdapter.notifyItemInserted(data.size() - 1);
//          recyclerView.scrollToPosition(data.size() - 1);
            createActivityCall(itemText, "0", v);

//            Intent intent = new Intent(v.getContext(), DetailActivity.class);
//            intent.putExtra("detailName", createdActivity.getName());
//            intent.putExtra("detailDuration", createdActivity.getDuration());
//            intent.putExtra("detailId", createdActivity.get_id());
//            v.getContext().startActivity(intent);



//            SystemClock.sleep(3000);
//            callActivityList();
//            textInput.setText("");
            closeKeyBoard();
        }
    }
}