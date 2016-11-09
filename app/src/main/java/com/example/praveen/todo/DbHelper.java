package com.example.praveen.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Praveen on 11/5/2016.
 */

public class DbHelper extends SQLiteOpenHelper{

    // Log tag
    public static final String LOGTAG = "Todo-DB";

    // Database Name
    private static final String DATABASE_NAME = "todo.db";

    // Database Version
    private static int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_TODO = "todo";

    // todo table Column names
    public static final String KEY_TODOID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_COMPLETED = "completed";

    // todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO
            + " ( " + KEY_TODOID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE + " TEXT, "
            + KEY_DETAIL + " TEXT, "
            + KEY_COMPLETED + " INTEGER DEFAULT 0)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TODO);
        Log.d(LOGTAG, "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        Log.d(LOGTAG, "Table Deleted");
        // recreate new tables

        DATABASE_VERSION += 1;
        onCreate(sqLiteDatabase);
    }

    // closing database
    public void closeDB()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
