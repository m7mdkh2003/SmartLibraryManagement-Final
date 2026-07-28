package com.example.librarymanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class LocalBookAdapter extends RecyclerView.Adapter<LocalBookAdapter.ViewHolder> {
    public interface Listener { void onBookClick(BookModel book); }
    private final List<BookModel> items = new ArrayList<>();
    private final Listener listener;

    public LocalBookAdapter(Listener listener) { this.listener = listener; }

    public void submitList(List<BookModel> books) {
        items.clear();
        if (books != null) items.addAll(books);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.allbook, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel item = items.get(position);
        holder.name.setText(item.getName());
        holder.author.setText(item.getAuthor());
        holder.publisher.setText(item.getPublisher());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.available.setText(item.isAvailable() ? R.string.available : R.string.not_available);
        holder.itemView.setOnClickListener(v -> listener.onBookClick(item));
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, author, publisher, quantity, available;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtallbookcardtitle);
            author = itemView.findViewById(R.id.txtallbookcardauthor);
            publisher = itemView.findViewById(R.id.txtallbookcardpublisher);
            quantity = itemView.findViewById(R.id.txtallbookcardquantity);
            available = itemView.findViewById(R.id.txtcardavailable);
        }
    }
}
