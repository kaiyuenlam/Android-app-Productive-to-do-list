package com.example.group10;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.group10.todolist.ToDoModel;
import com.google.android.material.navigation.NavigationView;

public class Settings extends AppCompatActivity implements View.OnClickListener{
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    Button changeGreeting;
    Button email;

    String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setDrawerNavigationView();
        setBottomNavigationView();

        changeGreeting = findViewById(R.id.btn1);
        email = findViewById(R.id.btn2);

        changeGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                final EditText editText = new EditText(Settings.this);

                builder.setView(editText);
                builder.setTitle("Greeting input");
                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences pref = getSharedPreferences("Greeting", MODE_PRIVATE);
                        greeting = editText.getText().toString();
                        pref.edit().putString("greeting", greeting).commit();
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
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                LinearLayout layout = new LinearLayout(Settings.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText et1 = new EditText(Settings.this);
                et1.setHint("Title");
                layout.addView(et1);

                final EditText et2 = new EditText(Settings.this);
                et2.setHint("Content");
                layout.addView(et2);

                builder.setView(layout);
                builder.setTitle("Report Bug");
                builder.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, "210303442@stu.vtc.edu.hk");
                        email.putExtra(Intent.EXTRA_SUBJECT, et1.getText().toString());
                        email.putExtra(Intent.EXTRA_TEXT, et2.getText().toString());
                        email.setType("text/plain");
                        startActivity(Intent.createChooser(email, "Select your Email app"));
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
                Intent intent1 = new Intent(Settings.this, MyFavorite.class);
                finish();
                startActivity(intent1);
                break;
            case R.id.buttonNav_calendar:
                Intent intent2 = new Intent(Settings.this, calendar.class);
                finish();
                startActivity(intent2);
                break;
            case R.id.buttonNav_settings:

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
                        Intent intent1 = new Intent(Settings.this, MainActivity.class);
                        intent1.putExtra("greeting", greeting);
                        setResult(77, intent1);
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(Settings.this, calendar.class);
                        finish();
                        startActivity(intent2);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(Settings.this, FlipCard.class);
                        finish();
                        startActivity(intent3);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(Settings.this, Notes.class);
                        finish();
                        startActivity(intent4);
                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent5 = new Intent(Settings.this, MyFavorite.class);
                        finish();
                        startActivity(intent5);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
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
}