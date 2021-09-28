package com.quickbooks2;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.material.button.MaterialButton;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.quickbooks2.Models.CountryCodeModel;
import com.quickbooks2.Models.CountryCodeSpinnerAdapter;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MaterialButton btnSelectCountry, btnCreateAccount;
    private CountryCodeSpinnerAdapter adapter;
    private final ArrayList<CountryCodeModel> arrayList = new ArrayList<>();
    private Dialog listDialog;
    private TextView tvEmailPlace, tvPasswordPlace, tvConfirmPasswordPlace, tvPhonePlace, tvSignIn;
    private EditText etEmail, etPassword, etConfirmPassword, etPhone;
    private String valid_email = "", password = "";
    private TextView tvCondition1, tvCondition2, tvCondition3, tvCondition4;
    private ConstraintLayout constraintLayout;
    private String countryCode = "+93", phone = "";
    private SpannableString spannableString;
    private ProgressDialog progressDialog;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        String accountName = result.getData().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                        etEmail.setText(accountName);
                    }
                }
            });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btnSelectCountry = findViewById(R.id.btnSelectCountry);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvEmailPlace = findViewById(R.id.tvEmailPlace);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        tvPasswordPlace = findViewById(R.id.tvPasswordPlace);
        tvConfirmPasswordPlace = findViewById(R.id.tvConfirmPasswordPlace);
        constraintLayout = findViewById(R.id.constraintLayout);
        tvPhonePlace = findViewById(R.id.tvPhonePlace);
        tvCondition1 = findViewById(R.id.tvCondition1);
        tvCondition2 = findViewById(R.id.tvCondition2);
        tvCondition3 = findViewById(R.id.tvCondition3);
        tvCondition4 = findViewById(R.id.tvCondition4);
        tvSignIn = findViewById(R.id.tvSignIn);

        String myString = "Already have an Account?  Sign in";
        spannableString = new SpannableString(myString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(CreateAccount.this, FingerPrintLogin.class));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 25, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignIn.setText(spannableString);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());


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
                    if (etEmail.getText().length() <= 0){

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            Intent intent = AccountManager.newChooseAccountIntent(null, null,
                                    new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, null,
                                    null, null, null);
                            launcher.launch(intent);                        }
                    }
                    tvEmailPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                }
                else {
                    etEmail.setFocusable(false);
                    etEmail.setFocusableInTouchMode(false);
                    tvEmailPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                }
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Is_Valid_Email(etEmail); // pass your EditText Obj here.

            }
            public void Is_Valid_Email(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                }
                else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                }
                else {
                    valid_email = edt.getText().toString();
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null ,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.check_circle_finger) ,null);
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
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
                    constraintLayout.setVisibility(View.VISIBLE);
                    tvPasswordPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                }
                else {
                    constraintLayout.setVisibility(View.GONE);
                    etPassword.setFocusable(false);
                    etPassword.setFocusableInTouchMode(false);
                    tvPasswordPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("asdfgh", "onTextChanged: "+editable.toString());

                if (etPassword.getText().toString().length() > 0){
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.closed_lock), null);
                }
                else {
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.lock_open), null);
                }

                if (validPassword(etPassword.getText().toString())){
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.checked_lock), null);
                    tvConfirmPasswordPlace.setVisibility(View.VISIBLE);
                    etConfirmPassword.setVisibility(View.VISIBLE);
                }
                else {

                    etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.closed_lock), null);
                    tvConfirmPasswordPlace.setVisibility(View.GONE);
                    etConfirmPassword.setVisibility(View.GONE);
                }
            }

            public boolean validPassword(String password){
                boolean upCase = false;
                boolean loCase = false;
                boolean isDigit = false;
                boolean spChar = false;
                boolean greaterThen8 = false;

                Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
                Pattern lowerCasePatten = Pattern.compile("[a-z ]");
                Pattern digitCasePatten = Pattern.compile("[0-9 ]");

                    if (UpperCasePatten.matcher(password).find() && lowerCasePatten.matcher(password).find()){
                        upCase = true;
                        loCase = true;
                        tvCondition2.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                        tvCondition2.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.check),
                                null, null, null);
                    }
                    else {
                        tvCondition2.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                        tvCondition2.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.cross),
                                null, null, null);
                    }
                    if (digitCasePatten.matcher(password).find()){
                        isDigit = true;
                        tvCondition3.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                        tvCondition3.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.check),
                                null, null,null);
                    }
                    else {
                        tvCondition3.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                        tvCondition3.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.cross),
                                null, null, null);
                    }
                    if (specailCharPatten.matcher(password).find()){
                        spChar = true;
                        tvCondition4.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                        tvCondition4.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.check),
                                null, null,null);
                    }
                    else {
                        tvCondition4.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                        tvCondition4.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.cross),
                                null, null,null);
                    }
                    if (password.length() >= 8){
                        greaterThen8 = true;
                        tvCondition1.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                        tvCondition1.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.check),
                                null, null,null);
                    }
                    else {
                        tvCondition1.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                        tvCondition1.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(CreateAccount.this, R.drawable.cross),
                                null, null,null);
                    }
                return (upCase && loCase && isDigit && spChar && greaterThen8);
            }
        });

        etConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                etConfirmPassword.setFocusable(true);
                etConfirmPassword.setFocusableInTouchMode(true);
                return false;
            }
        });
        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b){
                    tvConfirmPasswordPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                }
                else {
                    etConfirmPassword.setFocusable(false);
                    etConfirmPassword.setFocusableInTouchMode(false);
                    tvConfirmPasswordPlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));

                    if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                        etConfirmPassword.setError("Confirmation password does not match. Please make sure password and confirmation password are the same.");
                    }
                }
            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPassword.getText().toString().equals(charSequence.toString())){
                    etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null ,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.check_circle_finger) ,null);
                    password = etPassword.getText().toString();
                }
                else {
                    etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null , null ,null);
                    password = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPhone.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                etPhone.setFocusable(true);
                etPhone.setFocusableInTouchMode(true);
                return false;
            }
        });
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b){
                    tvPhonePlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.main_color));
                }
                else {
                    etPhone.setFocusable(false);
                    etPhone.setFocusableInTouchMode(false);
                    tvPhonePlace.setTextColor(ContextCompat.getColor(CreateAccount.this, R.color.black));
                }
            }
        });
        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isPhoneNumberValid(charSequence.toString(), countryCode) != null){
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null, null ,
                            ContextCompat.getDrawable(CreateAccount.this, R.drawable.check_circle_finger) ,null);


                    phone = isPhoneNumberValid(charSequence.toString(), countryCode);
                    Log.d("phoneNum1", phone);

                }
                else {
                    etPhone.setCompoundDrawablesWithIntrinsicBounds(null, null , null,null);
                    phone = "";
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            public String isPhoneNumberValid(String phoneNumber, String countryCode) {

                PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

                Phonenumber.PhoneNumber phoneNumber1 = null;
                String finalNumber = null;
                String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
                boolean isValid = false;
                PhoneNumberUtil.PhoneNumberType isMobile = null;

                //NOTE: This should probably be a member variable.
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                try
                {
                    phoneNumber1 = phoneNumberUtil.parse(phoneNumber, isoCode);
                    isValid = phoneNumberUtil.isValidNumber(phoneNumber1);
                    isMobile = phoneNumberUtil.getNumberType(phoneNumber1);

                }
                catch (NumberParseException e) {
                    Log.d("phoneNum2", e.getMessage());
                }
                if (isValid && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile || PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
                    finalNumber = phoneNumberUtil.format(phoneNumber1,
                            PhoneNumberUtil.PhoneNumberFormat.E164).substring(1);
                }

                String phone = String.valueOf(finalNumber);
                Log.d("phoneNum3", phone);

                return finalNumber;
            }
        });

        {
            arrayList.clear();
            arrayList.add(new CountryCodeModel("af", "Afghanistan", "+93", "🇦🇫"));
            arrayList.add(new CountryCodeModel("al", "Albania", "+355", "🇦🇱"));
            arrayList.add(new CountryCodeModel("dz", "Algeria", "+213", "🇩🇿"));
            arrayList.add(new CountryCodeModel("as", "American Samoa", "+1684", "🇦🇸"));
            arrayList.add(new CountryCodeModel("ad", "Andorra", "+376", "🇦🇩"));
            arrayList.add(new CountryCodeModel("ao", "Angola", "+244", "🇦🇴"));
            arrayList.add(new CountryCodeModel("ai", "Anguilla", "+1264", "🇦🇮"));
            arrayList.add(new CountryCodeModel("aq", "Antarctica", "+672", "🇦🇶"));
            arrayList.add(new CountryCodeModel("ag", "Antigua and Barbuda", "+1268", "🇦🇬"));
            arrayList.add(new CountryCodeModel("ar", "Argentina", "+54", "🇦🇷"));
            arrayList.add(new CountryCodeModel("am", "Armenia", "+374", "🇦🇲"));
            arrayList.add(new CountryCodeModel("aw", "Aruba", "+297", "🇦🇼"));
            arrayList.add(new CountryCodeModel("au", "Australia", "+61", "🇦🇺"));
            arrayList.add(new CountryCodeModel("at", "Austria", "+43", "🇦🇹"));
            arrayList.add(new CountryCodeModel("az", "Azerbaijan", "+994", "🇦🇿"));
            arrayList.add(new CountryCodeModel("bs", "Bahamas", "+1242", "🇧🇸"));
            arrayList.add(new CountryCodeModel("bh", "Bahrain", "+973", "🇧🇭"));
            arrayList.add(new CountryCodeModel("bd", "Bangladesh", "+880", "🇧🇩"));
            arrayList.add(new CountryCodeModel("bb", "Barbados", "+1242", "🇧🇧"));
            arrayList.add(new CountryCodeModel("by", "Belarus", "+375", "🇧🇾"));
            arrayList.add(new CountryCodeModel("be", "Belgium", "+32", "🇧🇪"));
            arrayList.add(new CountryCodeModel("bz", "Belize", "+501", "🇧🇿"));
            arrayList.add(new CountryCodeModel("bj", "Benin", "+229", "🇧🇯"));
            arrayList.add(new CountryCodeModel("bm", "Bermuda", "+1441", "🇧🇲"));
            arrayList.add(new CountryCodeModel("bt", "Bhutan", "+975", "🇧🇹"));
            arrayList.add(new CountryCodeModel("bo", "Bolivia", "+591", "🇧🇴"));
            arrayList.add(new CountryCodeModel("ba", "Bosnia And Herzegovina", "+387", "🇧🇦"));
            arrayList.add(new CountryCodeModel("bw", "Botswana", "+267", "🇧🇼"));
            arrayList.add(new CountryCodeModel("br", "Brazil", "+55", "🇧🇷"));
            arrayList.add(new CountryCodeModel("io", "British (IOT)", "+246", "🇮🇴"));
            arrayList.add(new CountryCodeModel("vg", "British (VI)", "+1284", "🇻🇬"));
            arrayList.add(new CountryCodeModel("bn", "Brunei Darussalam", "+673", "🇧🇳"));
            arrayList.add(new CountryCodeModel("bg", "Bulgaria", "+359", "🇧🇬"));
            arrayList.add(new CountryCodeModel("bf", "Burkina Faso", "+226", "🇧🇫"));
            arrayList.add(new CountryCodeModel("bi", "Burundi", "+257", "🇧🇮"));
            arrayList.add(new CountryCodeModel("kh", "Cambodia", "+855", "🇰🇭"));
            arrayList.add(new CountryCodeModel("cm", "Cameroon", "+237", "🇨🇲"));
            arrayList.add(new CountryCodeModel("ca", "Canada", "+1", "🇨🇦"));
            arrayList.add(new CountryCodeModel("cv", "Cape Verde", "+238", "🇨🇻"));
            arrayList.add(new CountryCodeModel("ky", "Cayman Islands", "+345", "🇰🇾"));
            arrayList.add(new CountryCodeModel("cf", "Central African Republic", "+236", "🇨🇫"));
            arrayList.add(new CountryCodeModel("td", "Chad", "+235", "🇹🇩"));
            arrayList.add(new CountryCodeModel("cl", "Chile", "+56", "🇨🇱"));
            arrayList.add(new CountryCodeModel("cn", "China", "+86", "🇨🇳"));
            arrayList.add(new CountryCodeModel("cx", "Christmas Island", "+61", "🇨🇽"));
            arrayList.add(new CountryCodeModel("cc", "Cocos (keeling) Islands", "+61", "🇨🇨"));
            arrayList.add(new CountryCodeModel("co", "Colombia", "+57", "🇨🇴"));
            arrayList.add(new CountryCodeModel("km", "Comoros", "+269", "🇰🇲"));
            arrayList.add(new CountryCodeModel("ck", "Cook Islands", "+682", "🇨🇰"));
            arrayList.add(new CountryCodeModel("cr", "Costa Rica", "+506", "🇨🇷"));
            arrayList.add(new CountryCodeModel("hr", "Croatia", "+385", "🇭🇷"));
            arrayList.add(new CountryCodeModel("cu", "Cuba", "+53", "🇨🇺"));
            arrayList.add(new CountryCodeModel("cy", "Cyprus", "+357", "🇨🇾"));
            arrayList.add(new CountryCodeModel("cz", "Czech Republic", "+420", "🇨🇿"));
            arrayList.add(new CountryCodeModel("ci", "Côte D'ivoire", "+225", "🇨🇮"));
            arrayList.add(new CountryCodeModel("cd", "Congo (DRC)", "+243", "🇨🇩"));
            arrayList.add(new CountryCodeModel("dk", "Denmark", "+45", "🇩🇰"));
            arrayList.add(new CountryCodeModel("dj", "Djibouti", "+253", "🇩🇯"));
            arrayList.add(new CountryCodeModel("dm", "Dominica", "+1767", "🇩🇲"));
            arrayList.add(new CountryCodeModel("do", "Dominican (R)", "+1849", "🇩🇴"));
            arrayList.add(new CountryCodeModel("ec", "Ecuador", "+593", "🇪🇨"));
            arrayList.add(new CountryCodeModel("eg", "Egypt", "+20", "🇪🇬"));
            arrayList.add(new CountryCodeModel("sv", "El Salvador", "+503", "🇸🇻"));
            arrayList.add(new CountryCodeModel("gq", "Equatorial Guinea", "+240", "🇬🇶"));
            arrayList.add(new CountryCodeModel("er", "Eritrea", "+291", "🇪🇷"));
            arrayList.add(new CountryCodeModel("ee", "Estonia", "+372", "🇪🇪"));
            arrayList.add(new CountryCodeModel("et", "Ethiopia", "+251", "🇪🇹"));
            arrayList.add(new CountryCodeModel("fk", "Falkland Islands (malvinas)", "+500", "🇫🇰"));
            arrayList.add(new CountryCodeModel("fo", "Faroe Islands", "+298", "🇫🇴"));
            arrayList.add(new CountryCodeModel("fj", "Fiji", "+679", "🇫🇯"));
            arrayList.add(new CountryCodeModel("fi", "Finland", "+358", "🇫🇮"));
            arrayList.add(new CountryCodeModel("fr", "France", "+33", "🇫🇷"));
            arrayList.add(new CountryCodeModel("gf", "French Guiana", "+594", "🇬🇫"));
            arrayList.add(new CountryCodeModel("pf", "French Polynesia", "+689", "🇵🇫"));
            arrayList.add(new CountryCodeModel("ga", "Gabon", "+241", "🇬🇦"));
            arrayList.add(new CountryCodeModel("gm", "Gambia", "+220", "🇬🇲"));
            arrayList.add(new CountryCodeModel("ge", "Georgia", "+995", "🇬🇪"));
            arrayList.add(new CountryCodeModel("de", "Germany", "+49", "🇩🇪"));
            arrayList.add(new CountryCodeModel("gh", "Ghana", "+233", "🇬🇭"));
            arrayList.add(new CountryCodeModel("gi", "Gibraltar", "+350", "🇬🇮"));
            arrayList.add(new CountryCodeModel("gr", "Greece", "+30", "🇬🇷"));
            arrayList.add(new CountryCodeModel("gl", "Greenland", "+299", "🇬🇱"));
            arrayList.add(new CountryCodeModel("gd", "Grenada", "+1473", "🇬🇩"));
            arrayList.add(new CountryCodeModel("gp", "Guadeloupe", "+590", "🇬🇵"));
            arrayList.add(new CountryCodeModel("gu", "Guam", "+1671", "🇬🇺"));
            arrayList.add(new CountryCodeModel("gt", "Guatemala", "+502", "🇬🇹"));
            arrayList.add(new CountryCodeModel("gg", "Guernsey", "+44", "🇬🇬"));
            arrayList.add(new CountryCodeModel("gn", "Guinea", "+224", "🇬🇳"));
            arrayList.add(new CountryCodeModel("gw", "Guinea-Bissau", "+245", "🇬🇼"));
            arrayList.add(new CountryCodeModel("gy", "Guyana", "+592", "🇬🇾"));
            arrayList.add(new CountryCodeModel("ht", "Haiti", "+509", "🇭🇹"));
            arrayList.add(new CountryCodeModel("va", "Holy See (VCS)", "+379", "🇻🇦"));
            arrayList.add(new CountryCodeModel("hn", "Honduras", "+504", "🇭🇳"));
            arrayList.add(new CountryCodeModel("hk", "Hong Kong", "+852", "🇭🇰"));
            arrayList.add(new CountryCodeModel("hu", "Hungary", "+36", "🇭🇺"));
            arrayList.add(new CountryCodeModel("is", "Iceland", "+354", "🇮🇸"));
            arrayList.add(new CountryCodeModel("in", "India", "+91", "🇮🇳"));
            arrayList.add(new CountryCodeModel("id", "Indonesia", "+62", "🇮🇩"));
            arrayList.add(new CountryCodeModel("ir", "Iran", "+98", "🇮🇷"));
            arrayList.add(new CountryCodeModel("iq", "Iraq", "+964", "🇮🇶"));
            arrayList.add(new CountryCodeModel("ie", "Ireland", "+353", "🇮🇪"));
            arrayList.add(new CountryCodeModel("im", "Isle Of Man", "+44", "🇮🇲"));
            arrayList.add(new CountryCodeModel("il", "Israel", "+972", "🇮🇱"));
            arrayList.add(new CountryCodeModel("it", "Italy", "+39", "🇮🇹"));
            arrayList.add(new CountryCodeModel("jm", "Jamaica", "+1876", "🇯🇲"));
            arrayList.add(new CountryCodeModel("jp", "Japan", "+81", "🇯🇵"));
            arrayList.add(new CountryCodeModel("je", "Jersey", "+44", "🇯🇪"));
            arrayList.add(new CountryCodeModel("jo", "Jordan", "+962", "🇯🇴"));
            arrayList.add(new CountryCodeModel("kz", "Kazakhstan", "+7", "🇰🇿"));
            arrayList.add(new CountryCodeModel("ke", "Kenya", "+254", "🇰🇪"));
            arrayList.add(new CountryCodeModel("ki", "Kiribati", "+686", "🇰🇮"));
            arrayList.add(new CountryCodeModel("xk", "Kosovo", "+383", "🇽🇰"));
            arrayList.add(new CountryCodeModel("kw", "Kuwait", "+965", "🇰🇼"));
            arrayList.add(new CountryCodeModel("kg", "Kyrgyzstan", "+996", "🇰🇬"));
            arrayList.add(new CountryCodeModel("la", "Lao (PDR)", "+856", "🇱🇦"));
            arrayList.add(new CountryCodeModel("lv", "Latvia", "+371", "🇱🇻"));
            arrayList.add(new CountryCodeModel("lb", "Lebanon", "+961", "🇱🇧"));
            arrayList.add(new CountryCodeModel("ls", "Lesotho", "+266", "🇱🇸"));
            arrayList.add(new CountryCodeModel("lr", "Liberia", "+231", "🇱🇷"));
            arrayList.add(new CountryCodeModel("ly", "Libya", "+218", "🇱🇾"));
            arrayList.add(new CountryCodeModel("li", "Liechtenstein", "+423", "🇱🇮"));
            arrayList.add(new CountryCodeModel("lt", "Lithuania", "+370", "🇱🇹"));
            arrayList.add(new CountryCodeModel("lu", "Luxembourg", "+352", "🇱🇺"));
            arrayList.add(new CountryCodeModel("mo", "Macao Sar China", "+853", "🇲🇴"));
            arrayList.add(new CountryCodeModel("mk", "Macedonia", "+389", "🇲🇰"));
            arrayList.add(new CountryCodeModel("mg", "Madagascar", "+261", "🇲🇬"));
            arrayList.add(new CountryCodeModel("mw", "Malawi", "+265", "🇲🇼"));
            arrayList.add(new CountryCodeModel("my", "Malaysia", "+60", "🇲🇾"));
            arrayList.add(new CountryCodeModel("mv", "Maldives", "+960", "🇲🇻"));
            arrayList.add(new CountryCodeModel("ml", "Mali", "+223", "🇲🇱"));
            arrayList.add(new CountryCodeModel("mt", "Malta", "+356", "🇲🇹"));
            arrayList.add(new CountryCodeModel("mh", "Marshall Islands", "+692", "🇲🇭"));
            arrayList.add(new CountryCodeModel("mq", "Martinique", "+596", "🇲🇶"));
            arrayList.add(new CountryCodeModel("mr", "Mauritania", "+222", "🇲🇷"));
            arrayList.add(new CountryCodeModel("mu", "Mauritius", "+230", "🇲🇺"));
            arrayList.add(new CountryCodeModel("yt", "Mayotte", "+262", "🇾🇹"));
            arrayList.add(new CountryCodeModel("mx", "Mexico", "+52", "🇲🇽"));
            arrayList.add(new CountryCodeModel("fm", "Micronesia", "+691", "🇫🇲"));
            arrayList.add(new CountryCodeModel("md", "Moldova", "+373", "🇲🇩"));
            arrayList.add(new CountryCodeModel("mc", "Monaco", "+377", "🇲🇨"));
            arrayList.add(new CountryCodeModel("mn", "Mongolia", "+976", "🇲🇳"));
            arrayList.add(new CountryCodeModel("me", "Montenegro", "+382", "🇲🇪"));
            arrayList.add(new CountryCodeModel("ms", "Montserrat", "+1664", "🇲🇸"));
            arrayList.add(new CountryCodeModel("ma", "Morocco", "+212", "🇲🇦"));
            arrayList.add(new CountryCodeModel("mz", "Mozambique", "+258", "🇲🇿"));
            arrayList.add(new CountryCodeModel("mm", "Myanmar (Burma)", "+95", "🇲🇲"));
            arrayList.add(new CountryCodeModel("na", "Namibia", "+264", "🇳🇦"));
            arrayList.add(new CountryCodeModel("nr", "Nauru", "+674", "🇳🇷"));
            arrayList.add(new CountryCodeModel("np", "Nepal", "+977", "🇳🇵"));
            arrayList.add(new CountryCodeModel("nl", "Netherlands", "+31", "🇳🇱"));
            arrayList.add(new CountryCodeModel("nc", "New Caledonia", "+687", "🇳🇨"));
            arrayList.add(new CountryCodeModel("nz", "New Zealand", "+64", "🇳🇿"));
            arrayList.add(new CountryCodeModel("ni", "Nicaragua", "+505", "🇳🇮"));
            arrayList.add(new CountryCodeModel("ne", "Niger", "+227", "🇳🇪"));
            arrayList.add(new CountryCodeModel("ng", "Nigeria", "+234", "🇳🇬"));
            arrayList.add(new CountryCodeModel("nu", "Niue", "+683", "🇳🇺"));
            arrayList.add(new CountryCodeModel("nf", "Norfolk Island", "+1670", "🇳🇫"));
            arrayList.add(new CountryCodeModel("kp", "North Korea", "+672", "🇰🇵"));
            arrayList.add(new CountryCodeModel("mp", "Mariana Islands (N)", "+850", "🇲🇵"));
            arrayList.add(new CountryCodeModel("no", "Norway", "+47", "🇳🇴"));
            arrayList.add(new CountryCodeModel("om", "Oman", "+968", "🇴🇲"));
            arrayList.add(new CountryCodeModel("pk", "Pakistan", "+92", "🇵🇰"));
            arrayList.add(new CountryCodeModel("pw", "Palau", "+680", "🇵🇼"));
            arrayList.add(new CountryCodeModel("ps", "Palestinian Territory, (O)", "+970", "🇵🇸"));
            arrayList.add(new CountryCodeModel("pa", "Panama", "+507", "🇵🇦"));
            arrayList.add(new CountryCodeModel("pg", "Papua New Guinea", "+675", "🇵🇬"));
            arrayList.add(new CountryCodeModel("py", "Paraguay", "+595", "🇵🇾"));
            arrayList.add(new CountryCodeModel("pe", "Peru", "+51", "🇵🇪"));
            arrayList.add(new CountryCodeModel("ph", "Philippines", "+63", "🇵🇭"));
            arrayList.add(new CountryCodeModel("pn", "Pitcairn Islands", "+870", "🇵🇳"));
            arrayList.add(new CountryCodeModel("pl", "Poland", "+48", "🇵🇱"));
            arrayList.add(new CountryCodeModel("pt", "Portugal", "+351", "🇵🇹"));
            arrayList.add(new CountryCodeModel("pr", "Puerto Rico", "+1939", "🇵🇷"));
            arrayList.add(new CountryCodeModel("qa", "Qatar", "+974", "🇶🇦"));
            arrayList.add(new CountryCodeModel("cg", "Brazzaville (RC)", "+242", "🇨🇬"));
            arrayList.add(new CountryCodeModel("ro", "Romania", "+40", "🇷🇴"));
            arrayList.add(new CountryCodeModel("ru", "Russian Federation", "+7", "🇷🇺"));
            arrayList.add(new CountryCodeModel("rw", "Rwanda", "+250", "🇷🇼"));
            arrayList.add(new CountryCodeModel("re", "Réunion", "+262", "🇷🇪"));
            arrayList.add(new CountryCodeModel("bl", "Saint Barthélemy", "+590", "🇧🇱"));
            arrayList.add(new CountryCodeModel("sh", "Saint Helena", "+290", "🇸🇭"));
            arrayList.add(new CountryCodeModel("kn", "Saint Kitts & Nevis", "+1869", "🇰🇳"));
            arrayList.add(new CountryCodeModel("lc", "Saint Lucia", "+1758", "🇱🇨"));
            arrayList.add(new CountryCodeModel("mf", "Saint Martin", "+590", "🇲🇫"));
            arrayList.add(new CountryCodeModel("pm", "Saint (Pie & Miq)", "+508", "🇵🇲"));
            arrayList.add(new CountryCodeModel("vc", "Saint (Vin & The Gre)", "+1784", "🇻🇨"));
            arrayList.add(new CountryCodeModel("ws", "Samoa", "+685", "🇼🇸"));
            arrayList.add(new CountryCodeModel("sm", "San Marino", "+378", "🇸🇲"));
            arrayList.add(new CountryCodeModel("st", "Sao Tome & Principe", "+239", "🇸🇹"));
            arrayList.add(new CountryCodeModel("sa", "Saudi Arabia", "+966", "🇸🇦"));
            arrayList.add(new CountryCodeModel("sn", "Senegal", "+221", "🇸🇳"));
            arrayList.add(new CountryCodeModel("rs", "Serbia", "+381", "🇷🇸"));
            arrayList.add(new CountryCodeModel("sc", "Seychelles", "+248", "🇸🇨"));
            arrayList.add(new CountryCodeModel("sl", "Sierra Leone", "+232", "🇸🇱"));
            arrayList.add(new CountryCodeModel("sg", "Singapore", "+65", "🇸🇬"));
            arrayList.add(new CountryCodeModel("sx", "Sint Maarten", "+1", "🇸🇽"));
            arrayList.add(new CountryCodeModel("sk", "Slovakia", "+421", "🇸🇰"));
            arrayList.add(new CountryCodeModel("si", "Slovenia", "+386", "🇸🇮"));
            arrayList.add(new CountryCodeModel("sb", "Solomon Islands", "+677", "🇸🇧"));
            arrayList.add(new CountryCodeModel("so", "Somalia", "+252", "🇸🇴"));
            arrayList.add(new CountryCodeModel("za", "South Africa", "+27", "🇿🇦"));
            arrayList.add(new CountryCodeModel("gs", "South Africa (South Georgia & South Sandwich Islands)", "+500", "🇬🇸"));
            arrayList.add(new CountryCodeModel("kr", "South Korea", "+82", "🇰🇷"));
            arrayList.add(new CountryCodeModel("ss", "South Sudan", "+211", "🇸🇸"));
            arrayList.add(new CountryCodeModel("es", "Spain", "+34", "🇪🇸"));
            arrayList.add(new CountryCodeModel("lk", "Sri Lanka", "+94", "🇱🇰"));
            arrayList.add(new CountryCodeModel("sd", "Sudan", "+249", "🇸🇩"));
            arrayList.add(new CountryCodeModel("sr", "Suriname", "+597", "🇸🇷"));
            arrayList.add(new CountryCodeModel("sz", "Swaziland", "+268", "🇸🇿"));
            arrayList.add(new CountryCodeModel("se", "Sweden", "+46", "🇸🇪"));
            arrayList.add(new CountryCodeModel("ch", "Switzerland", "+41", "🇨🇭"));
            arrayList.add(new CountryCodeModel("sy", "Syrian Arab Republic", "+963", "🇸🇾"));
            arrayList.add(new CountryCodeModel("tw", "Taiwan", "+886", "🇹🇼"));
            arrayList.add(new CountryCodeModel("tj", "Tajikistan", "+992", "🇹🇯"));
            arrayList.add(new CountryCodeModel("tz", "Tanzania", "+255", "🇹🇿"));
            arrayList.add(new CountryCodeModel("th", "Thailand", "+66", "🇹🇭"));
            arrayList.add(new CountryCodeModel("tl", "Timor-Leste", "+670", "🇹🇱"));
            arrayList.add(new CountryCodeModel("tg", "Togo", "+228", "🇹🇬"));
            arrayList.add(new CountryCodeModel("tk", "Tokelau", "+690", "🇹🇰"));
            arrayList.add(new CountryCodeModel("to", "Tonga", "+676", "🇹🇴"));
            arrayList.add(new CountryCodeModel("tt", "Trinidad & Tobago", "+1868", "🇹🇹"));
            arrayList.add(new CountryCodeModel("tn", "Tunisia", "+216", "🇹🇳"));
            arrayList.add(new CountryCodeModel("tr", "Turkey", "+90", "🇹🇷"));
            arrayList.add(new CountryCodeModel("tm", "Turkmenistan", "+993", "🇹🇲"));
            arrayList.add(new CountryCodeModel("tc", "Turks & Caicos Islands", "+1649", "🇹🇨"));
            arrayList.add(new CountryCodeModel("tv", "Tuvalu", "+688", "🇹🇻"));
            arrayList.add(new CountryCodeModel("ug", "Uganda", "+256", "🇺🇬"));
            arrayList.add(new CountryCodeModel("ua", "Ukraine", "+380", "🇺🇦"));
            arrayList.add(new CountryCodeModel("ae", "United Arab Emirates", "+971", "🇦🇪"));
            arrayList.add(new CountryCodeModel("gb", "United Kingdom", "+44", "🇬🇧"));
            arrayList.add(new CountryCodeModel("us", "United States", "+1", "🇺🇸"));
            arrayList.add(new CountryCodeModel("uy", "Uruguay", "+598", "🇺🇾"));
            arrayList.add(new CountryCodeModel("vi", "US Virgin Islands", "+1340", "🇻🇮"));
            arrayList.add(new CountryCodeModel("uz", "Uzbekistan", "+998", "🇺🇿"));
            arrayList.add(new CountryCodeModel("vu", "Vanuatu", "+678", "🇻🇺"));
            arrayList.add(new CountryCodeModel("ve", "Venezuela", "+58", "🇻🇪"));
            arrayList.add(new CountryCodeModel("vn", "Vietnam", "+84", "🇻🇳"));
            arrayList.add(new CountryCodeModel("wf", "Wallis And Futuna", "+681", "🇼🇫"));
            arrayList.add(new CountryCodeModel("ye", "Yemen", "+967", "🇾🇪"));
            arrayList.add(new CountryCodeModel("zm", "Zambia", "+260", "🇿🇲"));
            arrayList.add(new CountryCodeModel("zw", "Zimbabwe", "+263", "🇿🇼"));
            arrayList.add(new CountryCodeModel("ax", "Åland Islands", "+358", "🇦🇽"));
        }

        btnSelectCountry.setText(arrayList.get(0).getCountryFlag() + "   " + arrayList.get(0).getPhoneCode());
        btnSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });

        progressDialog = new ProgressDialog(CreateAccount.this);
        progressDialog.setMessage("Signing In");

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid_email.equals("") || password.equals("") || phone.equals("")){
                    Toast.makeText(CreateAccount.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("allCredentials", "Email: " + valid_email +" Password: "+password+" Phone: "+phone);
                    progressDialog.show();
                }
            }
        });
    }

    private void showdialog() {
        listDialog = new Dialog(this);
        listDialog.setTitle("Select Item");
        View v = getLayoutInflater().inflate(R.layout.dialog_list, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!

        ListView list = (ListView) listDialog.findViewById(R.id.listview);

        list.setOnItemClickListener(this);
        adapter = new CountryCodeSpinnerAdapter(CreateAccount.this,arrayList);
        list.setAdapter(adapter);

        //now that the dialog is set up, it's time to show it
        listDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        CountryCodeModel countryModel = (CountryCodeModel) adapterView.getAdapter().getItem(position);
        Log.d("spinnerItem", countryModel.getPhoneCode());

        btnSelectCountry.setText(countryModel.getCountryFlag() + "   " + countryModel.getPhoneCode());
        countryCode = countryModel.getPhoneCode();
        listDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}