package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityNotificationactivityBinding;
import com.mdq.rv_adapter.Notification_adapter;

public class Notificationactivity extends AppCompatActivity {

    ActivityNotificationactivityBinding activityNotificationactivityBinding;
    LinearLayoutManager linearLayoutManager;
    Notification_adapter notification_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotificationactivityBinding=ActivityNotificationactivityBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationactivityBinding.getRoot());
        linearLayoutManager=new LinearLayoutManager(this);
        notification_adapter=new Notification_adapter();

        activityNotificationactivityBinding.rvNotification.setLayoutManager(linearLayoutManager);
        activityNotificationactivityBinding.rvNotification.setAdapter(notification_adapter);

    }
}