package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO comment line 17 and uncomment line 18 to test auth screen
        setContentView(R.layout.drawer_menu);
        //startActivity(new Intent(this, AuthScreen.class));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMenu);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}


