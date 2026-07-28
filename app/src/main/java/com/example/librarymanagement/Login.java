package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.auth.AuthRepository;
import com.google.android.material.checkbox.MaterialCheckBox;

public class Login extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private MaterialCheckBox rememberMe;
    private Button loginButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.txtloginemail);
        passwordInput = findViewById(R.id.txtloginpassword);
        rememberMe = findViewById(R.id.checkboxRememberMe);
        loginButton = findViewById(R.id.btnlogin);
        TextView register = findViewById(R.id.btnswapregister);
        TextView forgot = findViewById(R.id.textView7);
        authRepository = new AuthRepository(this);

        String previousEmail = new SessionManager(this).getSavedEmail();
        if (!previousEmail.isEmpty()) emailInput.setText(previousEmail);

        loginButton.setOnClickListener(v -> login());
        register.setOnClickListener(v -> startActivity(new Intent(this, Register.class)));
        forgot.setOnClickListener(v -> resetPassword());
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (!validEmail(email) || password.isEmpty()) {
            if (!validEmail(email)) emailInput.setError("Enter a valid email address");
            if (password.isEmpty()) passwordInput.setError("Password is required");
            return;
        }
        loginButton.setEnabled(false);
        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(String accountEmail, boolean firebaseUsed) {
                loginButton.setEnabled(true);
                new SessionManager(Login.this).saveLoginSession(accountEmail, rememberMe.isChecked());
                Toast.makeText(Login.this, firebaseUsed ? "Firebase login successful" : "Local login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            @Override public void onError(String message) {
                loginButton.setEnabled(true);
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetPassword() {
        String email = emailInput.getText().toString().trim();
        if (!validEmail(email)) { emailInput.setError("Enter your Firebase email first"); return; }
        authRepository.sendPasswordReset(email, new AuthRepository.AuthCallback() {
            @Override public void onSuccess(String accountEmail, boolean firebaseUsed) {
                Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_LONG).show();
            }
            @Override public void onError(String message) { Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show(); }
        });
    }

    private boolean validEmail(String email) { return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
}
