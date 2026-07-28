package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.core.AppExecutors;

public class BookDetailsActivity extends AppCompatActivity {
    private String bookName;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        bookName=getIntent().getStringExtra("bookName");
        ((TextView)findViewById(R.id.txtBookName)).setText(bookName);
        ((TextView)findViewById(R.id.txtBookAuthor)).setText(getIntent().getStringExtra("bookAuthor"));
        ((TextView)findViewById(R.id.txtBookPublisher)).setText(getIntent().getStringExtra("bookPublisher"));
        ((TextView)findViewById(R.id.txtMemberName)).setText(getIntent().getStringExtra("member"));
        Button button=findViewById(R.id.btnReturnBook);
        button.setOnClickListener(v -> {
            button.setEnabled(false);
            AppExecutors.getInstance().diskIO().execute(() -> {
                boolean success;
                try(DbHelper helper=new DbHelper(this)){ success=helper.returnBook(bookName); }
                boolean result=success;
                AppExecutors.getInstance().runOnMainThread(() -> {
                    Toast.makeText(this,result?"Book returned successfully":"Reservation not found",Toast.LENGTH_SHORT).show();
                    if(result) finish(); else button.setEnabled(true);
                });
            });
        });
    }
}
