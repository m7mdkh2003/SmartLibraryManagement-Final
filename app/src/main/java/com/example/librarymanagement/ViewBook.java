package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.adapter.LocalBookAdapter;

public class ViewBook extends AppCompatActivity {
    private LocalBookAdapter adapter;
    private BookController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        controller = new BookController(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewviewbook);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocalBookAdapter(book -> {
            Intent intent = new Intent(this, EditBook.class);
            intent.putExtra("bookName", book.getName());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        findViewById(R.id.btnhome).setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
        findViewById(R.id.btnsearch).setOnClickListener(v -> startActivity(new Intent(this, OnlineBooksActivity.class)));
        findViewById(R.id.btnnotification).setOnClickListener(v -> startActivity(new Intent(this, ViewReservedBook.class)));
        findViewById(R.id.btnstore).setOnClickListener(v -> refresh());
    }

    @Override protected void onResume() { super.onResume(); refresh(); }

    private void refresh() {
        controller.loadBooks(new BookController.BooksCallback() {
            @Override public void onLoaded(java.util.List<com.example.librarymanagement.model.BookModel> books) {
                adapter.submitList(books);
                if (books.isEmpty()) Toast.makeText(ViewBook.this, "No books found", Toast.LENGTH_SHORT).show();
            }
            @Override public void onError(String message) { Toast.makeText(ViewBook.this, message, Toast.LENGTH_SHORT).show(); }
        });
    }
}
