package com.example.group10.FlipCardPacket;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddFCAdapter extends RecyclerView.Adapter<AddFCAdapter.ViewHolder>{
    /* int count is for increase view item
    *  ArrayList is for store data*/
    private int count = 1;
    ArrayList<String> front = new ArrayList<String>();
    ArrayList<String> back = new ArrayList<String>();

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addnewfcitem, parent, false);
        front.add("");
        back.add("");
        return new AddFCAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.counter.setText(Integer.toString(position + 1));
        holder.editText_front.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //replace a new value to specific arrayList index after editText changed
                front.set(position, editable.toString());
            }
        });

        holder.editText_back.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //replace a new value to specific arrayList index after editText changed
                back.set(position, editable.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    //
    public void addOne() {
        count += 1;
        //add ArrayList index
        front.add("");
        back.add("");
        notifyDataSetChanged();
    }

    public ArrayList<String> getFront() {
        return front;
    }

    public ArrayList<String> getBack() {
        return back;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        EditText editText_front, editText_back;
        TextView counter;

        ViewHolder(View view){
            super(view);
            editText_front = view.findViewById(R.id.edit_front);
            editText_back = view.findViewById(R.id.edit_back);
            counter = view.findViewById(R.id.counter);

        }
    }
}
