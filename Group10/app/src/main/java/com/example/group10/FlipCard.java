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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.FlipCardPacket.*;
import com.example.group10.FlipCardPacket.DisplayFc.DisplayFlipCard;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FlipCard extends AppCompatActivity implements View.OnClickListener, FlipCardAdapter.OnItemClickListener, FlipCardAdapter.AddToFavor{
    final String TAG = "FlipCardActivity";
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    //ui
    ImageButton addNewCardButton;
    RecyclerView recyclerView;

    //database
    FlipCardDatabaseHelper db;
    FlipCardAdapter adapter;

    //Gson, used for convert ArrayList into Json, which is String as SQLite can't store arraylist
    Gson gson = new Gson();
    ArrayList<MyFavorModel> myFavorList;

    /*allow passing data from add new note activity and note display
     * */
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult");

                    if (result.getResultCode() == 22) {
                        //add new flip card
                        Log.i(TAG, "22");
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<String> newFront = intent.getStringArrayListExtra("front");
                            ArrayList<String> newBack = intent.getStringArrayListExtra("back");
                            String title = intent.getStringExtra("title");

                            String inputFront = gson.toJson(newFront);
                            String inputBack = gson.toJson(newBack);
                            FlipCardModel newOne = new FlipCardModel(title, inputFront, inputBack);
                            db.insertTask(newOne);
                            adapter.UpdateList();
                        }

                    } else if (result.getResultCode() == 11) {
                        //update flip card
                        Log.i(TAG, "11");
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<String> newFront = intent.getStringArrayListExtra("front");
                            ArrayList<String> newBack = intent.getStringArrayListExtra("back");
                            String title = intent.getStringExtra("title");
                            int id = intent.getIntExtra("id", -1);
                            if (id == -1) {
                                Log.i(TAG, "damn, wrong id");
                            } else {
                                String inputFront = gson.toJson(newFront);
                                String inputBack = gson.toJson(newBack);
                                FlipCardModel newUpdate = new FlipCardModel(title, inputFront, inputBack);
                                newUpdate.setId(id);
                                db.updateTask(newUpdate);
                                adapter.UpdateList();
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_card);

        setDrawerNavigationView();
        setBottomNavigationView();

        /*get list of my favor from SharedPreference
         * and convert back to arraylist */
        String favorList = getSharedPreferences("myFavor", MODE_PRIVATE)
                .getString("list", "");
        if ("".equals(favorList)) {
            Log.d("FC Activity", "get null from sharedPreference");
        }
        Type type = new TypeToken<ArrayList<MyFavorModel>>() {}.getType();
        gson = new Gson();
        myFavorList = gson.fromJson(favorList, type);
        //
        addNewCardButton = findViewById(R.id.addNewCard);
        recyclerView = findViewById(R.id.fcRecyclerViewContainer);

        db = new FlipCardDatabaseHelper(this);
        adapter = new FlipCardAdapter(db, this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        addNewCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlipCard.this, AddNewFC.class);
                launcher.launch(intent);
            }
        });
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
                Intent intent1 = new Intent(FlipCard.this, MyFavorite.class);
                uploadMyFavorList();
                finish();
                startActivity(intent1);
                break;
            case R.id.buttonNav_calendar:
                Intent intent2 = new Intent(FlipCard.this, calendar.class);
                uploadMyFavorList();
                finish();
                startActivity(intent2);
                break;
            case R.id.buttonNav_settings:
                Intent intent3 = new Intent(FlipCard.this, Settings.class);
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
                        Intent intent1 = new Intent(FlipCard.this, MainActivity.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(FlipCard.this, calendar.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent2);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(FlipCard.this, Notes.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent4);
                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent5 = new Intent(FlipCard.this, MyFavorite.class);
                        uploadMyFavorList();
                        finish();
                        startActivity(intent5);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent6 = new Intent(FlipCard.this, Settings.class);
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

    //open content of flip class
    @Override
    public void onItemClick(int id, String title, String front, String back) {
        Intent intent = new Intent(this, DisplayFlipCard.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("front", front);
        intent.putExtra("back", back);
        launcher.launch(intent);
    }

    @Override
    public void addFavor(int position) {
        ArrayList<FlipCardModel> list = adapter.getList();
        String title = list.get(position).getTitle();
        String front = list.get(position).getFront();
        String back = list.get(position).getBack();
        MyFavorModel addNew = new MyFavorModel(title, front, back);
        myFavorList.add(addNew);
    }

    private void uploadMyFavorList() {
        String list = gson.toJson(myFavorList);
        SharedPreferences pref = getSharedPreferences("myFavor", MODE_PRIVATE);
        pref.edit().putString("list", list).apply();
    }
}