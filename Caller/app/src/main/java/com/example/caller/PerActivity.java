package com.example.caller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PerActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String PREF_PREV_STARTED = "prevStarted";
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        startButton = findViewById(R.id.start);
        startButton.setOnClickListener(v -> checkPermissions());

        // Check SharedPreferences and proceed accordingly
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(PREF_PREV_STARTED, false)) {
            if (areAllPermissionsGranted()) {
                navigateToMainActivity();
            } else {
                checkPermissions();
            }
        }
    }

    /**
     * Verifies if all required permissions are granted.
     */
    private boolean areAllPermissionsGranted() {
        String[] permissions = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Requests all required permissions if not granted.
     */
    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE
        };

        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS
            );
        } else {
            proceedWithAction();
        }
    }

    /**
     * Handles the result of the permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                savePermissionState();
                proceedWithAction();
            } else {
                Toast.makeText(
                        this,
                        "Permissions are required to proceed. Please enable them in app settings.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    /**
     * Saves the permission state to SharedPreferences.
     */
    private void savePermissionState() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_PREV_STARTED, true); // Mark permissions as granted
        editor.apply();
    }

    /**
     * Proceeds to the main activity after permissions are granted.
     */
    private void proceedWithAction() {
        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
        navigateToMainActivity();
    }

    /**
     * Navigates to MainActivity.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(PerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
