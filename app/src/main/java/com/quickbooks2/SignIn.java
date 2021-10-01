package com.quickbooks2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.material.button.MaterialButton;

public class SignIn extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvEmailPlace, tvPasswordPlace, tvForgotPassword, tvSignUp;
    private MaterialButton btnSignIn;
    private String valid_email = "", password = "";
    private SpannableString spannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvEmailPlace = findViewById(R.id.tvEmailPlace);
        tvPasswordPlace = findViewById(R.id.tvPasswordPlace);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        String myString = "New to QuickBooks 2? Sign up";
        spannableString = new SpannableString(myString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(SignIn.this, CreateAccount.class));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 21, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignUp.setText(spannableString);
        tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());

//        btnSignIn.setEnabled(false);

        etEmail.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                etEmail.setFocusable(true);
                etEmail.setFocusableInTouchMode(true);
                return false;
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b){
                    tvEmailPlace.setTextColor(ContextCompat.getColor(SignIn.this, R.color.main_color));
                }
                else {
                    etEmail.setFocusable(false);
                    etEmail.setFocusableInTouchMode(false);
                    tvEmailPlace.setTextColor(ContextCompat.getColor(SignIn.this, R.color.black));
                }
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (valid_email.equals("") || password.equals("")){

                    btnSignIn.setEnabled(false);
                }
                else {
                    btnSignIn.setEnabled(true);
                }

                if (etEmail.getText().toString().length() > 0){
                    valid_email = etEmail.getText().toString();
                }
                else {
                    valid_email = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                etPassword.setFocusable(true);
                etPassword.setFocusableInTouchMode(true);
                return false;
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b){
                    tvPasswordPlace.setTextColor(ContextCompat.getColor(SignIn.this, R.color.main_color));
                }
                else {
                    etPassword.setFocusable(false);
                    etPassword.setFocusableInTouchMode(false);
                    tvPasswordPlace.setTextColor(ContextCompat.getColor(SignIn.this, R.color.black));
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (valid_email.equals("") || password.equals("")){

                    btnSignIn.setEnabled(false);
                }
                else {
                    btnSignIn.setEnabled(true);
                }

                if (etPassword.getText().toString().length() > 0){
                    password = etPassword.getText().toString();
                }
                else {
                    password = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignIn.this, valid_email + password, Toast.LENGTH_SHORT).show();
            }
        });
    }
}