package com.example.librarymanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanagement.R;
import com.example.librarymanagement.ReservationActivity;
import com.example.librarymanagement.ViewReservedBook;

public class ReservationsFragment extends Fragment {
    public ReservationsFragment() { super(R.layout.fragment_reservations); }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnFragmentReserveBook).setOnClickListener(v -> startActivity(new Intent(requireContext(), ReservationActivity.class)));
        view.findViewById(R.id.btnFragmentViewReserved).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewReservedBook.class)));
    }
}
