package com.example.librarymanagement.auth;

import android.content.Context;

import com.example.librarymanagement.DbHelper;
import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.firebase.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/** Repository Pattern: chooses Firebase Authentication when configured and SQLite otherwise. */
public class AuthRepository {
    public interface AuthCallback {
        void onSuccess(String email, boolean firebaseUsed);
        void onError(String message);
    }

    private final Context appContext;

    public AuthRepository(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public void login(String email, String password, AuthCallback callback) {
        if (FirebaseConfig.initialize(appContext)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> callback.onSuccess(email, true))
                    .addOnFailureListener(error -> callback.onError(error.getMessage() == null
                            ? "Firebase login failed" : error.getMessage()));
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean valid;
            try (DbHelper dbHelper = new DbHelper(appContext)) {
                valid = dbHelper.loginUser(email, password);
            }
            boolean finalValid = valid;
            AppExecutors.getInstance().runOnMainThread(() -> {
                if (finalValid) {
                    callback.onSuccess(email, false);
                } else {
                    callback.onError("Invalid email or password");
                }
            });
        });
    }

    public void register(String name, String email, String password, AuthCallback callback) {
        if (FirebaseConfig.initialize(appContext)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        String uid = result.getUser() == null ? email : result.getUser().getUid();
                        Map<String, Object> profile = new HashMap<>();
                        profile.put("name", name);
                        profile.put("email", email);
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .set(profile)
                                .addOnCompleteListener(task -> callback.onSuccess(email, true));
                    })
                    .addOnFailureListener(error -> callback.onError(error.getMessage() == null
                            ? "Firebase registration failed" : error.getMessage()));
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean inserted;
            try (DbHelper dbHelper = new DbHelper(appContext)) {
                inserted = dbHelper.registerUser(name, email, password, password);
            }
            boolean finalInserted = inserted;
            AppExecutors.getInstance().runOnMainThread(() -> {
                if (finalInserted) {
                    callback.onSuccess(email, false);
                } else {
                    callback.onError("Email is already registered");
                }
            });
        });
    }

    public void sendPasswordReset(String email, AuthCallback callback) {
        if (!FirebaseConfig.initialize(appContext)) {
            callback.onError("Password reset requires Firebase configuration");
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> callback.onSuccess(email, true))
                .addOnFailureListener(error -> callback.onError(error.getMessage() == null
                        ? "Unable to send reset email" : error.getMessage()));
    }

    public void logout() {
        if (FirebaseConfig.initialize(appContext)) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
