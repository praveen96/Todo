package com.example.praveen.todo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
 * {@link CompletedTodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompletedTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// Fragment to display a completed todo

public class CompletedTodoFragment extends Fragment {
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

    public CompletedTodoFragment() {
        // Required empty public constructor
    }

    // * @param param2 Parameter 2.
    //**
     /* Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment CompletedTodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static CompletedTodoFragment newInstance(String param1, String param2) {
        CompletedTodoFragment  fragment = new CompletedTodoFragment ();
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
            mParam1 = getArguments().getInt(ARG_PARAM1);
            todo = db.getTodo(mParam1);
            /*if(todo != null)
                System.out.println("NOT NULL!!!");
            else
                System.out.println("NULL!!! " + mParam1);*/
        }
        setHasOptionsMenu(true);       // to show the menu on toolbar
    }

    public static CompletedTodoFragment newInstance(int param1) {
//        System.out.println("MAMLA " + param1);
        // create a fragment instance to display the clicked todo
        CompletedTodoFragment fragment = new CompletedTodoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_todo, container, false);

        // setup the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.completedViewPagerToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // back arrow to go back to parent activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Back clicked!",     Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView titleField = (TextView) view.findViewById(R.id.completedViewPagerTodoTitle);
        final TextView detailField = (TextView) view.findViewById(R.id.completedViewPagerTodoDetail);

        titleField.setText(todo.getTitle());
        detailField.setText(todo.getDetail());

        return view;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("ID BC - " + item.getItemId());
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
