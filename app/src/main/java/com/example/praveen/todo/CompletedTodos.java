package com.example.praveen.todo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Praveen on 11/5/2016.
 */

// Activity to show the list of completed todos

public class CompletedTodos extends AppCompatActivity implements CompletedTodosFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyMaterialTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todos);

        // get the fragment to display all the completed todos
        getSupportFragmentManager().beginTransaction().add(R.id.completedContainer, new CompletedTodosFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onTodoSelected(Todo todo) {
        // open the completedtodos viewpager to show the clicked todo
        Intent intent = CompletedTodoPagerActivity.newIntent(CompletedTodos.this, todo.getId());
        startActivity(intent);
    }
}
