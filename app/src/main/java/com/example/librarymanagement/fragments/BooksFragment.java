package com.example.librarymanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanagement.AddBook;
import com.example.librarymanagement.OnlineBooksActivity;
import com.example.librarymanagement.R;
import com.example.librarymanagement.ViewBook;

public class BooksFragment extends Fragment {
    public BooksFragment() { super(R.layout.fragment_books); }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnFragmentAddBook).setOnClickListener(v -> startActivity(new Intent(requireContext(), AddBook.class)));
        view.findViewById(R.id.btnFragmentViewBooks).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewBook.class)));
        view.findViewById(R.id.btnFragmentOnlineBooks).setOnClickListener(v -> startActivity(new Intent(requireContext(), OnlineBooksActivity.class)));
    }
}
