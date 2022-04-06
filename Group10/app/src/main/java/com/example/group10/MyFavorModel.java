package com.example.group10;

public class MyFavorModel {
    //type: 1 for note, 2 for flip card
    int id, type;
    String title, content, front, back;

    public MyFavorModel() {
        this.type = type;
        this.title = null;
        this.content = null;
        this.front = null;
        this.back = null;
    }

    public MyFavorModel(String title, String content){
        super();
        this.type = 1;
        this.title = title;
        this.content = content;
    }

    MyFavorModel(String title, String front, String back){
        super();
        this.type = 2;
        this.title = title;
        this.front = front;
        this.back = back;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
