package com.technologyg.taxiii.includes;

import com.technologyg.taxiii.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyToolbar {
    public static void show(AppCompatActivity activity, String title, boolean upButton){
        Toolbar mToolbar = activity.findViewById(R.id.Toolbar);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
