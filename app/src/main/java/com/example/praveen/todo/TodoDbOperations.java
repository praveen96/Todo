package com.example.praveen.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Praveen on 11/5/2016.
 */

public class TodoDbOperations {
    private DbHelper dbHelper;

    public TodoDbOperations(Context context)
    {
        dbHelper = new DbHelper(context);
    }

    public int createTodo() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_TITLE, "");
        values.put(DbHelper.KEY_DETAIL, "");
        long todoId = db.insert(DbHelper.TABLE_TODO, null, values);
        db.close();
        return (int) todoId;
    }

    public int createTodo(String title, String detail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_TITLE, title);
        values.put(DbHelper.KEY_DETAIL, detail);
        long todoId = db.insert(DbHelper.TABLE_TODO, null, values);
        db.close();
        return (int) todoId;
    }

    public Todo getTodo(int todoId)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT *"
                + " FROM " + DbHelper.TABLE_TODO
                + " WHERE id = " + Integer.toString(todoId);

        Cursor c = db.rawQuery(selectQuery, null);
        Todo res = new Todo();
        if(c.moveToFirst())
        {
            do {
                res.setId(c.getInt(c.getColumnIndex(DbHelper.KEY_TODOID)));
                res.setTitle(c.getString(c.getColumnIndex(DbHelper.KEY_TITLE)));
                res.setDetail(c.getString(c.getColumnIndex(DbHelper.KEY_DETAIL)));
            } while (c.moveToNext());
        } else {
            res = null;
        }

        c.close();
        db.close();
        return res;
    }

    public ArrayList<Todo> getAllTodos()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  *"
                + " FROM " + DbHelper.TABLE_TODO
                + " ORDER BY " + DbHelper.KEY_TODOID + " DESC";

        ArrayList<Todo> res = new ArrayList<Todo>();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst())
        {
            do {
                Todo todo = new Todo();
                todo.setId(c.getInt(c.getColumnIndex(DbHelper.KEY_TODOID)));
                todo.setTitle(c.getString(c.getColumnIndex(DbHelper.KEY_TITLE)));
                todo.setDetail(c.getString(c.getColumnIndex(DbHelper.KEY_DETAIL)));
                res.add(todo);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return res;
    }

    public ArrayList<Todo> getAllPendingTodos()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  *"
                + " FROM " + DbHelper.TABLE_TODO
                + " WHERE " + DbHelper.KEY_COMPLETED + " = " + Integer.toString(0)
                + " ORDER BY " + DbHelper.KEY_TODOID + " DESC";

        ArrayList<Todo> res = new ArrayList<Todo>();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst())
        {
            do {
                Todo todo = new Todo();
                todo.setId(c.getInt(c.getColumnIndex(DbHelper.KEY_TODOID)));
                todo.setTitle(c.getString(c.getColumnIndex(DbHelper.KEY_TITLE)));
                todo.setDetail(c.getString(c.getColumnIndex(DbHelper.KEY_DETAIL)));
                res.add(todo);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return res;
    }

    public ArrayList<Todo> getAllCompletedTodos()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  *"
                + " FROM " + DbHelper.TABLE_TODO
                + " WHERE " + DbHelper.KEY_COMPLETED + " = " + Integer.toString(1)
                + " ORDER BY " + DbHelper.KEY_TODOID + " DESC";

        ArrayList<Todo> res = new ArrayList<Todo>();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst())
        {
            do {
                Todo todo = new Todo();
                todo.setId(c.getInt(c.getColumnIndex(DbHelper.KEY_TODOID)));
                todo.setTitle(c.getString(c.getColumnIndex(DbHelper.KEY_TITLE)));
                todo.setDetail(c.getString(c.getColumnIndex(DbHelper.KEY_DETAIL)));
                res.add(todo);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return res;
    }

    public void updateTodo(Todo todo)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_TITLE, todo.getTitle());
        values.put(DbHelper.KEY_DETAIL, todo.getDetail());
        db.update(DbHelper.TABLE_TODO, values, DbHelper.KEY_TODOID + " = ?", new String[]{String.valueOf(todo.getId())});
        db.close();
    }

    public void completeTodo(int todoID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_COMPLETED, 1);
        db.update(DbHelper.TABLE_TODO, values, DbHelper.KEY_TODOID + " = ?", new String[]{String.valueOf(todoID)});
        db.close();
    }

    public void deleteTodo(int todoId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_TODO, DbHelper.KEY_TODOID + " = ?", new String[]{String.valueOf(todoId)});
        db.close();
    }

    public int isCompleted(int todoId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT *"
                + " FROM " + DbHelper.TABLE_TODO
                + " WHERE id = " + Integer.toString(todoId);

        Cursor c = db.rawQuery(selectQuery, null);
        int res;
        if(c.moveToFirst())
        {
            res = c.getInt(c.getColumnIndex(DbHelper.KEY_COMPLETED));
            /*do {
                res.setId(c.getInt(c.getColumnIndex(DbHelper.KEY_TODOID)));
                res.setTitle(c.getString(c.getColumnIndex(DbHelper.KEY_TITLE)));
                res.setDetail(c.getString(c.getColumnIndex(DbHelper.KEY_DETAIL)));
            } while (c.moveToNext());*/
        } else {
            res = 0;
        }

        c.close();
        db.close();
        return res;
    }

}
