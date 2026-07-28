package com.example.librarymanagement.members;

import com.example.librarymanagement.model.MemberModel;

import java.util.List;

/** MVP contract for the Members module. */
public interface MemberContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showMembers(List<MemberModel> members);
        void showError(String message);
    }

    interface Presenter {
        void loadMembers();
        void detach();
    }
}
