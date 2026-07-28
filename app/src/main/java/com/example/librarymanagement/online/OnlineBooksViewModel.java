package com.example.librarymanagement.online;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.librarymanagement.model.OnlineBook;

import java.util.Collections;
import java.util.List;

/** MVVM ViewModel for the online-books screen. */
public class OnlineBooksViewModel extends AndroidViewModel {
    private final OnlineBookRepository repository;
    private final MutableLiveData<List<OnlineBook>> books = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public OnlineBooksViewModel(@NonNull Application application) {
        super(application);
        repository = new OnlineBookRepository(application);
    }

    public LiveData<List<OnlineBook>> getBooks() { return books; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }

    public void search(String query) {
        if (query == null || query.trim().length() < 2) {
            message.setValue("Enter at least two characters");
            return;
        }
        loading.setValue(true);
        repository.search(query.trim(), new OnlineBookRepository.SearchCallback() {
            @Override
            public void onResult(List<OnlineBook> result) {
                loading.setValue(false);
                books.setValue(result);
                if (result.isEmpty()) message.setValue("No online books found");
            }

            @Override
            public void onError(String error) {
                loading.setValue(false);
                message.setValue(error);
            }
        });
    }

    public void saveFavorite(OnlineBook book) {
        repository.saveFavorite(book, new OnlineBookRepository.SaveCallback() {
            @Override
            public void onSaved(boolean firebaseUsed) {
                message.setValue(firebaseUsed
                        ? "Saved to Firebase favorites" : "Saved to local SQLite favorites");
            }

            @Override
            public void onError(String error) {
                message.setValue(error);
            }
        });
    }
}
