package com.example.librarymanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanagement.AddMember;
import com.example.librarymanagement.R;
import com.example.librarymanagement.ViewMember;

public class MembersFragment extends Fragment {
    public MembersFragment() { super(R.layout.fragment_members); }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btnFragmentAddMember).setOnClickListener(v -> startActivity(new Intent(requireContext(), AddMember.class)));
        view.findViewById(R.id.btnFragmentViewMembers).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewMember.class)));
    }
}
