package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.model.BookModel;

public class EditBook extends AppCompatActivity {
    private EditText name, author, publisher, quantity;
    private TextView idView;
    private Switch available;
    private BookController controller;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        name = findViewById(R.id.txtupdatebookname);
        author = findViewById(R.id.txtupdatebookauthor);
        publisher = findViewById(R.id.txtupdatepublication);
        quantity = findViewById(R.id.txtupdatequantity);
        idView = findViewById(R.id.txtbookid);
        available = findViewById(R.id.switchavailability);
        Button update = findViewById(R.id.btnupdate);
        Button delete = findViewById(R.id.btndelete);
        controller = new BookController(this);

        String bookName = getIntent().getStringExtra("bookName");
        controller.getBook(bookName, new BookController.BookCallback() {
            @Override public void onLoaded(BookModel book) {
                bookId = book.getId();
                idView.setText(String.valueOf(book.getId()));
                name.setText(book.getName());
                author.setText(book.getAuthor());
                publisher.setText(book.getPublisher());
                quantity.setText(String.valueOf(book.getQuantity()));
                available.setChecked(book.isAvailable());
            }
            @Override public void onError(String message) { Toast.makeText(EditBook.this, message, Toast.LENGTH_SHORT).show(); finish(); }
        });

        update.setOnClickListener(v -> {
            String q = quantity.getText().toString().trim();
            if (name.getText().toString().trim().isEmpty() || author.getText().toString().trim().isEmpty()
                    || publisher.getText().toString().trim().isEmpty() || q.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show(); return;
            }
            int amount;
            try { amount = Integer.parseInt(q); } catch (NumberFormatException e) { quantity.setError("Invalid quantity"); return; }
            BookModel model = new BookModel(bookId, name.getText().toString().trim(), author.getText().toString().trim(),
                    publisher.getText().toString().trim(), amount, available.isChecked());
            controller.updateBook(model, (success, message) -> { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); if (success) finish(); });
        });
        delete.setOnClickListener(v -> controller.deleteBook(bookId,
                (success, message) -> { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); if (success) finish(); }));
    }
}
