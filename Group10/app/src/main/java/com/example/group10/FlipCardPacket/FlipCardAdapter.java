package com.example.group10.FlipCardPacket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FlipCardAdapter extends RecyclerView.Adapter<FlipCardAdapter.ViewHolder> {
    ArrayList<FlipCardModel> list;
    FlipCardDatabaseHelper db;
    OnItemClickListener onItemClickListener;
    AddToFavor addToFavor;

    public FlipCardAdapter(FlipCardDatabaseHelper db, OnItemClickListener onItemClickListener, AddToFavor addToFavor) {
        this.db = db;
        list = db.getAllTasks();
        this.onItemClickListener = onItemClickListener;
        this.addToFavor = addToFavor;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteslist_items, parent, false);
        return new FlipCardAdapter.ViewHolder(itemView, onItemClickListener, db, addToFavor);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void UpdateList() {
        list.clear();
        list = db.getAllTasks();
        notifyDataSetChanged();
    }

    public ArrayList<FlipCardModel> getList() {
        return list;
    }

    public interface OnItemClickListener{
        void onItemClick(int id,String title, String fc_front, String fc_back);
    }

    public interface AddToFavor{
        void addFavor(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView title;
        ImageView star;
        OnItemClickListener onItemClickListener;
        ArrayList<FlipCardModel> list;
        FlipCardDatabaseHelper db;
        AddToFavor addToFavor;

        ViewHolder(View view, OnItemClickListener onItemClickListener, FlipCardDatabaseHelper db, AddToFavor addToFavor) {
            super(view);
            title = view.findViewById(R.id.noteItems_title);
            star = view.findViewById(R.id.favorNote);
            star.setVisibility(View.INVISIBLE);
            this.db = db;
            this.addToFavor = addToFavor;
            this.list = db.getAllTasks();
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //update list and pass flip card data to interface
            list.clear();
            list = db.getAllTasks();
            int id = list.get(getAdapterPosition()).getId();
            String front = list.get(getAdapterPosition()).getFront();
            String back = list.get(getAdapterPosition()).getBack();
            String title = list.get(getAdapterPosition()).getTitle();
            onItemClickListener.onItemClick(id, title, front, back);
        }

        @Override
        public boolean onLongClick(View view) {
            addToFavor.addFavor(getAdapterPosition());
            star.setVisibility(View.VISIBLE);
            return true;
        }
    }
}
