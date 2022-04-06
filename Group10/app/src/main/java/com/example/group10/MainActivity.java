package com.example.group10;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.circularProgressbar.CircleProgressBar;
import com.example.group10.todolist.DatabaseHelper;
import com.example.group10.todolist.ToDoListAdapter;
import com.example.group10.todolist.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    //to do list
    private RecyclerView toDoListView;
    private FloatingActionButton addTaskButton;
    DatabaseHelper databaseHelper;
    ToDoListAdapter listAdapter;
    List<ToDoModel> todoList;

    //fragment
    private Fragment fragment;

    //greeting text and circular progress bar
    TextView greeting, progressbarPercentage;
    CircleProgressBar circleProgressBar;

    Gson gson;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("MAINACTIVITY", "onActivityResult");

                    if (result.getResultCode() == 77) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String newGreeting = intent.getStringExtra("greeting");
                            greeting.setText(newGreeting);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setGreetingAndProgressBar();
        setDrawerNavigationView();
        setBottomNavigationView();
        setToDoListView();

        //initial list for add favor
        ArrayList<MyFavorModel> myFavorList = new ArrayList<>();
        gson = new Gson();
        String list = gson.toJson(myFavorList);
        SharedPreferences pref = getSharedPreferences("myFavor", MODE_PRIVATE);
        pref.edit().putString("list", list).commit();
        String newGreeting = getSharedPreferences("Greeting", MODE_PRIVATE).getString("greeting", null);
        if (newGreeting != null) {
            greeting.setText(newGreeting);
        }
    }

    protected void setGreetingAndProgressBar() {
        greeting = findViewById(R.id.greeting);
        circleProgressBar = findViewById(R.id.progress_circular);
        progressbarPercentage = findViewById(R.id.progress_percentage);

    }

    //bind to do list view and function
    protected void setToDoListView() {
        addTaskButton = findViewById(R.id.addTaskButton);

        databaseHelper = new DatabaseHelper(this);
        todoList = databaseHelper.getAllTasks();

        listAdapter= new ToDoListAdapter(progressbarPercentage, circleProgressBar, databaseHelper, this, todoList);
        toDoListView = findViewById(R.id.todolist);
        toDoListView.setLayoutManager(new LinearLayoutManager(this));
        toDoListView.setAdapter(listAdapter);
        listAdapter.refreshProgressbar();
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
    }

    //bind view and OnClickListener
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
                Intent intent1 = new Intent(MainActivity.this, MyFavorite.class);
                uploadList();
                startActivity(intent1);
                break;
            case R.id.buttonNav_calendar:
                Intent intent2 = new Intent(MainActivity.this, calendar.class);
                List<ToDoModel> aList = databaseHelper.getAllTasks();
                String list = gson.toJson(aList);
                int i = listAdapter.getFinished();
                Log.i("MAINactivity", Integer.toString(i));
                intent2.putExtra("todolist", list);
                intent2.putExtra("finished", i);
                uploadList();
                startActivity(intent2);
                break;
            case R.id.buttonNav_settings:
                Intent intent3 = new Intent(MainActivity.this, Settings.class);
                uploadList();
                launcher.launch(intent3);
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
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(MainActivity.this, calendar.class);
                        uploadList();
                        startActivity(intent2);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(MainActivity.this, FlipCard.class);
                        uploadList();
                        startActivity(intent3);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(MainActivity.this, Notes.class);
                        uploadList();
                        startActivity(intent4);
                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent5 = new Intent(MainActivity.this, MyFavorite.class);
                        uploadList();
                        startActivity(intent5);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent6 = new Intent(MainActivity.this, Settings.class);
                        uploadList();
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

    //pop up dialog for input new todolist item
    private void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(MainActivity.this);

        builder.setView(editText);
        builder.setTitle("New Task Input");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ToDoModel newTask = new ToDoModel();
                newTask.setTask(editText.getText().toString());
                databaseHelper.insertTask(newTask);
                listAdapter.updateList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //leave empty for cancel
            }
        });
        builder.create().show();
    }

    private void uploadList(){
        /*SharedPreferences pref = getSharedPreferences("ToDoList", MODE_PRIVATE);
        List<ToDoModel> aList = databaseHelper.getAllTasks();
        String list = gson.toJson(aList);
        pref.edit().putString("list", list);
        pref.edit().putInt("finished", listAdapter.getFinished());
        pref.edit().apply();*/
    }

}