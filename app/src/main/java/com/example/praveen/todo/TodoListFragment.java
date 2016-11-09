package com.example.praveen.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praveen on 11/5/2016.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TodoListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int restore;

    private static List<Todo> todosList = new ArrayList<>();
    private List<Todo> todosToDelete = new ArrayList<>();
    private RecyclerView recyclerView;
    private TodosAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    private TodoDbOperations db;

    private static Callbacks mCallbacks;

    private Paint p = new Paint();

    public TodoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoListFragment newInstance(String param1, String param2) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void onRowClick(int position) {
        Todo todo = todosList.get(position);
//                Toast.makeText(getActivity().getApplicationContext(), todo.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
//                Intent viewPagerIntent = new Intent(getActivity(), TodoPagerActivity.class);
        /*Intent intent = TodoPagerActivity.newIntent(getActivity(), todo.getId());
        startActivity(intent);*/
        mCallbacks.onTodoSelected(todo);    // to open up viewpager activity from mainactivity
    }

    public interface Callbacks {
        // call the todoViewPager from Mainactivity
        void onTodoSelected(Todo todo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);        // to show the menu in toolbar
        db = new TodoDbOperations(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        // Create the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        // retrieve all the pending todos to display on the main screen
        todosList = db.getAllPendingTodos();
        // setup the recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.todo_list_recycler_view);
        mAdapter = new TodosAdapter(todosList, getActivity().getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200);
        itemAnimator.setRemoveDuration(200);
        recyclerView.setItemAnimator(itemAnimator);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setupSwipeOperations();
        return view;
    }

    // snackbar to restore a deleted todo
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Todo todo = todosList.get(adapterPosition);
        restore = 0;
        Snackbar snackbar = Snackbar
                .make(recyclerView, "Todo Removed!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restore = 1;
                        todosList.add(adapterPosition, todo);
                        recyclerView.getAdapter().notifyItemInserted(adapterPosition);
                        recyclerView.scrollToPosition(adapterPosition);
                        todosToDelete.remove(todo);
                    }
                })
                .setDuration(3000);
        snackbar.show();
        todosList.remove(adapterPosition);
        recyclerView.getAdapter().notifyItemRemoved(adapterPosition);
        todosToDelete.add(todo);
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                //see Snackbar.Callback docs for event details
//                Toast.makeText(getActivity().getApplicationContext(), "DELETING!!!", Toast.LENGTH_SHORT).show();
                if(restore == 0)
                    db.deleteTodo(todo.getId());
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
    }

    private void setupSwipeOperations(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
//                    onItemRemove(viewHolder);
                    // show undo option on left swipe
                    int swipedPosition = viewHolder.getAdapterPosition();
                    TodosAdapter adapter = (TodosAdapter) recyclerView.getAdapter();
                    adapter.pendingRemoval(swipedPosition);
                    /*onItemRemove(viewHolder, recyclerView);
                    db.deleteTodo(todosList.get(position).getId());
                    todosList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    recyclerView.getAdapter().notifyItemRangeChanged(position, todosList.size());*/
                } else {
                    db.completeTodo(todosList.get(position).getId());
                    todosList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    recyclerView.getAdapter().notifyItemRangeChanged(position, todosList.size());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        // make the check sign
//                        p.setColor(Color.parseColor("#388E3C"));
                        p.setColor(Color.parseColor("#00c500"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_done);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + 0.75F*width ,(float) itemView.getTop() + 0.7F*width,(float) itemView.getLeft()+ 2.5F*width,(float)itemView.getBottom() - 0.7F*width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        // make the delete sign
//                        p.setColor(Color.parseColor("#D32F2F"));
                        p.setColor(Color.parseColor("#FF0000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2.5F*width ,(float) itemView.getTop() + 0.7F*width,(float) itemView.getRight() - 0.75F*width,(float)itemView.getBottom() - 0.7F*width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // save the scrolled state of recyclerview
        state.putParcelable("LIST_STATE", recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable("LIST_STATE");  // restore the scroll position of recyclerview
            if (listState != null)
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    /*private void prepareTodosData() {
        *//*Todo todo = new Todo(1, "Mad Max: Fury Road", "Action & Adventure");
        todosList.add(todo);

        todo = new Todo(2, "Inside Out", "Animation, Kids & Family");
        todosList.add(todo);

        todo = new Todo(3, "Star Wars: Episode VII - The Force Awakens", "Action");
        todosList.add(todo);*//*

        int id = db.createTodo("Mad Max: Fury Road", "Action & Adventure");
        todosList.add(db.getTodo(id));
        id = db.createTodo("Inside Out", "Animation, Kids & Family");
        todosList.add(db.getTodo(id));
        id = db.createTodo("Star Wars: Episode VII - The Force Awakens", "Action");
        todosList.add(db.getTodo(id));

//        todosList = db.getAllPendingTodos();

        Toast.makeText(getActivity().getApplicationContext(), "Todos - " + todosList.size(), Toast.LENGTH_SHORT).show();

        mAdapter.notifyDataSetChanged();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mCallbacks = (Callbacks)getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        todosList.clear();
        List<Todo> newTodosList = new ArrayList<>();
        newTodosList = db.getAllPendingTodos();
        todosList.addAll(newTodosList);
        newTodosList.clear();
//        System.out.println("ONRESUME!!!");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
//            Log.i("ADD BUTTON", "CLICKED!");
            Intent newTodoIntent = new Intent(getActivity().getApplicationContext(), NewTodo.class);
            startActivity(newTodoIntent);
            return true;
        }
        if(id == R.id.action_done) {
//            Log.i("DONE BUTTON", "CLICKED!");
            Intent newTodoIntent = new Intent(getActivity().getApplicationContext(), CompletedTodos.class);
            startActivity(newTodoIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
