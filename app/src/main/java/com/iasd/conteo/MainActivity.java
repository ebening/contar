package com.iasd.conteo;

import static com.iasd.conteo.R.*;
import static com.iasd.conteo.R.id.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //ScrollingFragment scrollingFragment = new ScrollingFragment();
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    AssistantsFragment assistantsFragment = new AssistantsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        bottomNavigationView = findViewById(bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(container, homeFragment).commit();



        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case home:
                getSupportFragmentManager().beginTransaction().replace(container, homeFragment).commit();
                return true;
            case assistant:
                getSupportFragmentManager().beginTransaction().replace(container, assistantsFragment).commit();
                return true;
            case setting:
                getSupportFragmentManager().beginTransaction().replace(container, settingsFragment).commit();
                return true;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
    }
}