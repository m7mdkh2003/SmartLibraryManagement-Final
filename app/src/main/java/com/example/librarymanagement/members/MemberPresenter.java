package com.example.librarymanagement.members;

import com.example.librarymanagement.model.MemberModel;

import java.util.List;

public class MemberPresenter implements MemberContract.Presenter {
    private MemberContract.View view;
    private final MemberRepository repository;

    public MemberPresenter(MemberContract.View view, MemberRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadMembers() {
        if (view == null) return;
        view.showLoading();
        repository.loadMembers(new MemberRepository.Callback() {
            @Override
            public void onSuccess(List<MemberModel> members) {
                if (view == null) return;
                view.hideLoading();
                view.showMembers(members);
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.hideLoading();
                view.showError(message);
            }
        });
    }

    @Override
    public void detach() {
        view = null;
    }
}
