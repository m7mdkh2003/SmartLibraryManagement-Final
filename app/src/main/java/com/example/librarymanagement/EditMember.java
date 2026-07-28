package com.example.librarymanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagement.core.AppExecutors;
import com.example.librarymanagement.model.MemberModel;

public class EditMember extends AppCompatActivity {
    private EditText name, email, phone, address;
    private TextView idView;
    private int memberId;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        name=findViewById(R.id.txtupdatemembername); email=findViewById(R.id.txtupdatememberemail);
        phone=findViewById(R.id.txtupdatememberphone); address=findViewById(R.id.txtupdatememberaddress);
        idView=findViewById(R.id.txtmemberid);
        Button update=findViewById(R.id.btnupdatemember), delete=findViewById(R.id.btndeletemember);
        load(getIntent().getStringExtra("memberName"));
        update.setOnClickListener(v -> update());
        delete.setOnClickListener(v -> delete());
    }

    private void load(String memberName) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            MemberModel model=null;
            try(DbHelper helper=new DbHelper(this); Cursor cursor=helper.getMemberByName(memberName)){
                if(cursor.moveToFirst()) model=new MemberModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.getMemberDetails("MEMBER_ID"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getMemberDetails("MEMBER_NAME"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getMemberDetails("MEMBER_EMAIL"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getMemberDetails("MEMBER_PHONE"))),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.getMemberDetails("MEMBER_ADDRESS"))));
            }
            MemberModel result=model;
            AppExecutors.getInstance().runOnMainThread(() -> {
                if(result==null){ Toast.makeText(this,"Member not found",Toast.LENGTH_SHORT).show(); finish(); return; }
                memberId=result.getId(); idView.setText(String.valueOf(memberId)); name.setText(result.getName());
                email.setText(result.getEmail()); phone.setText(result.getPhone()); address.setText(result.getAddress());
            });
        });
    }

    private void update() {
        String n=name.getText().toString().trim(), e=email.getText().toString().trim(),
                p=phone.getText().toString().trim(), a=address.getText().toString().trim();
        if(n.isEmpty()||e.isEmpty()||p.isEmpty()||a.isEmpty()){ Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show(); return; }
        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){ email.setError("Invalid email"); return; }
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean success; try(DbHelper helper=new DbHelper(this)){success=helper.updateMember(memberId,n,e,p,a);}
            AppExecutors.getInstance().runOnMainThread(() -> {Toast.makeText(this,success?"Member updated successfully":"Unable to update member",Toast.LENGTH_SHORT).show();if(success)finish();});
        });
    }

    private void delete() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            boolean success; try(DbHelper helper=new DbHelper(this)){success=helper.deleteMember(memberId);}
            AppExecutors.getInstance().runOnMainThread(() -> {Toast.makeText(this,success?"Member deleted successfully":"Unable to delete member",Toast.LENGTH_SHORT).show();if(success)finish();});
        });
    }
}
