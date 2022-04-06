package com.example.group10.fragments.notesFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.group10.Notes;
import com.example.group10.R;

public class notesAddNewNote extends AppCompatActivity {

    EditText editText_title, editText_body;
    TextView backButton, addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_add_new_note);

        bindView();

        //back to note activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //destroy activity
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] inputData = getInputData();
                String inputTitle = inputData[0];
                String inputContent = inputData[1];

                Intent intent = new Intent();
                intent.putExtra("inputTitle", inputTitle);
                intent.putExtra("inputContent", inputContent);
                setResult(66, intent);
                //destroy activity
                finish();
            }
        });

    }

    private void bindView() {
        editText_title = findViewById(R.id.text_title);
        editText_body = findViewById(R.id.text_body);
        backButton = findViewById(R.id.backButton);
        addButton = findViewById(R.id.AddNewNotes);

    }
    /* store the input data from two editText into a string array and return it
    * */
    private String[] getInputData() {
        String[] result =  new String[2];
        result[0] = editText_title.getText().toString();
        result[1] = editText_body.getText().toString();
        return result;
    }


}