package com.example.group10.fragments.notesFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.Notes;
import com.example.group10.R;


import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private List<NotesModel> notesModelList;
    private NotesDatabaseHelper notesDatabaseHelper;
    private OnNoteListener onNoteListener;
    private Notes activity;
    OnNoteLongClick onNoteLongClick;

    public NotesAdapter(List<NotesModel> notesModelList, NotesDatabaseHelper notesDatabaseHelper, OnNoteListener onNoteListener, Notes activity, OnNoteLongClick onNoteLongClick) {
        this.notesDatabaseHelper = notesDatabaseHelper;
        this.notesModelList = notesModelList;
        this.onNoteListener = onNoteListener;
        this.activity = activity;
        this.onNoteLongClick = onNoteLongClick;
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        NotesModel item = notesModelList.get(position);
        notesDatabaseHelper.deleteTask(item.getId());
        notesModelList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noteslist_items, parent, false);
        return new ViewHolder(itemView, onNoteListener, onNoteLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.noteItemTitle.setText(notesModelList.get(position).getTitle());

    }



    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList() {
        notesModelList.clear();
        notesModelList = notesDatabaseHelper.getAllTasks();
        notifyDataSetChanged();
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }

    public interface OnNoteLongClick{
        void OnNoteLongClick(int position);
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView noteItemTitle;
        ImageView favorNote;
        OnNoteListener onNoteListener;
        OnNoteLongClick onNoteLongClick;

        ViewHolder(View view, OnNoteListener onNoteListener, OnNoteLongClick onNoteLongClick){
            super(view);
            noteItemTitle = view.findViewById(R.id.noteItems_title);
            favorNote = view.findViewById(R.id.favorNote);
            favorNote.setVisibility(view.INVISIBLE);
            this.onNoteListener = onNoteListener;
            this.onNoteLongClick = onNoteLongClick;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            favorNote.setVisibility(view.VISIBLE);
            onNoteLongClick.OnNoteLongClick(getAdapterPosition());
            return true;
        }
    }
}
