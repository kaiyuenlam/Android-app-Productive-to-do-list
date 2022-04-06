package com.example.group10.fragments.notesFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.group10.R;

public class DisplayNote extends AppCompatActivity implements View.OnClickListener{

    TextView backButton;
    ImageButton editButton, viewButton;


    String title, content;
    int key_id;

    private FragmentTransaction fragmentTransaction;
    private NotesFragmentViewMode viewMode;
    private NotesFragmentEditMode editMode;

    //SharedViewModel is for store data
    private SharedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        //get data
        Intent intent = getIntent();
        title = intent.getStringExtra("displayTitle");
        content = intent.getStringExtra("displayContent");
        key_id = intent.getIntExtra("ID", -1);
        if (key_id == -1) {
            Log.i("DisplayNote", "damn, wrong id, fix bug fix bug");
        }
        bindView();

        /* using view model for observe data changed
        *  1: save title and content to view model
        *  2: set observer to resign the value when data is changed*/
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        viewModel.setTitle(title);
        viewModel.setContent(content);
        viewModel.getTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                title = s;
            }
        });
        viewModel.getContent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                content = s;
            }
        });

        //initial view mode fragment
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        viewMode = new NotesFragmentViewMode();
        fragmentTransaction.add(R.id.notesFragmentContainer, viewMode);
        fragmentTransaction.commit();
    }

    /* initial a new view mode fragment
    * */
    private void openViewMode() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        viewMode = new NotesFragmentViewMode();
        fragmentTransaction.replace(R.id.notesFragmentContainer, viewMode);
        fragmentTransaction.commit();
    }
    /* initial a new edit mode fragment
     * */
    private void openEditMode() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        editMode = new NotesFragmentEditMode();
        fragmentTransaction.replace(R.id.notesFragmentContainer, editMode);
        fragmentTransaction.commit();
    }

    // remove all fragment
    private void removeAllFragment() {
        if (viewMode != null) {
            fragmentTransaction.remove(viewMode);
            viewModel = null;
        }
        if (editMode != null) {
            fragmentTransaction.remove(editMode);
            editMode = null;
        }
    }

    private void bindView(){
        //find ui components with id
        backButton = findViewById(R.id.exitNoteView);
        editButton = findViewById(R.id.editNote);
        viewButton = findViewById(R.id.viewNote);
        //set on click listener
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);

        viewButton.setClickable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exitNoteView:
                //pass the data back to note activity for saving data
                Intent intent = new Intent();
                intent.putExtra("editTitle", title);
                intent.putExtra("editContent", content);
                intent.putExtra("ID", key_id);
                setResult(33, intent);
                //destroy activity
                finish();
                break;

            case R.id.editNote:
                openEditMode();
                editButton.setClickable(false);
                editButton.setVisibility(View.INVISIBLE);
                viewButton.setClickable(true);
                viewButton.setVisibility(View.VISIBLE);
                break;

            case R.id.viewNote:
                editMode.updateTask();
                openViewMode();
                viewButton.setClickable(false);
                viewButton.setVisibility(View.INVISIBLE);
                editButton.setClickable(true);
                editButton.setVisibility(View.VISIBLE);
                break;
        }

    }
}