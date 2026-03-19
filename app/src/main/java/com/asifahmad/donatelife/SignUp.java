package com.asifahmad.donatelife;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends BaseActivity {

    TextInputLayout emailLayout, passwordLayout;
    TextInputEditText emailEdText, passwordEdText;
    Button signupBtn;
    TextView haveAccText;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passLayout);

        emailEdText = findViewById(R.id.emailEditText);
        passwordEdText = findViewById(R.id.passEditText);
        signupBtn = findViewById(R.id.signUpBtn);
        haveAccText = findViewById(R.id.haveAccText);

        haveAccText.setOnClickListener(v -> {
            Intent i = new Intent(SignUp.this, Login.class);
            startActivity(i);
        });

        setupRealTimeValidation();

        signupBtn.setOnClickListener(view -> {
            String email = emailEdText.getText().toString().trim();
            String password = passwordEdText.getText().toString().trim();

            if (!isValidEmail(email)) {
                showValidation(emailLayout, "Invalid email format", false);
                return;
            }

            if (!isStrongPassword(password)) {
                showValidation(passwordLayout, "Min 6 chars, must contain letters & numbers", false);
                return;
            }

            signupBtn.setEnabled(false);
            showLoader();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                signupBtn.setEnabled(true);
                hideLoader();
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, PersonalDetails.class));
                    finish();
                } else {
                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void setupRealTimeValidation() {

        emailEdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s)) {
                    emailLayout.setError(null);
                    emailLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                } else if (isValidEmail(s.toString())) {
                    showValidation(emailLayout, "Valid Email", true);
                } else {
                    showValidation(emailLayout, "Invalid Email", false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordEdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(s)) {
                    passwordLayout.setError(null);
                    passwordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                } else if (isStrongPassword(s.toString())) {
                    showValidation(passwordLayout, "Strong Password", true);
                } else {
                    showValidation(passwordLayout, "Min 6 chars + letters & numbers", false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isStrongPassword(String password) {
        if (password.length() < 6) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }

    private void showValidation(TextInputLayout layout, String message, boolean isValid) {

        layout.setError(isValid ? null : message);

        layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        layout.setEndIconDrawable(isValid ? R.drawable.check_circle : R.drawable.exclamation_circle);
    }
}