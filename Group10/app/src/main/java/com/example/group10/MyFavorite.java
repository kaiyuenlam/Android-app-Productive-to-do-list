package com.example.group10;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.FlipCardPacket.DisplayFc.DisplayFlipCard;
import com.example.group10.fragments.notesFragments.DisplayNote;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyFavorite extends AppCompatActivity implements View.OnClickListener, MyFavorAdapter.OnItemClick{
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    RecyclerView recyclerView;
    MyFavorAdapter adapter;

    ArrayList<MyFavorModel> myFavorList;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        setDrawerNavigationView();
        setBottomNavigationView();
        initView();

        /*get list of my favor from SharedPreference
         * and convert back to arraylist */
        String favorList = getSharedPreferences("myFavor", MODE_PRIVATE).getString("list", "");
        if ("".equals(favorList)) {
            Log.d("MyFavor Activity", "get null from sharedPreference");
        }
        Type type = new TypeToken<ArrayList<MyFavorModel>>() {}.getType();
        gson = new Gson();
        myFavorList = gson.fromJson(favorList, type);

        adapter = new MyFavorAdapter(myFavorList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

    private void initView() {
        recyclerView = findViewById(R.id.myFavorRecyclerView);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonNav_favor:

                break;
            case R.id.buttonNav_calendar:
                Intent intent2 = new Intent(MyFavorite.this, calendar.class);
                finish();
                startActivity(intent2);
                break;
            case R.id.buttonNav_settings:
                Intent intent3 = new Intent(MyFavorite.this, Settings.class);
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
                        Intent intent1 = new Intent(MyFavorite.this, MainActivity.class);
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(MyFavorite.this, calendar.class);
                        finish();
                        startActivity(intent2);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(MyFavorite.this, FlipCard.class);
                        finish();
                        startActivity(intent3);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(MyFavorite.this, Notes.class);
                        finish();
                        startActivity(intent4);
                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent6 = new Intent(MyFavorite.this, Settings.class);
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
    public void open(int position) {
        int type = myFavorList.get(position).getType();
        if (type == 1) {
            Intent intent = new Intent(this, DisplayNote.class);
            intent.putExtra("displayTitle", myFavorList.get(position).getTitle());
            intent.putExtra("displayContent", myFavorList.get(position).getContent());
            intent.putExtra("ID", 0);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DisplayFlipCard.class);
            intent.putExtra("id", 0);
            intent.putExtra("title", myFavorList.get(position).getTitle());
            intent.putExtra("front", myFavorList.get(position).getFront());
            intent.putExtra("back", myFavorList.get(position).getBack());
            startActivity(intent);
        }
    }
}