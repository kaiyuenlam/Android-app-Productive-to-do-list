package com.example.group10.FlipCardPacket;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.R;

import java.util.ArrayList;

public class AddNewFC extends AppCompatActivity implements View.OnClickListener{

    private TextView backButton, addButton;
    private ImageButton AddCard;
    private RecyclerView recyclerView;
    private EditText fcTitle;
    AddFCAdapter adapter;

    //data
    ArrayList<String> front;
    ArrayList<String> back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_fc);

        bindView();
        adapter = new AddFCAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void bindView() {
        backButton = findViewById(R.id.backToFcButton);
        addButton = findViewById(R.id.AddNewFC);
        AddCard = findViewById(R.id.addFc_imageButton);
        recyclerView = findViewById(R.id.addFc_viewItemContainer);
        fcTitle = findViewById(R.id.edit_fc_title);

        backButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        AddCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToFcButton:
                finish();
                break;

            case R.id.AddNewFC:
                // call public method of adapter to get data
                front = adapter.getFront();
                back = adapter.getBack();
                String flipCardTitle = fcTitle.getText().toString();

                Intent intent = new Intent();
                intent.putStringArrayListExtra("front", front);
                intent.putStringArrayListExtra("back", back);
                intent.putExtra("title", flipCardTitle);
                setResult(22, intent);
                //destroy activity
                finish();
                break;

            case R.id.addFc_imageButton:
                adapter.addOne();
                break;

        }
    }
}