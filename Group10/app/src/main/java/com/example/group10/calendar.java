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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.group10.CalendarPacket.CalendarAdapter;
import com.example.group10.CalendarPacket.CalendarDatabaseHelper;
import com.example.group10.CalendarPacket.CalendarDatabaseModel;
import com.example.group10.CalendarPacket.ListViewAdapter;
import com.example.group10.todolist.ToDoModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class calendar extends AppCompatActivity implements View.OnClickListener, CalendarAdapter.OnItemListener {
    //bottom navigation bar
    ImageButton buttonNav_favor, buttonNav_calendar, buttonNav_settings;

    //drawer navigation bar
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CustomDrawerButton customDrawerButton;

    //calendar
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    //
    Gson gson = new Gson();
    CalendarDatabaseHelper db;
    CalendarDatabaseModel todayModel;

    //
    ArrayList<CalendarDatabaseModel> modelList;
    RecyclerView listView;
    ListViewAdapter adapter;
    String todoList;
    int finished;
    CalendarDatabaseModel calendarDatabaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        selectedDate = LocalDate.now();
        setData();

        setDrawerNavigationView();
        setBottomNavigationView();
        db = new CalendarDatabaseHelper(this);
        initWidgets();

        setMonthView();

        listView = findViewById(R.id.calendarItemListView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
    }

    //get todo list data
    private void setData() {
        // not completed method
        Intent intent = getIntent();
        todoList = intent.getStringExtra("todolist");
        finished = intent.getIntExtra("finished", -1);
        if (finished == -1) {
            Log.i("CALENDAR activity", "damn, get null int");
        }
        calendarDatabaseModel = new CalendarDatabaseModel();
        calendarDatabaseModel.setDate(String.valueOf(selectedDate.getDayOfMonth()));
        calendarDatabaseModel.setTodoStringList(todoList);
        calendarDatabaseModel.setMonth(selectedDate.getMonth().toString());
        calendarDatabaseModel.setFinished(finished);
        //db.insertData(calendarDatabaseModel);
    }

    //bind calender view
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(selectedDate.getMonth().toString());
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate.getMonth().toString(), calendarDatabaseModel);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(String dayText) {
        if (dayText.equals(calendarDatabaseModel.getDate())) {

            adapter.setList(calendarDatabaseModel.getTodoList());
        }
        //CalendarDatabaseModel model = db.getSpecificDate(month, Integer.parseInt(dayText));

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
                Intent intent1 = new Intent(calendar.this, MyFavorite.class);
                finish();
                startActivity(intent1);
                break;
            case R.id.buttonNav_calendar:
                break;
            case R.id.buttonNav_settings:
                Intent intent3 = new Intent(calendar.this, Settings.class);
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
                        Intent intent1 = new Intent(calendar.this, MainActivity.class);
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.drawer_calendar:
                        Log.i("MENU_DRAWER_TAG", "Calendar item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.drawer_flipcard:
                        Log.i("MENU_DRAWER_TAG", "FlipCard item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent3 = new Intent(calendar.this, FlipCard.class);
                        finish();
                        startActivity(intent3);
                        break;

                    case R.id.drawer_notes:
                        Log.i("MENU_DRAWER_TAG", "Notes item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent4 = new Intent(calendar.this, Notes.class);
                        finish();
                        startActivity(intent4);
                        break;

                    case R.id.drawer_favor:
                        Log.i("MENU_DRAWER_TAG", "Favourite item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent5 = new Intent(calendar.this, MyFavorite.class);
                        finish();
                        startActivity(intent5);
                        break;

                    case R.id.drawer_setting:
                        Log.i("MENU_DRAWER_TAG", "Settings item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent6 = new Intent(calendar.this, Settings.class);
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
}
