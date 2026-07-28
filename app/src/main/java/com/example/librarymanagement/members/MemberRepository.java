package com.example.librarymanagement.members;

import android.content.Context;

import com.example.librarymanagement.DbHelper;
import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.model.MemberModel;

import java.util.List;

public class MemberRepository {
    public interface Callback {
        void onSuccess(List<MemberModel> members);
        void onError(String message);
    }

    private final Context appContext;

    public MemberRepository(Context context) {
        appContext = context.getApplicationContext();
    }

    public void loadMembers(Callback callback) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try (DbHelper helper = new DbHelper(appContext)) {
                List<MemberModel> members = helper.getAllMembersList();
                AppExecutors.getInstance().runOnMainThread(() -> callback.onSuccess(members));
            } catch (Exception error) {
                AppExecutors.getInstance().runOnMainThread(() -> callback.onError("Unable to load members"));
            }
        });
    }
}
