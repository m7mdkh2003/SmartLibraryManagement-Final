package com.example.librarymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.members.MemberContract;
import com.example.librarymanagement.members.MemberPresenter;
import com.example.librarymanagement.members.MemberRepository;
import com.example.librarymanagement.model.MemberModel;

import java.util.ArrayList;
import java.util.List;

/** MVP View implementation for the Members module. */
public class ViewMember extends AppCompatActivity implements MemberContract.View {
    private final List<MemberModel> items = new ArrayList<>();
    private MemberAdapter adapter;
    private MemberPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewviewmember);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberAdapter(items);
        recyclerView.setAdapter(adapter);
        presenter = new MemberPresenter(this, new MemberRepository(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadMembers();
    }

    @Override
    public void showLoading() {
        findViewById(R.id.recyclerViewviewmember).setAlpha(0.5f);
    }

    @Override
    public void hideLoading() {
        findViewById(R.id.recyclerViewviewmember).setAlpha(1f);
    }

    @Override
    public void showMembers(List<MemberModel> members) {
        items.clear();
        items.addAll(members);
        adapter.notifyDataSetChanged();
        if (members.isEmpty()) Toast.makeText(this, "No members found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    private class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
        private final List<MemberModel> members;

        MemberAdapter(List<MemberModel> members) {
            this.members = members;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.allmember, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MemberModel member = members.get(position);
            holder.name.setText(member.getName());
            holder.email.setText(member.getEmail());
            holder.phone.setText(member.getPhone());
            holder.address.setText(member.getAddress());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ViewMember.this, EditMember.class);
                intent.putExtra("memberName", member.getName());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return members.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView name, email, phone, address;
            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.txt_member_name);
                email = itemView.findViewById(R.id.txt_member_email);
                phone = itemView.findViewById(R.id.txt_member_phone);
                address = itemView.findViewById(R.id.txt_member_address);
            }
        }
    }
}
