package com.example.praveen.todo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praveen on 11/5/2016.
 */

public class CompletedTodoPagerActivity extends ActionBarActivity implements TodoListFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private List<Todo> todosList = new ArrayList<>();
    public static final String EXTRA_TODO_ID = "TodoID";
    private TodoDbOperations db;

    public static Intent newIntent(Context packageContext, int todoId) {
        Intent intent = new Intent(packageContext, CompletedTodoPagerActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyMaterialTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo_pager);

        db = new TodoDbOperations(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.completedViewpager);

        // get all completed todos
        todosList = db.getAllCompletedTodos();

        FragmentManager fm = getSupportFragmentManager(); // get the activities instance of fragment manager
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm){ //set adapter to be an unnamed instance of FSPA(agent), manages convo w/ ViewPager
            @Override
            public int getCount() { //returns how many items are in the array list
                return todosList.size();
            }
            //What is agent doing? ADding fragments you return to your activity and helping ViewPAger identify fragments views to be placed correctly.
            @Override
            public Fragment getItem(int pos) { //where magic happens, fetches the Todo instance for the given position in the dataset, creates and returns
                //properly configured CompletedTodoFragment.
                Todo todo = todosList.get(pos);
//                System.out.println("ID " + todo.getId());
                return CompletedTodoFragment.newInstance(todo.getId());
            }
        });

//        viewPager.getAdapter().notifyDataSetChanged();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) { }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }

            public void onPageSelected(int pos) {
                Todo todo = todosList.get(pos);
                if (todo.getTitle() != null) {
//                    setTitle(todo.getTitle());
                    setTitle("Todo");
                }
            }
        });

        int  todoID = getIntent().getIntExtra(EXTRA_TODO_ID, 0);
//        System.out.println("ORIGINAL " + todoID);
        for (int i = 0; i < todosList.size(); i++) {
            if(todosList.get(i).getId() == todoID) {
                setTitle("Todo");
//                System.out.println("NO. OF MAMLAS " + todosList.size());
//                System.out.println("SETTING " + i);
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
//        Toast toast = Toast.makeText(this, "Wheeee!",Toast.LENGTH_SHORT);
//        toast.show();
    }

}
