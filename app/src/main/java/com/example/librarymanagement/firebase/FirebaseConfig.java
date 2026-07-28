package com.example.librarymanagement.firebase;

import android.content.Context;

import com.example.librarymanagement.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Initializes Firebase without the Google Services Gradle plugin.
 * Replace the three placeholder values in strings.xml to enable real Firebase.
 */
public final class FirebaseConfig {
    private FirebaseConfig() {
    }

    public static boolean isConfigured(Context context) {
        String apiKey = context.getString(R.string.firebase_web_api_key).trim();
        String appId = context.getString(R.string.firebase_application_id).trim();
        String projectId = context.getString(R.string.firebase_project_id).trim();
        return !isPlaceholder(apiKey) && !isPlaceholder(appId) && !isPlaceholder(projectId);
    }

    public static boolean initialize(Context context) {
        if (!isConfigured(context)) {
            return false;
        }
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setApiKey(context.getString(R.string.firebase_web_api_key).trim())
                        .setApplicationId(context.getString(R.string.firebase_application_id).trim())
                        .setProjectId(context.getString(R.string.firebase_project_id).trim())
                        .build();
                FirebaseApp.initializeApp(context, options);
            }
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static boolean isPlaceholder(String value) {
        return value.isEmpty() || value.startsWith("REPLACE_");
    }
}
