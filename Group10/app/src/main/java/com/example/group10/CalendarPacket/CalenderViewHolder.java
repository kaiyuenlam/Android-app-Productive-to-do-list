package com.example.group10.CalendarPacket;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import com.example.group10.circularProgressbar.CircleProgressBar;

public class CalenderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView dayOfMonth;
    public CircleProgressBar circleProgressBar;
    private final CalendarAdapter.OnItemListener onItemListener;
    String month;

    public CalenderViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, String month)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        circleProgressBar = itemView.findViewById(R.id.circleProgressBarI);
        this.month = month;
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick((String) dayOfMonth.getText());
    }
}
