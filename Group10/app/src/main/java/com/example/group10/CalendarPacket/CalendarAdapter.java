package com.example.group10.CalendarPacket;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalenderViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private String month;
    CalendarDatabaseModel calendarDatabaseModel;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, String month, CalendarDatabaseModel calendarDatabaseModel) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.month = month;
        this.calendarDatabaseModel = calendarDatabaseModel;
    }

    @NonNull
    @Override
    public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalenderViewHolder(view, onItemListener, month);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        if ("".equals(daysOfMonth.get(position))) {
            holder.circleProgressBar.setVisibility(View.INVISIBLE);
        } else {
            //model = db.getTheDay(month, daysOfMonth.get(position));
            holder.circleProgressBar.setProgress(0);
        }
        if (daysOfMonth.get(position).equals(calendarDatabaseModel.getDate())) {
            holder.circleProgressBar.setProgress(calendarDatabaseModel.getFinished());
        }
    }


    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {

        void onItemClick(String dayText);

    }
}

