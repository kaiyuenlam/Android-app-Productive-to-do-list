package com.example.group10.CalendarPacket;

import com.example.group10.todolist.ToDoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CalendarDatabaseModel {
    int id;
    List<ToDoModel> todoList;
    String date;
    String month;
    String todoStringList;
    int finished;

    private Gson gson = new Gson();
    Type type = new TypeToken<List<ToDoModel>>() {}.getType();

    public CalendarDatabaseModel(){
        todoList = null;
        date = null;
        month = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ToDoModel> getTodoList() {
        if (todoList == null) {
            this.todoList = gson.fromJson(this.todoStringList, type);
        }
        return todoList;
    }

    public String getStringToDoList() {
        return todoStringList;
    }

    public void setTodoStringList(String todoStringList) {
        this.todoList = gson.fromJson(todoStringList, type);
        this.todoStringList = todoStringList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
