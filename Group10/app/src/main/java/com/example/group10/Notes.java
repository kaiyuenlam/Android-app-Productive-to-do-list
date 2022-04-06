package com.example.group10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.fragments.notesFragments.*;
import com.example.group10.todolist.DatabaseHelper;
import com.example.group10.todolist.ToDoListAdapter;
import com.example.group10.todolist.ToDoModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity implements View.OnClickListener, NotesAdapter.OnNoteListener, NotesAdapter.OnNoteLongClick {
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    //notes database
    NotesDatabaseHelper notesDatabaseHelper;
    NotesAdapter notesAdapter;
    List<NotesModel> notesModelList;

    //add new note button
    ImageButton addNewNote;
    //my favor note
    ArrayList<MyFavorModel> myFavorList;
    Gson gson;

    /*allow passing data from add new note activity and note display
    * */
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("NOTESACTIVITY", "onActivityResult");

                    if (result.getResultCode() == 66) {
                        Log.i("NOTESACTIVITY", "66");
                        Intent intent = result.getData();
                        if (intent != null) {
                            String newNoteTitle = intent.getStringExtra("inputTitle");
                            String newNoteContent = intent.getStringExtra("inputContent");
                            addNewNote(newNoteTitle, newNoteContent);
                            notesModelList.clear();
                            notesModelList = notesDatabaseHelper.getAllTasks();
                            notesAdapter.updateList();
                        }
                    } else if (result.getResultCode() == 33) {
                        Log.i("NOTESACTIVITY", "33");
                        Intent intent = result.getData();
                        if (intent != null) {
                            String newNoteTitle = intent.getStringExtra("editTitle");
                            String newNoteContent = intent.getStringExtra("editContent");
                            int id = intent.getIntExtra("ID", -1);
                            if (id == -1) {
                                Log.i("NotesActivity", "damn, wrong id");
                            } else {
                                updateNote(id, newNoteTitle, newNoteContent);
                                notesModelList.clear();
                                notesModelList = notesDatabaseHelper.getAllTasks();
                                notesAdapter.updateList();
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        //set bottom navigation bar and drawer navigation
        setDrawerNavigationView();
        setBottomNavigationView();
        /*get list of my favor from SharedPreference
        * and convert back to arraylist */
        String favorList = getSharedPreferences("myFavor", MODE_PRIVATE)
                .getString("list", "");
        if ("".equals(favorList)) {
            Log.d("Notes Activity", "get null from sharedPreference");
        }
        Type type = new TypeToken<ArrayList<MyFavorModel>>() {}.getType();
        gson = new Gson();
        myFavorList = gson.fromJson(favorList, type);

        //fill list data from database
        notesDatabaseHelper = new NotesDatabaseHelper(this);
        notesModelList = notesDatabaseHelper.getAllTasks();
        //initial adapter
        notesAdapter = new NotesAdapter(notesModelList, notesDatabaseHelper, this, this, this);
        //recycler view and adapter
        RecyclerView recyclerView = findViewById(R.id.note_fragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);
        //ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(notesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //add now notes button
        addNewNote = findViewById(R.id.notesFront_addButton);
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notes.this, notesAddNewNote.class);
                launcher.launch(intent);
            }
        });
    }

    /* add a new note to database
    **/
    private void addNewNote(String title, String content) {
        NotesModel newNote = new NotesModel();
        newNote.setTitle(title);
        newNote.setContent(content);
        notesDatabaseHelper.insertTask(newNote);
    }

    private void updateNote(int id, String title, String content) {
        Log.d("Update Note","called");
        NotesModel editNote = new NotesModel();
        editNote.setTitle(title);
        editNote.setContent(content);
        editNote.setId(id);
        notesDatabaseHelper.updateTask(editNote);
    }

    //bind bottom imageButton view and OnClickListener
    protected void setBottomNavigationView() {
        buttonNav_favor = findViewById(R.id.buttonNav_favor);
        buttonNav_calendar = findViewById(R.id.buttonNav_calendar);
        buttonNav_settings = findViewById(R.id.buttonNav_settings);

        buttonNav_favor.setOnClickListener(this);
        buttonNav_calendar.setOnClickListener(this);
        buttonNav_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonNav_favor:
                Intent intent1 = new Intent(Notes.this, MyFavorite.class);
                uploadMyFavorList();
                finish();
                startActivity(intent1);
                break;
            case R.id.buttonNav_calendar:
                Intent intent2 = new Intent(Notes.this, calendar.class);
                uploadMyFavorList();
                finish();
                startActivity(intent2);
                break;
            case R.id.buttonNav_settings:
                Intent intent3 = new Intent(Notes.this, Settings.class);
                uploadMyFavorList();
                finish();
                startActivity(intent3);
                break;
        }
    }

    //bind drawer navigation and functions
    protected void setDrawerNavigationView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.drawerView);
        customDrawerButton = findViewById(R.id.buttonNav_menu);

        customDrawerButton.setDrawerLayout(drawerLayout);
        customDrawerButton.getDrawerLayout().addDrawerListener(customDrawerButton);

        //OnClickListener for open the drawer
        customDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDrawerButton.changeState();
            }
        });

        //OnClickListener for navigation items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                switch (item.getItemId()) {
                    case R.id.drawer_home:
                        Log.i("MENU_DRAWER_TAG", "Home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(Notes.this, MainActivity.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(Notes.this, calendar.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent2);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(Notes.this, FlipCard.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent3);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent5 = new Intent(Notes.this, MyFavorite.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent5);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent6 = new Intent(Notes.this, Settings.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent6);
                        break;
                }

                return true;
            }
        });
    }

    //Handle navigation view item clicks in activity method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnNoteClick(int position) {
        Intent intent = new Intent(this, DisplayNote.class);
        intent.putExtra("displayTitle", notesModelList.get(position).getTitle());
        intent.putExtra("displayContent", notesModelList.get(position).getContent());
        intent.putExtra("ID", notesModelList.get(position).getId());
        launcher.launch(intent);
    }

    @Override
    public void OnNoteLongClick(int position) {
        String title = notesModelList.get(position).getTitle();
        String content = notesModelList.get(position).getContent();
        MyFavorModel addNew = new MyFavorModel(title, content);
        myFavorList.add(addNew);
    }

    private void uploadMyFavorList() {
        String list = gson.toJson(myFavorList);
        SharedPreferences pref = getSharedPreferences("myFavor", MODE_PRIVATE);
        pref.edit().putString("list", list).apply();
    }
}