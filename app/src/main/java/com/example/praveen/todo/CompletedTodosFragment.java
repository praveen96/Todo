package com.example.praveen.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Praveen on 11/5/2016.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CompletedTodosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// display the list of completed todos

public class CompletedTodosFragment extends Fragment {
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

    private static Callbacks mCallbacks;

    private TodoDbOperations db;

    private Paint p = new Paint();

//    private OnFragmentInteractionListener mListener;

    public CompletedTodosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedTodosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedTodosFragment newInstance(String param1, String param2) {
        CompletedTodosFragment fragment = new CompletedTodosFragment();
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
        void onTodoSelected(Todo todo);     // to open up the completedtodos viewpager from completedtodos activity
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);       // to show the menu in toolbar
        db = new TodoDbOperations(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_todos, container, false);
        // setup the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.completedToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // to go back to parent activity on pressing the back arrow in the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Back clicked!",     Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });

        // get all the completed todos
        todosList = db.getAllCompletedTodos();
        // setup the recyclerView to show the list of completed todos
        recyclerView = (RecyclerView) view.findViewById(R.id.completed_todo_list_recycler_view);
        mAdapter = new TodosAdapter(todosList, getActivity().getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
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

    // snackbar to restore deleted todo
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
        /*if(!snackbar.isShown()) {
            Toast.makeText(getActivity().getApplicationContext(), "DELETE KRING!!!", Toast.LENGTH_SHORT).show();
            db.deleteTodo(todo.getId());
        }*/
    }

    private void setupSwipeOperations(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
//                    onItemRemove(viewHolder);
                    // show undo option to restore deleted todo
                    int swipedPosition = viewHolder.getAdapterPosition();
                    TodosAdapter adapter = (TodosAdapter) recyclerView.getAdapter();
                    adapter.pendingRemoval(swipedPosition);
                    /*db.deleteTodo(todosList.get(position).getId());
                    todosList.remove(position);
                    recyclerView.getAdapter().notifyItemRemoved(position);
                    recyclerView.getAdapter().notifyItemRangeChanged(position, todosList.size());*/
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    // to make the delete sign
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    p.setColor(Color.parseColor("#FF0000"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background,p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2.5F*width ,(float) itemView.getTop() + 0.7F*width,(float) itemView.getRight() - 0.75F*width,(float)itemView.getBottom() - 0.7F*width);
                    c.drawBitmap(icon,null,icon_dest,p);

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
        // save the scrollposition of recyclerView
        state.putParcelable("LIST_STATE", recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // restore the scroll position of recyclerView
            Parcelable listState = savedInstanceState.getParcelable("LIST_STATE");
            if (listState != null)
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
        mCallbacks = (Callbacks)getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        todosList.clear();
        List<Todo> newTodosList = new ArrayList<>();
        newTodosList = db.getAllCompletedTodos();
        todosList.addAll(newTodosList);
        newTodosList.clear();
//        System.out.println("ONRESUME!!!");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
