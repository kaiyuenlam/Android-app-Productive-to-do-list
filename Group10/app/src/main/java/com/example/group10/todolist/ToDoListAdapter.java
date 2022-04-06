package com.example.group10.todolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.MainActivity;
import com.example.group10.R;
import com.example.group10.circularProgressbar.CircleProgressBar;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHelper db;
    private CircleProgressBar circleProgressBar;
    private TextView progressbarPercentage;
    private int finished;

    private int finishedTask = 0;
    private int totalTask;

    public ToDoListAdapter(TextView progressbarPercentage, CircleProgressBar circleProgressBar, DatabaseHelper db, MainActivity activity, List<ToDoModel> todoList){
        this.db = db;
        this.activity = activity;
        this.todoList = todoList;
        this.circleProgressBar = circleProgressBar;
        this.totalTask = todoList.size();
        this.progressbarPercentage = progressbarPercentage;
    }

    //return view Holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todolist_item, parent, false);
        return new ViewHolder(itemView);
    }

    //Bind view-holder and create view
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position){
        holder.task.setText(todoList.get(position).getTask());
        holder.task.setChecked(toBoolean(todoList.get(position).getStatus()));
        //check status, to do task finished or not
        if (toBoolean(todoList.get(position).getStatus())) {
            holder.task.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);  //set the font Strikethrough
            holder.task.getPaint().setAntiAlias(true);
            finishedTask += 1; //count finished task
            refreshProgressbar();
        }
        /* set OnCheckedChangeListener to check box, update database and progressbar when checked
        * */
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(todoList.get(position).getId(), 1);
                    holder.task.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);  //set the font Strikethrough
                    holder.task.getPaint().setAntiAlias(true);
                    finishedTask += 1; //count the
                    refreshProgressbar();
                } else {
                    db.updateStatus(todoList.get(position).getId(), 0);
                    holder.task.getPaint().setFlags(0);  //remove the font Strikethrough
                    finishedTask -= 1;
                    refreshProgressbar();
                }
            }
        });
        /* set OnLongCheckListener to check box, update database and progressbar when checked
         * */
        holder.task.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Delete this task");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteTask(todoList.get(position).getId());
                        updateList();
                        refreshProgressbar();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //leave empty for cancel
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount(){
        return todoList.size();
    }


    //delete the older list and get a new list from database again
    public void updateList() {
        this.todoList.clear();
        List<ToDoModel> newList = db.getAllTasks();
        totalTask = newList.size();
        this.todoList.addAll(newList);
        notifyDataSetChanged();
        finishedTask = 0;
    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    //count the percentage of finished task and set the circular progress bar
    public void refreshProgressbar() {
        float percentage = (float) finishedTask / totalTask * 100f;
        Log.i("FINISHEDTASK", Integer.toString(finishedTask));
        Log.i("TOTALTASKS", Integer.toString(totalTask));
        Log.i("PERCENTAGE", Float.toString(percentage));
        circleProgressBar.setProgress(percentage);
        int percentage1 = (int) percentage;
        this.finished = percentage1;
        Log.i("", Integer.toString(percentage1));
        progressbarPercentage.setText(Integer.toString(percentage1) + "%");
    }

    public int getFinished(){
        return finished;
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.checkbox);
        }
    }

}
