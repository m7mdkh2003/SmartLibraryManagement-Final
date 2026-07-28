package com.example.librarymanagement;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.model.BookModel;
import com.example.librarymanagement.model.MemberModel;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    private AutoCompleteTextView searchInput;
    private TextView details;
    private Spinner memberSpinner;
    private Button reserveButton;
    private ArrayAdapter<String> bookAdapter;
    private BookController bookController;
    private BookModel selectedBook;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        searchInput=findViewById(R.id.searchBookAutoComplete); details=findViewById(R.id.bookDetailsTextView);
        memberSpinner=findViewById(R.id.memberSpinner); reserveButton=findViewById(R.id.reserveButton);
        bookController=new BookController(this);
        bookAdapter=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,new ArrayList<>());
        searchInput.setAdapter(bookAdapter); searchInput.setThreshold(1);
        loadMembers();
        searchInput.setOnItemClickListener((parent,view,position,id)->loadSelectedBook((String)parent.getItemAtPosition(position)));
        searchInput.addTextChangedListener(new TextWatcher(){
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            @Override public void onTextChanged(CharSequence s,int start,int before,int count){ selectedBook=null; fetchSuggestions(s.toString()); }
            @Override public void afterTextChanged(Editable s){}
        });
        reserveButton.setOnClickListener(v->reserve());
    }

    private void loadMembers(){
        AppExecutors.getInstance().diskIO().execute(()->{
            List<String> names=new ArrayList<>();
            try(DbHelper helper=new DbHelper(this)){for(MemberModel member:helper.getAllMembersList())names.add(member.getName());}
            AppExecutors.getInstance().runOnMainThread(()->{
                ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);memberSpinner.setAdapter(adapter);
                if(names.isEmpty())Toast.makeText(this,"Add a member before reserving a book",Toast.LENGTH_LONG).show();
            });
        });
    }

    private void fetchSuggestions(String query){
        if(query.trim().isEmpty()){ AppExecutors.getInstance().runOnMainThread(() -> { bookAdapter.clear(); bookAdapter.notifyDataSetChanged(); }); return; }
        AppExecutors.getInstance().diskIO().execute(()->{
            List<String> suggestions;try(DbHelper helper=new DbHelper(this)){suggestions=helper.getBookSuggestions(query);}
            AppExecutors.getInstance().runOnMainThread(()->{bookAdapter.clear();bookAdapter.addAll(suggestions);bookAdapter.notifyDataSetChanged();});
        });
    }

    private void loadSelectedBook(String name){
        bookController.getBook(name,new BookController.BookCallback(){
            @Override public void onLoaded(BookModel book){selectedBook=book;details.setText(getString(R.string.book_details_format,book.getName(),book.getAuthor(),book.getPublisher(),book.getQuantity()));}
            @Override public void onError(String message){Toast.makeText(ReservationActivity.this,message,Toast.LENGTH_SHORT).show();}
        });
    }

    private void reserve(){
        if(memberSpinner.getSelectedItem()==null){Toast.makeText(this,"Please add and select a member",Toast.LENGTH_SHORT).show();return;}
        String typed=searchInput.getText().toString().trim();
        if(typed.isEmpty()){searchInput.setError("Select a book");return;}
        if(selectedBook==null||!selectedBook.getName().equals(typed)){loadSelectedBookAndReserve(typed);return;}
        performReservation(selectedBook);
    }

    private void loadSelectedBookAndReserve(String name){
        bookController.getBook(name,new BookController.BookCallback(){
            @Override public void onLoaded(BookModel book){selectedBook=book;performReservation(book);}
            @Override public void onError(String message){Toast.makeText(ReservationActivity.this,message,Toast.LENGTH_SHORT).show();}
        });
    }

    private void performReservation(BookModel book){
        String member=String.valueOf(memberSpinner.getSelectedItem());reserveButton.setEnabled(false);
        AppExecutors.getInstance().diskIO().execute(()->{
            boolean success;try(DbHelper helper=new DbHelper(this)){success=helper.reserveBook(book.getId(),member);}
            AppExecutors.getInstance().runOnMainThread(()->{
                reserveButton.setEnabled(true);Toast.makeText(this,success?"Book reserved successfully":"Book is not available",Toast.LENGTH_SHORT).show();
                if(success){selectedBook=null;searchInput.setText("");details.setText("");}
            });
        });
    }
}
