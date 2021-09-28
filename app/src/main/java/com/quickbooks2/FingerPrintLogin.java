package com.quickbooks2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.quickbooks2.Models.FingerprintHandler;

public class FingerPrintLogin extends AppCompatActivity {

    private MaterialButton btnFingerPrint, btnLater, btnCancelFingerprint;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private ImageView ivFinger;
    private TextView tvTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_login);

        btnFingerPrint = findViewById(R.id.btnFingerPrint);
        btnLater = findViewById(R.id.btnLater);

        View view = getLayoutInflater().inflate(R.layout.finger_print_bottom_sheet, null);
        ivFinger = view.findViewById(R.id.ivFinger);
        tvTouch = view.findViewById(R.id.tvTouch);
        btnCancelFingerprint = view.findViewById(R.id.btnCancelFingerprint);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FingerPrintLogin.this);
        bottomSheetDialog.setContentView(view);


        btnFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                    keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                    if (!fingerprintManager.isHardwareDetected()){
                        Toast.makeText(FingerPrintLogin.this, "Fingerprint scanner is not detected in device.", Toast.LENGTH_SHORT).show();
                    }
                    else if (!keyguardManager.isKeyguardLocked()){
                        Toast.makeText(FingerPrintLogin.this, "Add Lock to your Phone in Settings.", Toast.LENGTH_SHORT).show();
                    }
                    else if (!fingerprintManager.hasEnrolledFingerprints()){
                        Toast.makeText(FingerPrintLogin.this, "You should add atleast 1 Fingerprint to use this feature.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        FingerprintHandler fingerprintHandler = new FingerprintHandler(FingerPrintLogin.this, tvTouch, ivFinger, btnCancelFingerprint);
                        fingerprintHandler.startAuth(fingerprintManager,  null);

                        bottomSheetDialog.show();

                    }
                }


            }
        });
    }
}