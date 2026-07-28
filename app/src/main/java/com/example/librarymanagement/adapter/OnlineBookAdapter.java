package com.example.librarymanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.OnlineBook;

import java.util.ArrayList;
import java.util.List;

public class OnlineBookAdapter extends RecyclerView.Adapter<OnlineBookAdapter.ViewHolder> {
    public interface Listener {
        void onSaveFavorite(OnlineBook book);
    }

    private final List<OnlineBook> items = new ArrayList<>();
    private final Listener listener;

    public OnlineBookAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<OnlineBook> books) {
        items.clear();
        if (books != null) items.addAll(books);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnlineBook item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.year.setText(holder.itemView.getContext().getString(R.string.publish_year_value, item.getFirstPublishYear()));
        holder.save.setOnClickListener(v -> listener.onSaveFavorite(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView author;
        final TextView year;
        final Button save;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtOnlineTitle);
            author = itemView.findViewById(R.id.txtOnlineAuthor);
            year = itemView.findViewById(R.id.txtOnlineYear);
            save = itemView.findViewById(R.id.btnSaveFavorite);
        }
    }
}
