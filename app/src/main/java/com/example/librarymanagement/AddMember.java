package com.example.librarymanagement;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.core.AppExecutors;

public class AddMember extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        EditText name = findViewById(R.id.txtmname);
        EditText email = findViewById(R.id.txtmemail);
        EditText phone = findViewById(R.id.txtmphone);
        EditText address = findViewById(R.id.txtmaddress);
        Button add = findViewById(R.id.btnaddmember);
        add.setOnClickListener(v -> {
            String n=name.getText().toString().trim(), e=email.getText().toString().trim(),
                    p=phone.getText().toString().trim(), a=address.getText().toString().trim();
            if (n.isEmpty()||e.isEmpty()||p.isEmpty()||a.isEmpty()) { Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_SHORT).show(); return; }
            if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) { email.setError("Invalid email"); return; }
            add.setEnabled(false);
            AppExecutors.getInstance().diskIO().execute(() -> {
                boolean success;
                try (DbHelper helper = new DbHelper(this)) { success = helper.addMember(n,e,p,a); }
                boolean result=success;
                AppExecutors.getInstance().runOnMainThread(() -> {
                    add.setEnabled(true); Toast.makeText(this,result?"Member added successfully":"Unable to add member",Toast.LENGTH_SHORT).show();
                    if(result){ name.setText("");email.setText("");phone.setText("");address.setText(""); }
                });
            });
        });
    }
}
