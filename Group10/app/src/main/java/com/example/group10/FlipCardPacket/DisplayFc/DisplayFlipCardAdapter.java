package com.example.group10.FlipCardPacket.DisplayFc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DisplayFlipCardAdapter extends RecyclerView.Adapter<DisplayFlipCardAdapter.ViewHolder>{

    ArrayList<String> front;
    ArrayList<String> back;
    OnItemClick onItemClick;
    DisplayFlipCard activity;

    public DisplayFlipCardAdapter(ArrayList<String> front, ArrayList<String> back, OnItemClick onItemClick, DisplayFlipCard activity) {
        this.front = front;
        this.back = back;
        this.onItemClick = onItemClick;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_flip_card_item, parent, false);
        return new DisplayFlipCardAdapter.ViewHolder(itemView, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.frontText.setText(front.get(position));
        holder.backText.setText(back.get(position));

    }

    @Override
    public int getItemCount() {
        return front.size();
    }

    public void itemOnMove(int from, int to) {
        String frontText = front.get(from);
        String backText = back.get(from);
        front.remove(frontText);
        back.remove(backText);
        front.add(to, frontText);
        back.add(to, backText);
        notifyItemMoved(from, to);
    }

    public void addOne(String frontText, String backText) {
        front.add(frontText);
        back.add(backText);
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        front.remove(position);
        back.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getFront() {
        return front;
    }

    public ArrayList<String> getBack() {
        return back;
    }

    //interface for passing view item to DisplayFlipCard activity
    interface OnItemClick {
        void onClick(CardView frontCard, CardView backCard, boolean isFront);
    }


    //initial item view
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView frontCard, backCard;
        TextView frontText, backText;
        OnItemClick onItemClick;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            frontCard = itemView.findViewById(R.id.frontCardView);
            backCard = itemView.findViewById(R.id.backCardView);
            frontText = itemView.findViewById(R.id.textView_front);
            backText = itemView.findViewById(R.id.textView_back);
            frontText.setClickable(true);
            backText.setClickable(false);
            frontText.setOnClickListener(this);
            backText.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.textView_front) {
                //pass the clicked item to display flip card activity
                onItemClick.onClick(frontCard, backCard, true);
                frontText.setClickable(false);
                backText.setClickable(true);
            } else if (view.getId() == R.id.textView_back) {
                onItemClick.onClick(frontCard, backCard, false);
                frontText.setClickable(true);
                backText.setClickable(false);
            }
        }
    }
}
