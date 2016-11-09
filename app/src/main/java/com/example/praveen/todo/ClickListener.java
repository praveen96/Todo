package com.example.praveen.todo;

import android.view.View;

/**
 * Created by Praveen on 11/5/2016.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
