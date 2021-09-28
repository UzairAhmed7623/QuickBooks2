package com.quickbooks2.Models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.quickbooks2.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    Context context;
    TextView tvFingerprint;
    ImageView ivFingerprint;
    MaterialButton btnCancel;

    public FingerprintHandler(Context context, TextView tvFingerprint, ImageView ivFingerprint, MaterialButton btnCancel) {
        this.context = context;
        this.tvFingerprint = tvFingerprint;
        this.ivFingerprint = ivFingerprint;
        this.btnCancel = btnCancel;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal,0, this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an Auth Error. " + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Auth failed. ", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the app. ", true);
    }

    private void update(String s, boolean b) {

        tvFingerprint.setText(s);

        if (b == false){
            tvFingerprint.setTextColor(ContextCompat.getColor(context, R.color.purple_200));
            ivFingerprint.setImageResource(R.drawable.fingerprint_icon_red);
        }
        else {
            tvFingerprint.setTextColor(ContextCompat.getColor(context, R.color.fingerprint_color));
            ivFingerprint.setImageResource(R.drawable.check_circle_finger);
        }
    }
}