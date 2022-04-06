package com.example.group10.todolist;

public class ToDoModel {

    private int id;
    private String task;
    private int status;

    public ToDoModel() {
        this.task = null;
        this.status = 0;
    }
    public ToDoModel(String task, int status) {
        super();
        this.task = task;
        this.status = status;
    }

    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
