package com.mdq.rv_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdq.marinetechapp.R;

public class Notification_adapter extends RecyclerView.Adapter<Notification_adapter.mine> {
    @NonNull
    @Override
    public mine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_notification,parent,false);
        return new mine(view);

    }

    @Override
    public void onBindViewHolder(@NonNull mine holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class mine extends RecyclerView.ViewHolder {
        public mine(@NonNull View itemView) {
            super(itemView);


        }
    }
}
