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

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText name = findViewById(R.id.txtname);
        EditText email = findViewById(R.id.txtemail);
        EditText password = findViewById(R.id.txtpassword);
        EditText confirm = findViewById(R.id.txtcomfirmpassword);
        Button register = findViewById(R.id.btnregister);
        TextView login = findViewById(R.id.btnswaplogin);
        AuthRepository repository = new AuthRepository(this);

        register.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString();
            String c = confirm.getText().toString();
            if (n.isEmpty() || e.isEmpty() || p.isEmpty() || c.isEmpty()) {
                Toast.makeText(this, "Please enter all the data", Toast.LENGTH_SHORT).show(); return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) { email.setError("Enter a valid email"); return; }
            if (p.length() < 6) { password.setError("Password must have at least 6 characters"); return; }
            if (!p.equals(c)) { confirm.setError("Passwords do not match"); return; }

            register.setEnabled(false);
            repository.register(n, e, p, new AuthRepository.AuthCallback() {
                @Override public void onSuccess(String accountEmail, boolean firebaseUsed) {
                    register.setEnabled(true);
                    Toast.makeText(Register.this, firebaseUsed ? "Firebase account created" : "Local account created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();
                }
                @Override public void onError(String message) {
                    register.setEnabled(true);
                    Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                }
            });
        });
        login.setOnClickListener(v -> { startActivity(new Intent(this, Login.class)); finish(); });
    }
}
