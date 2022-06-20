package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityHistoryBinding;
import com.mdq.rv_adapter.History_adapter;

public class History extends AppCompatActivity {

    ActivityHistoryBinding activityHistoryBinding;

    History_adapter history_adapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        activityHistoryBinding=ActivityHistoryBinding.inflate(getLayoutInflater());
        linearLayoutManager=new LinearLayoutManager(this);

        history_adapter=new History_adapter();
        activityHistoryBinding.rvHistory.setLayoutManager(linearLayoutManager);
        activityHistoryBinding.rvHistory.setAdapter(history_adapter);

    }
}