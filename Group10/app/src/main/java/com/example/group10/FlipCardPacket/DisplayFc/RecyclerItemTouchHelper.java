package com.example.group10.FlipCardPacket.DisplayFc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final DisplayFlipCardAdapter adapter;

    public RecyclerItemTouchHelper(DisplayFlipCardAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
        adapter.itemOnMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete card");
            builder.setMessage("Are you sure you want to delete this flip-card?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dx, float dY, int actionState, boolean isCurrentlyAction) {
        super.onChildDraw(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyAction);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dx < 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
            assert icon != null;
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();
            //Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dx) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);
            icon.draw(c);
        }


    }
}
