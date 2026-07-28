package com.example.librarymanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanagement.OnlineBooksActivity;
import com.example.librarymanagement.Book;
import com.example.librarymanagement.Member;
import com.example.librarymanagement.R;
import com.example.librarymanagement.ReservationActivity;
import com.example.librarymanagement.ViewBook;
import com.example.librarymanagement.ViewMember;
import com.example.librarymanagement.ViewReservedBook;
import com.example.librarymanagement.firebase.FirebaseConfig;

public class DashboardFragment extends Fragment {
    public DashboardFragment() { super(R.layout.fragment_dashboard); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnDashboardBooks).setOnClickListener(v -> startActivity(new Intent(requireContext(), Book.class)));
        view.findViewById(R.id.btnDashboardMembers).setOnClickListener(v -> startActivity(new Intent(requireContext(), Member.class)));
        view.findViewById(R.id.btnDashboardReserve).setOnClickListener(v -> startActivity(new Intent(requireContext(), ReservationActivity.class)));
        view.findViewById(R.id.btnDashboardReserved).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewReservedBook.class)));
        view.findViewById(R.id.btnDashboardOnline).setOnClickListener(v -> startActivity(new Intent(requireContext(), OnlineBooksActivity.class)));
        android.widget.TextView mode = view.findViewById(R.id.txtDashboardMode);
        mode.setText(FirebaseConfig.initialize(requireContext())
                ? R.string.firebase_mode_active : R.string.local_mode_active);
    }
}
