package com.example.praveen.todo;

import android.os.Parcelable;

/**
 * Created by Praveen on 11/5/2016.
 */

public class Todo {
    private String title, detail;
    private int id;

    public  Todo() {

    }

    public Todo(int id, String title, String detail) {
        this.title = title;
        this.detail = detail;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {

        return title;
    }

    public String getDetail() {
        return detail;
    }
}
