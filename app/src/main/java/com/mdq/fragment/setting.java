package com.mdq.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdq.activities.Contact_us;
import com.mdq.activities.DeviceManger_activity;
import com.mdq.activities.Notificationactivity;
import com.mdq.activities.Warrenty_activity;
import com.mdq.activities.our_products;
import com.mdq.activities.profileActivity;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.FragmentLogBinding;
import com.mdq.marinetechapp.databinding.FragmentSettingBinding;

public class setting extends Fragment {

    FragmentSettingBinding fragmentSettingBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false);

        fragmentSettingBinding.DeviceManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), DeviceManger_activity.class));
            }
        });

        fragmentSettingBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), profileActivity.class));
            }
        });

        fragmentSettingBinding.warrantyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), Warrenty_activity.class));
            }
        });

        fragmentSettingBinding.contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), Contact_us.class));
            }
        });

        fragmentSettingBinding.Store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), our_products.class));
            }
        });

        fragmentSettingBinding.RateUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = requireActivity().getPackageName(); // getPackageName() from Context or Activity object

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }

        });


        return fragmentSettingBinding.getRoot();

    }
}