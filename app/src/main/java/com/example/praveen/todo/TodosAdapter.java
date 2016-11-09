package com.example.praveen.todo;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praveen on 11/5/2016.
 */

// recyclerView adaptor to use to show the titles of todos
public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.MyViewHolder> {
    private static final int PENDING_REMOVAL_TIMEOUT = 2000; // 2sec

    private List<Todo> todosList;
    List<Todo> todosListPendingRemoval;
    boolean undoOn = true;
    Context context;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Todo, Runnable> pendingRunnables = new HashMap<>();
    TodoDbOperations db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleField;
        public TextView undoButton;
//        , detailField;
        public MyViewHolder(View view) {
            super(view);
            titleField = (TextView) view.findViewById(R.id.todoTitle);
            undoButton = (TextView) itemView.findViewById(R.id.undo_button);
//            detailField = (TextView) view.findViewById(R.id.todoDetail);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if(view.findViewById(R.id.todoTitle) != null) {
//                        TodoListFragment.onRowClick(getAdapterPosition());
                    }*/
                    // to open the appropriate viewpager activity on clicking a recyclerview item
                    if(db.isCompleted(todosList.get(getAdapterPosition()).getId()) == 1) {
//                        System.out.println("ROW!!! COMPLETE " + getAdapterPosition());
                        CompletedTodosFragment.onRowClick(getAdapterPosition());
                    } else {
//                        System.out.println("ROW!!! PENDING" + getAdapterPosition());
                        TodoListFragment.onRowClick(getAdapterPosition());
                    }

//                    System.out.println("ROW!!! " + getAdapterPosition());
                }
            });
        }
        /*
        Doesn't work!
        @Override
        public void onClick(View view) {
            *//*if(view.getId() == R.id.undo_button) {
                System.out.println("BUTTON!!!");
            } else {*//*
                System.out.println("ROW???");
            //}
        }*/
    }


    public TodosAdapter(List<Todo> todosList, Context context) {
        this.todosList = todosList;
        this.context = context;
        this.todosListPendingRemoval = new ArrayList<>();
        this.db = new TodoDbOperations(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*Todo todo = todosList.get(position);
        holder.titleField.setText(todo.getTitle());
//        holder.detailField.setText(todo.getDetail());*/

        final MyViewHolder viewHolder = holder;
        final Todo item = todosList.get(position);

        if (todosListPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.titleField.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println("Button!!!" + viewHolder.getAdapterPosition());
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    todosListPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(todosList.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.rgb(249, 249, 249));
            viewHolder.titleField.setVisibility(View.VISIBLE);
            viewHolder.titleField.setText(item.getTitle());
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return todosList.size();
    }

    public void pendingRemoval(final int position) {
        final Todo item = todosList.get(position);
        if (!todosListPendingRemoval.contains(item)) {
            todosListPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(position);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
//        System.out.println("REMOVING  - " + position + " " + todosList.size());
        Todo item = todosList.get(position);
        if (todosListPendingRemoval.contains(item)) {
            todosListPendingRemoval.remove(item);
        }
        if (todosList.contains(item)) {
//            System.out.println("DEL!!!");
            db.deleteTodo(todosList.get(position).getId());
//            System.out.println("DEL DONE!!!");
            todosList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Todo item = todosList.get(position);
        return todosListPendingRemoval.contains(item);
    }

}
