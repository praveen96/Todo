package com.example.praveen.todo;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Praveen on 11/5/2016.
 */

// Activity to add a new todo

public class NewTodo extends AppCompatActivity {

    private Toolbar mToolbar;
    private TodoDbOperations db;
    TextView titleField, detailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyMaterialTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        db = new TodoDbOperations(getApplicationContext());

        titleField = (TextView) findViewById(R.id.newTodoTitle);
        detailField = (TextView) findViewById(R.id.newTodoDetail);

        // setup the toolbar
        mToolbar = (Toolbar) findViewById(R.id.newTodoToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // to go back to the parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // create the todo on pressing back arrow on toolbar
                if(titleField.getText().toString().equals("") && detailField.getText().toString().equals("")){

                } else {
                    db.createTodo(titleField.getText().toString(), detailField.getText().toString());
                }
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // create the todo on back button press
//        System.out.println("Content " + titleField.getText().toString() + " " + detailField.getText().toString());
        if(titleField.getText().toString().equals("") && detailField.getText().toString().equals("")){

        } else {
            db.createTodo(titleField.getText().toString(), detailField.getText().toString());
        }
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }

}
