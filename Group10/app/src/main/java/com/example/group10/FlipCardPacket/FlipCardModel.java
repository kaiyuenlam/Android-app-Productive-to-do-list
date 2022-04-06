package com.example.group10.FlipCardPacket;

import java.util.ArrayList;

public class FlipCardModel {
    String front;
    String back;
    int id;
    String title;

    public FlipCardModel() {
        this.title = null;
        this.front = null;
        this.back = null;
    }

    public FlipCardModel(String title, String front, String back) {
        super();
        this.title = title;
        this.front = front;
        this.back = back;
    }



    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
