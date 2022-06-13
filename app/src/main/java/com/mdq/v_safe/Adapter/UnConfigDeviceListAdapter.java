package com.mdq.v_safe.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdq.v_safe.Interface.ConnectPosition;
import com.mdq.v_safe.Interface.DeviceListInterface;
import com.mdq.v_safe.R;
import com.mdq.v_safe.Utils.PreferenceManager;
import com.mdq.v_safe.pojo.DeviceListObject;

import java.util.List;

public class UnConfigDeviceListAdapter extends RecyclerView.Adapter<UnConfigDeviceListAdapter.ViewHolder> implements ConnectPosition {

    private Context context;
    private List<DeviceListObject> deviceListObjects;
    private DeviceListInterface deviceListInterface;
    List<String> dItems;
    private int selected_position = -1;
    private int defaultPosition;
    private PreferenceManager preferenceManager;
    private ConnectPosition connectPosition;
    private boolean isSelected = false;

    public UnConfigDeviceListAdapter(Context context, List<String> listdata, DeviceListInterface deviceListInterface, ConnectPosition connectPosition, int defaultPosition) {
        this.context = context;
        dItems = listdata;
        this.deviceListInterface = deviceListInterface;
        this.connectPosition = connectPosition;
        this.defaultPosition = defaultPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_name_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.rlDeviceName.setOnClickListener(view -> {
            deviceListInterface.goToConfigure(String.valueOf(position));

            getPreferenceManager().setBLEPosition(String.valueOf(position));
            notifyDataSetChanged();
        });

        selected_position = defaultPosition;
        if (selected_position == position) {
            isSelected = true;
            holder.rlDeviceName.setBackgroundColor(Color.parseColor("#ececec"));
            holder.rlDeviceName.setEnabled(false);
            holder.rlDeviceName.setClickable(false);
            holder.tvDeviceName.setTypeface(null, Typeface.BOLD);
        } else if (selected_position == -1) {
            if (!isSelected) {
                holder.rlDeviceName.setEnabled(true);
                holder.rlDeviceName.setClickable(true);
            }
        } else {
            if (!isSelected) {
                holder.rlDeviceName.setEnabled(true);
                holder.rlDeviceName.setClickable(true);
            }
        }

        holder.tvDeviceName.setText(dItems.get(position));


    }

    @Override
    public int getItemCount() {
        return dItems.size();
    }

    @Override
    public void getPosition(String position) {
        Log.e("connectPosition", position);
        Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlDeviceName;
        TextView tvDeviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlDeviceName = itemView.findViewById(R.id.rlDeviceName);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
        }
    }

    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) preferenceManager = new PreferenceManager(context);
        return preferenceManager;
    }

}
