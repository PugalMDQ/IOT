package com.mdq.v_safe.UI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdq.v_safe.R;

public class LeDeviceListAdapter extends RecyclerView.Adapter<LeDeviceListAdapter.mine> {

    String name;
    LeDeviceListAdapter(String name){

        this.name=name;

    }

    @NonNull
    @Override
    public mine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull mine holder, int position) {

        holder.textView.setText(name);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class mine extends RecyclerView.ViewHolder {
        TextView textView;
        public mine(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.name);
        }
    }
}
