package com.quickbooks2.Models;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.quickbooks2.FingerPrintLogin;
import com.quickbooks2.R;

import java.util.concurrent.Executor;

public class FingerprintHandler extends BiometricPrompt.AuthenticationCallback{
    Context context;
    private BiometricPrompt.PromptInfo promptInfo;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(Executor executor, FingerPrintLogin fingerPrintLogin){
        BiometricPrompt biometricPrompt = new BiometricPrompt(fingerPrintLogin, executor, this );

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint")
                .setSubtitle("Sign in to your account")
                .setNegativeButtonText("CANCEL")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update(errString.toString(), false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Auth failed. ", false);
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//        Log.d("TAGh",  result.getCryptoObject().getSignature().toString());
        this.update("You can now access the app. ", true);
    }

    private void update(String s, boolean b) {

//        tvFingerprint.setText(s);
//
//        if (!b){
//            tvFingerprint.setTextColor(ContextCompat.getColor(context, R.color.purple_200));
//            ivFingerprint.setImageResource(R.drawable.fingerprint_icon_red);
//        }
//        else {
//            tvFingerprint.setTextColor(ContextCompat.getColor(context, R.color.fingerprint_color));
//            ivFingerprint.setImageResource(R.drawable.check_circle_finger);
//        }
    }
}