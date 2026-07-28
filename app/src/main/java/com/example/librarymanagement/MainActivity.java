package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.firebase.FirebaseConfig;

public class MainActivity extends AppCompatActivity {
    private static final long LOAD_SCREEN_DELAY = 1500;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null)getSupportActionBar().hide();
        FirebaseConfig.initialize(this);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Class<?> destination=new SessionManager(this).isLoggedIn()?Home.class:Login.class;
            startActivity(new Intent(this,destination)); finish();
        },LOAD_SCREEN_DELAY);
    }
}
