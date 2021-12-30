package com.technologyg.taxiiiclient.includes;

import com.technologyg.taxiiiclient.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String title){
        Toolbar mToolbar = activity.findViewById(R.id.Toolbar);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle(title);
    }
}
