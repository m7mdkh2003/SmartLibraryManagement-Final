package com.example.librarymanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.librarymanagement.R;
import com.example.librarymanagement.model.ReservedBookModel;

import java.util.ArrayList;
import java.util.List;

/** Custom Adapter for ListView, included explicitly for the course requirement. */
public class ReservedBookListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<ReservedBookModel> items = new ArrayList<>();

    public ReservedBookListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void submitList(List<ReservedBookModel> values) {
        items.clear();
        if (values != null) items.addAll(values);
        notifyDataSetChanged();
    }

    public ReservedBookModel getBook(int position) {
        return items.get(position);
    }

    @Override public int getCount() { return items.size(); }
    @Override public Object getItem(int position) { return items.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.reserved_book_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReservedBookModel item = items.get(position);
        holder.name.setText(item.getName());
        holder.author.setText(item.getAuthor());
        holder.publisher.setText(item.getPublisher());
        holder.member.setText(item.getReservedBy());
        return convertView;
    }

    private static class ViewHolder {
        final TextView name, author, publisher, member;
        ViewHolder(View view) {
            name = view.findViewById(R.id.txtReservedBookName);
            author = view.findViewById(R.id.txtReservedBookAuthor);
            publisher = view.findViewById(R.id.txtReservedBookPublisher);
            member = view.findViewById(R.id.txtReservedBy);
        }
    }
}
