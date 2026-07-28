package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.adapter.ReservedBookListAdapter;
import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.model.ReservedBookModel;

import java.util.List;

/** ListView + Custom BaseAdapter screen. */
public class ViewReservedBook extends AppCompatActivity {
    private ReservedBookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reserved_book);
        ListView listView = findViewById(R.id.listViewReservedBook);
        adapter = new ReservedBookListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ReservedBookModel item = adapter.getBook(position);
            Intent intent = new Intent(this, BookDetailsActivity.class);
            intent.putExtra("bookName", item.getName());
            intent.putExtra("bookAuthor", item.getAuthor());
            intent.putExtra("bookPublisher", item.getPublisher());
            intent.putExtra("member", item.getReservedBy());
            startActivity(intent);
        });
        findViewById(R.id.btnhome).setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
        findViewById(R.id.btnsearch).setOnClickListener(v -> startActivity(new Intent(this, OnlineBooksActivity.class)));
        findViewById(R.id.btnnotification).setOnClickListener(v -> loadBooks());
        findViewById(R.id.btnstore).setOnClickListener(v -> startActivity(new Intent(this, ViewBook.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            try (DbHelper helper = new DbHelper(this)) {
                List<ReservedBookModel> books = helper.getAllReservedBooksList();
                AppExecutors.getInstance().runOnMainThread(() -> {
                    adapter.submitList(books);
                    if (books.isEmpty()) Toast.makeText(this, "No reserved books", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
