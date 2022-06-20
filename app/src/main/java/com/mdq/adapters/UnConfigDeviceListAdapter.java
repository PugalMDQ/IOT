package com.mdq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.mdq.interfaces.DeviceListInterface;
import com.mdq.marinetechapp.R;
import com.mdq.interfaces.ConnectPosition;
import com.mdq.pojo.DeviceListObject;
import com.mdq.utils.PreferenceManagerMarine;
import java.util.List;

public class UnConfigDeviceListAdapter extends RecyclerView.Adapter<UnConfigDeviceListAdapter.ViewHolder> implements ConnectPosition {

    private Context context;
    private List<DeviceListObject> deviceListObjects;
    private DeviceListInterface deviceListInterface;
    List<String> dItems;
    private int selected_position = -1;
    private int defaultPosition;
    private PreferenceManagerMarine preferenceManager;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.rlDeviceName.setOnClickListener(view -> {
            holder.check.setBackground(context.getResources().getDrawable(R.drawable.right));
            deviceListInterface.goToConfigure(String.valueOf(position));
            getPreferenceManager().setBLEPosition(String.valueOf(position));
            notifyDataSetChanged();
        });


        selected_position = defaultPosition;
        if (selected_position == position) {

            isSelected = true;
            holder.rlDeviceName.setEnabled(false);
            holder.rlDeviceName.setClickable(false);

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
        String num =String.valueOf(position+1);
        holder.num.setText(num);


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

        ConstraintLayout rlDeviceName;
        TextView tvDeviceName;
        TextView num;
        TextView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlDeviceName = itemView.findViewById(R.id.rlDeviceName);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            num = itemView.findViewById(R.id.num);
            check = itemView.findViewById(R.id.check);
        }

    }

    public PreferenceManagerMarine getPreferenceManager() {
        if (preferenceManager == null) preferenceManager = new PreferenceManagerMarine(context);
        return preferenceManager;
    }

}
