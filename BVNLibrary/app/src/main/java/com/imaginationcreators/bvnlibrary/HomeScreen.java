package com.imaginationcreators.bvnlibrary;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class HomeScreen extends DrawerMenu {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.home_screen, contentFrameLayout);
    }
}
