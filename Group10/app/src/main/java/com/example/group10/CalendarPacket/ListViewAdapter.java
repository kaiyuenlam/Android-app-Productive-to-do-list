package com.example.group10.CalendarPacket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import com.example.group10.todolist.ToDoModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private List<ToDoModel> list;

    public void setList(List<ToDoModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getTask());
        if (list.get(position).getStatus() == 1) {
            holder.notDone.setVisibility(View.INVISIBLE);
        } else {
            holder.done.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        int show;
        if (list == null) {
            show = 0;
        } else {
            show = list.size();
        }
        return show;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView done, notDone;
        TextView textView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTitle);
            done = itemView.findViewById(R.id.done);
            notDone = itemView.findViewById(R.id.notDone);
        }
    }
}
