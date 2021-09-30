package com.quickbooks2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.quickbooks2.Models.FingerprintHandler;

import java.util.concurrent.Executor;

public class FingerPrintLogin extends AppCompatActivity {

    private MaterialButton btnFingerPrint, btnLater;
    private BiometricManager biometricManager;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_login);

        btnFingerPrint = findViewById(R.id.btnFingerPrint);
        btnLater = findViewById(R.id.btnLater);

        biometricManager = BiometricManager.from(FingerPrintLogin.this);
        executor = ContextCompat.getMainExecutor(FingerPrintLogin.this);
        
        btnFingerPrint.setOnClickListener(view1 -> {

            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS:

                    FingerprintHandler fingerprintHandler = new FingerprintHandler(FingerPrintLogin.this);
                    fingerprintHandler.startAuth(executor, FingerPrintLogin.this);

                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Toast.makeText(FingerPrintLogin.this, "Fingerprint scanner is not detected in device.", Toast.LENGTH_SHORT).show();
                    Log.d("MY_APP_TAG", "No biometric features available on this device.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Toast.makeText(FingerPrintLogin.this, "Add Lock to your Phone in Settings.", Toast.LENGTH_SHORT).show();
                    Log.d("MY_APP_TAG", "Biometric features are currently unavailable.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Toast.makeText(FingerPrintLogin.this, "You should add at least 1 Fingerprint to use this feature.", Toast.LENGTH_SHORT).show();

                    // Prompts the user to create credentials that your app accepts.
                    final Intent enrollIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivity(enrollIntent);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                    Log.d("MY_APP_TAG", "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED");

                    break;
                case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                    Log.d("MY_APP_TAG", "BIOMETRIC_ERROR_UNSUPPORTED");


                    break;

                case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                    Log.d("MY_APP_TAG", "BIOMETRIC_STATUS_UNKNOWN");

                    break;
            }
        });
    }
}