package com.mdq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.mdq.fragment.home;
import com.mdq.fragment.log;
import com.mdq.fragment.setting;
import com.mdq.marinetechapp.R;
import com.mdq.marinetechapp.databinding.ActivityHomeBinding;
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation;
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigationKt;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {

    CurvedBottomNavigation curvedBottomNavigation;
    ActivityHomeBinding activityHomeBinding;
    private FragmentManager fragmentManager;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        try {
            Intent intent = getIntent();
            from = intent.getStringExtra("from");
        } catch (Exception e) {

        }

        curvedBottomNavigation = findViewById(R.id.bottomNavigation);
        fragmentManager = getSupportFragmentManager();
        curvedBottomNavigation.add(new CurvedBottomNavigation.Model(1, "Log", R.drawable.ic_icons8_clock_96));
        curvedBottomNavigation.add(new CurvedBottomNavigation.Model(2, "Home", R.drawable.ic_home_svgrepo_com));
        curvedBottomNavigation.add(new CurvedBottomNavigation.Model(3, "Settings", R.drawable.ic_icons8_settings));
        curvedBottomNavigation.show(2, true);
        loadFragment(new home());


        curvedBottomNavigation.setOnClickMenuListener(new Function1<CurvedBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(CurvedBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        loadFragment(new log());
                        break;
                    case 2:
                        loadFragment(new home());
                        break;
                    case 3:
                        loadFragment(new setting());
                        break;
                }
                return null;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
//        if(from.trim().equals("login") ||from.trim().equals("signup")){
//            Bundle bundle = new Bundle();
//            String myMessage = from;
//            bundle.putString("message", myMessage);
//            fragment.setArguments(bundle);
//        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_home_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}