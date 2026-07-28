package com.example.librarymanagement.online;

import android.content.Context;
import android.net.Uri;

import com.example.librarymanagement.DbHelper;
import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.firebase.FirebaseConfig;
import com.example.librarymanagement.model.OnlineBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Web Service repository. Uses Open Library Search API and Firestore/local favorites. */
public class OnlineBookRepository {
    public interface SearchCallback {
        void onResult(List<OnlineBook> books);
        void onError(String message);
    }

    public interface SaveCallback {
        void onSaved(boolean firebaseUsed);
        void onError(String message);
    }

    private final Context appContext;

    public OnlineBookRepository(Context context) {
        appContext = context.getApplicationContext();
    }

    public void search(String query, SearchCallback callback) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            HttpURLConnection connection = null;
            try {
                String encoded = Uri.encode(query);
                URL url = new URL("https://openlibrary.org/search.json?q=" + encoded
                        + "&limit=25&fields=key,title,author_name,first_publish_year");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(12000);
                connection.setReadTimeout(12000);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "SmartLibraryAndroid/2.0");

                int code = connection.getResponseCode();
                InputStream stream = code >= 200 && code < 300
                        ? connection.getInputStream() : connection.getErrorStream();
                String response = readStream(stream);
                if (code < 200 || code >= 300) {
                    throw new IllegalStateException("Server returned HTTP " + code);
                }

                JSONObject root = new JSONObject(response);
                JSONArray docs = root.optJSONArray("docs");
                List<OnlineBook> result = new ArrayList<>();
                if (docs != null) {
                    for (int i = 0; i < docs.length(); i++) {
                        JSONObject doc = docs.optJSONObject(i);
                        if (doc == null) continue;
                        String key = doc.optString("key", "book-" + i);
                        String title = doc.optString("title", "Untitled");
                        JSONArray authors = doc.optJSONArray("author_name");
                        String author = authors != null && authors.length() > 0
                                ? authors.optString(0, "Unknown author") : "Unknown author";
                        String year = doc.has("first_publish_year")
                                ? String.valueOf(doc.optInt("first_publish_year")) : "Unknown year";
                        result.add(new OnlineBook(key, title, author, year));
                    }
                }
                AppExecutors.getInstance().runOnMainThread(() -> callback.onResult(result));
            } catch (Exception error) {
                String message = error.getMessage() == null ? "Unable to load online books" : error.getMessage();
                AppExecutors.getInstance().runOnMainThread(() -> callback.onError(message));
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    public void saveFavorite(OnlineBook book, SaveCallback callback) {
        if (FirebaseConfig.initialize(appContext)) {
            String userDocument = FirebaseAuth.getInstance().getCurrentUser() == null
                    ? "anonymous" : FirebaseAuth.getInstance().getCurrentUser().getUid();
            String bookDocument = book.getKey().replaceAll("[^A-Za-z0-9_-]", "_");
            Map<String, Object> values = new HashMap<>();
            values.put("key", book.getKey());
            values.put("title", book.getTitle());
            values.put("author", book.getAuthor());
            values.put("firstPublishYear", book.getFirstPublishYear());
            FirebaseFirestore.getInstance().collection("favorites")
                    .document(userDocument).collection("books").document(bookDocument)
                    .set(values)
                    .addOnSuccessListener(unused -> callback.onSaved(true))
                    .addOnFailureListener(error -> callback.onError(error.getMessage() == null
                            ? "Unable to save Firebase favorite" : error.getMessage()));
            return;
        }

        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean saved;
            try (DbHelper helper = new DbHelper(appContext)) {
                saved = helper.saveFavorite(book);
            }
            boolean finalSaved = saved;
            AppExecutors.getInstance().runOnMainThread(() -> {
                if (finalSaved) callback.onSaved(false);
                else callback.onError("Unable to save favorite");
            });
        });
    }

    private String readStream(InputStream stream) throws Exception {
        if (stream == null) return "";
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) builder.append(line);
        }
        return builder.toString();
    }
}
