package com.example.group10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyFavorAdapter extends RecyclerView.Adapter<MyFavorAdapter.ViewHolder> {
    ArrayList<MyFavorModel> myFavorList;
    OnItemClick onItemClick;

    public MyFavorAdapter(ArrayList<MyFavorModel> myFavorList, OnItemClick onItemClick) {
        this.myFavorList = myFavorList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteslist_items, parent, false);
        return new ViewHolder(itemView, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.title.setText(myFavorList.get(position).getTitle());
    }

    public interface OnItemClick {
        void open(int position);
    }

    @Override
    public int getItemCount() {
        return myFavorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView star;
        OnItemClick onItemClick;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            title = itemView.findViewById(R.id.noteItems_title);
            star = itemView.findViewById(R.id.favorNote);
            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick.open(getAdapterPosition());
        }
    }
}
