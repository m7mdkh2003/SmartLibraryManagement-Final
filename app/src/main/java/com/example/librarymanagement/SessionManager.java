package com.example.librarymanagement;

import android.content.Context;
import android.content.SharedPreferences;

/** Shared Preferences manager for Remember Me and the current user email. */
public class SessionManager {
    private static final String PREF_NAME = "library_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_EMAIL = "saved_email";
    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginSession(String email) { saveLoginSession(email, true); }

    public void saveLoginSession(String email, boolean rememberMe) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, rememberMe).putString(KEY_EMAIL, email).apply();
    }

    public boolean isLoggedIn() { return preferences.getBoolean(KEY_IS_LOGGED_IN, false); }
    public String getSavedEmail() { return preferences.getString(KEY_EMAIL, ""); }

    public void clearSession() { preferences.edit().clear().apply(); }
}
