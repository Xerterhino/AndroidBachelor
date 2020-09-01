package com.hda.bachelorandroid;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hda.bachelorandroid.services.activity.ApiUserRestClient;
import com.hda.bachelorandroid.services.network.RetrofitEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import retrofit2.Call;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Activity> mDataset;

    public void addItem(Activity item) {
        mDataset.add(item);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Chronometer durationChronometer;
        public ImageButton messageButton;
        public LinearLayout itemCardInnerLayout;

        public MyViewHolder (View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.activity_name);
            durationChronometer = (Chronometer) itemView.findViewById(R.id.chronometer);
            messageButton = (ImageButton) itemView.findViewById(R.id.message_button);
            itemCardInnerLayout = (LinearLayout) itemView.findViewById(R.id.itemCardInnerLayout);
        }
    }


    public RecyclerViewAdapter(ArrayList<Activity> myDataset) {
        mDataset = myDataset;
    }

    public void deleteActivityCall(String activityId) {
        ApiUserRestClient.Companion.getInstance().deleteActivity((RetrofitEventListener)(new RetrofitEventListener() {
            @Override
            public void onSuccess(@NotNull Call call, @NotNull Object response) {
                System.out.println("RESPONSEEEEEEEEEEEEEEE " + response);
                com.hda.bachelorandroid.services.activity.model.Activity ac = (com.hda.bachelorandroid.services.activity.model.Activity) response;
                System.out.println("testActivity " + ac.getName());
                mDataset.removeIf(n -> (n.get_id().equals(ac.getId())));
                notifyDataSetChanged();

            }
            @Override
            public void onError(@NotNull Call call, @NotNull Throwable t) {
                System.out.println("ERRORRRRRRRRRRRRRRRRRRRRR"  + t);
            }
        }),activityId);
    }



    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_recycler, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nameTextView.setText(mDataset.get(position).getName());
        holder.durationChronometer.setText(mDataset.get(position).getDuration());
        holder.durationChronometer.setBase(SystemClock.elapsedRealtime() - Integer.parseInt(mDataset.get(position).getDuration()) );


        holder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delelteActivity(position);

            }
        });

        holder.itemCardInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailViewActivity(v, position,  Long.parseLong(mDataset.get(position).getDuration()));
            }
        });



    }

    public void startDetailViewActivity(View view, int index, long currentDuration) {

        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detailName", mDataset.get(index).getName());
        intent.putExtra("detailDuration", currentDuration);
        intent.putExtra("detailId", mDataset.get(index).get_id());
        view.getContext().startActivity(intent);

    }



    public void delelteActivity(int index) {
        deleteActivityCall(mDataset.get(index).get_id());
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}