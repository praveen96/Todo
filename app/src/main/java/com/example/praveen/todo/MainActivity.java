package com.example.praveen.todo;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Praveen on 11/5/2016.
 */

public class MainActivity extends AppCompatActivity implements TodoListFragment.OnFragmentInteractionListener, TodoListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyMaterialTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new TodoListFragment());
        fragmentTransaction.commit();*/
//        if (savedInstanceState == null) {
            // use the TodoListFragment to display all the todos and toolbar
            getSupportFragmentManager().beginTransaction().add(R.id.container, new TodoListFragment()).commit();
//        }
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
    public void onFragmentInteraction(Uri uri) {

    }

    public void onTodoSelected (Todo todo) {

            Intent intent = TodoPagerActivity.newIntent(MainActivity.this, todo.getId());
            startActivity(intent);
    }

}
