package com.example.praveen.todo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Praveen on 11/5/2016.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mParam1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    /*private String mParam1;
    private String mParam2;*/

    private int mParam1;

//    private OnFragmentInteractionListener mListener;

    private TodoDbOperations db;
    private Todo todo;

    public TodoFragment() {
        // Required empty public constructor
    }

    // * @param param2 Parameter 2.
    //**
     /* Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static TodoFragment newInstance(String param1, String param2) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TodoDbOperations(getActivity().getApplicationContext());
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
            mParam1 = getArguments().getInt(ARG_PARAM1);    // retrieve the id of todo to open up
            todo = db.getTodo(mParam1);
            /*if(todo != null)
                System.out.println("NOT NULL!!!");
            else
                System.out.println("NULL!!! " + mParam1);*/
        }
        setHasOptionsMenu(true);    // to show menu on toolbar
    }

    public static TodoFragment newInstance(int param1) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        // Create the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.viewPagerToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // to get back to the previous activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Back clicked!",     Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // doesn't work

        // setting up the layout elements
        final TextView titleField = (TextView) view.findViewById(R.id.viewPagerTodoTitle);
        final TextView detailField = (TextView) view.findViewById(R.id.viewPagerTodoDetail);

        titleField.setText(todo.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // update the todo on keypress
                Todo newTodo = new Todo(todo.getId(), titleField.getText().toString(), detailField.getText().toString());
//                Toast.makeText(getActivity().getApplicationContext(), "Updating!", Toast.LENGTH_SHORT).show();
                db.updateTodo(newTodo);
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(getActivity().getApplicationContext(), "After TextChanged!", Toast.LENGTH_SHORT).show();
            }
        });

        detailField.setText(todo.getDetail());
        detailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Todo newTodo = new Todo(todo.getId(), titleField.getText().toString(), detailField.getText().toString());
                db.updateTodo(newTodo);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                db.createTodo(titleField.getText().toString(), detailField.getText().toString());
                Toast.makeText(getActivity().getApplicationContext(), "BACK", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public Todo getTodo() {
        return todo;
    }
}
