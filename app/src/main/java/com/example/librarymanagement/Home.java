package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.librarymanagement.auth.AuthRepository;
import com.example.librarymanagement.fragments.BooksFragment;
import com.example.librarymanagement.fragments.DashboardFragment;
import com.example.librarymanagement.fragments.MembersFragment;
import com.example.librarymanagement.fragments.ReservationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Single-activity dashboard that demonstrates practical Android Fragments. */
public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = findViewById(R.id.homeBottomNavigation);
        navigation.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_books) fragment = new BooksFragment();
            else if (id == R.id.nav_members) fragment = new MembersFragment();
            else if (id == R.id.nav_reservations) fragment = new ReservationsFragment();
            else fragment = new DashboardFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.homeFragmentContainer, fragment)
                    .commit();
            return true;
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            new AuthRepository(this).logout();
            new SessionManager(this).clearSession();
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        if (savedInstanceState == null) navigation.setSelectedItemId(R.id.nav_dashboard);
    }
}
