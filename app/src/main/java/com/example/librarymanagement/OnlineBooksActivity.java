package com.example.librarymanagement;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.adapter.OnlineBookAdapter;
import com.example.librarymanagement.online.OnlineBooksViewModel;

public class OnlineBooksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_books);

        EditText searchInput = findViewById(R.id.txtOnlineSearch);
        Button searchButton = findViewById(R.id.btnOnlineSearch);
        ProgressBar progress = findViewById(R.id.onlineProgress);
        TextView emptyView = findViewById(R.id.txtOnlineEmpty);
        RecyclerView recyclerView = findViewById(R.id.recyclerOnlineBooks);

        OnlineBookAdapter adapter = new OnlineBookAdapter(book -> viewModel().saveFavorite(book));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        OnlineBooksViewModel viewModel = viewModel();
        viewModel.getBooks().observe(this, books -> {
            adapter.submitList(books);
            emptyView.setVisibility(books == null || books.isEmpty() ? View.VISIBLE : View.GONE);
        });
        viewModel.getLoading().observe(this, loading -> {
            boolean isLoading = Boolean.TRUE.equals(loading);
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            searchButton.setEnabled(!isLoading);
        });
        viewModel.getMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        View.OnClickListener searchAction = v -> viewModel.search(searchInput.getText().toString());
        searchButton.setOnClickListener(searchAction);
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAction.onClick(v);
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            searchInput.setText("Android programming");
            viewModel.search("Android programming");
        }
    }

    private OnlineBooksViewModel viewModel() {
        return new ViewModelProvider(this).get(OnlineBooksViewModel.class);
    }
}
