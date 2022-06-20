package com.mdq.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.FragmentLogBinding;
import com.mdq.rv_adapter.History_adapter;

public class log extends Fragment {

    FragmentLogBinding fragmentLogBinding;
    History_adapter history_adapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentLogBinding=FragmentLogBinding.inflate(inflater, container, false);

        linearLayoutManager=new LinearLayoutManager(requireContext());

        history_adapter=new History_adapter();
        fragmentLogBinding.rvHistory.setLayoutManager(linearLayoutManager);
        fragmentLogBinding.rvHistory.setAdapter(history_adapter);



        return fragmentLogBinding.getRoot();
    }
}