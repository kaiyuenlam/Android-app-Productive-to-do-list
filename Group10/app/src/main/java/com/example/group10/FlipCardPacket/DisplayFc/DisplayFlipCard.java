package com.example.group10.FlipCardPacket.DisplayFc;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DisplayFlipCard extends AppCompatActivity implements View.OnClickListener, DisplayFlipCardAdapter.OnItemClick{

    private String title, front, back;
    private int id;
    ArrayList<String> frontList;
    ArrayList<String> backList;

    private TextView backButton;
    private ImageButton addCard;
    private RecyclerView recyclerView;

    Gson gson;
    DisplayFlipCardAdapter adapter;

    private AnimatorSet frontAnim;
    private AnimatorSet backAnim;
    private float scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_flip_card);
        //receive data from Flip Card activity
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        front = intent.getStringExtra("front");
        back = intent.getStringExtra("back");
        id = intent.getIntExtra("id", -1);
        if (id == -1) {
            Log.i("DisplayFC", "damn, wrong id, fix bug fix bug");
        }

        //convert te string data back to arrayList
        gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        frontList = gson.fromJson(front, type);
        backList = gson.fromJson(back, type);

        bindView();
        //pass data into adapter
        adapter = new DisplayFlipCardAdapter(frontList, backList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setFlipCardAnimation();
    }

    private void bindView() {
        backButton = findViewById(R.id.exitFCView);
        addCard = findViewById(R.id.displayFC);
        recyclerView = findViewById(R.id.viewFC_ListContainer);

        backButton.setOnClickListener(this);
        addCard.setOnClickListener(this);

        backButton.setText(title);
    }

    private void setFlipCardAnimation() {
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.back_animatior);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exitFCView:
                //pass data back to FlipCard Activity
                Intent intent = new Intent();
                frontList = adapter.getFront();
                backList = adapter.getBack();
                intent.putStringArrayListExtra("front", frontList);
                intent.putStringArrayListExtra("back", backList);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                setResult(11, intent);
                //destroy activity
                finish();
                break;

            case R.id.displayFC:
                /* add new task
                * build a new layout for alert dialog*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText frontBox = new EditText(this);
                frontBox.setHint("Front Card");
                layout.addView(frontBox);

                final EditText backBox = new EditText(this);
                backBox.setHint("Back Card");
                layout.addView(backBox);

                builder.setView(layout);
                builder.setTitle("New Card Input");
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get new input from edit text and pass to adapter
                        String frontText = frontBox.getText().toString();
                        String backText = backBox.getText().toString();
                        adapter.addOne(frontText, backText);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //leave empty for cancel
                    }
                });
                builder.create().show();
                break;
        }
    }

    //Flip card animation
    @Override
    public void onClick(CardView front, CardView back, boolean isFront) {
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);

        if (isFront){
            frontAnim.setTarget(front);
            backAnim.setTarget(back);
            frontAnim.start();
            backAnim.start();
        } else {
            frontAnim.setTarget(back);
            backAnim.setTarget(front);
            backAnim.start();
            frontAnim.start();
        }
    }
}