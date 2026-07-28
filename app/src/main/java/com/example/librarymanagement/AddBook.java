package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBook extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        EditText name = findViewById(R.id.txtbname);
        EditText author = findViewById(R.id.txtbauthor);
        EditText publisher = findViewById(R.id.txtbpublication);
        EditText quantity = findViewById(R.id.txtquntity);
        Button add = findViewById(R.id.btnadd);
        BookController controller = new BookController(this);

        add.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String a = author.getText().toString().trim();
            String p = publisher.getText().toString().trim();
            String q = quantity.getText().toString().trim();
            if (n.isEmpty() || a.isEmpty() || p.isEmpty() || q.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            int amount;
            try { amount = Integer.parseInt(q); }
            catch (NumberFormatException error) {
                quantity.setError("Enter a valid quantity");
                return;
            }
            add.setEnabled(false);
            controller.addBook(n, a, p, amount, (success, message) -> {
                add.setEnabled(true);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (success) {
                    name.setText(""); author.setText(""); publisher.setText(""); quantity.setText("");
                }
            });
        });
    }
}
