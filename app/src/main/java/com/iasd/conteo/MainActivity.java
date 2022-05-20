package com.iasd.conteo;

import static com.iasd.conteo.R.*;
import static com.iasd.conteo.R.id.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    //ScrollingFragment scrollingFragment = new ScrollingFragment();
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    AssistantsFragment assistantsFragment = new AssistantsFragment();

    private AppUpdateManager mAppUpdateManager;
    private static final int RCP_AP_UPDATE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        bottomNavigationView = findViewById(bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, RCP_AP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mAppUpdateManager.registerListener(installStateUpdatedListener);

    }
    
    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED){
                showCompletedUpdate();
            }
        }
    };

    @Override
    protected void onStop() {
        if (mAppUpdateManager != null) mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    private void showCompletedUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New app is ready!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RCP_AP_UPDATE && resultCode != RESULT_OK){
            Toast.makeText(this,"Cancel", Toast.LENGTH_SHORT);
        }
        super.onActivityResult(requestCode, resultCode, data);
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