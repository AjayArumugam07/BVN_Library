package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toolbar;

public class DrawerMenu extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_menu);

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawerView);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.drawerAction);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerMenu);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.map:
                        Intent i = new Intent(DrawerMenu.this, Map.class);
                        startActivity(i);
                        break;


                }
                return false;

            }

            /*
            Rajin and Ajay to add the click on the map to bring it up, i had to get rid of this

            protected void onPostCreate(@Nullable Bundle savedInstanceState) {
                super.onPostCreate(savedInstanceState);
                toggle.syncState();
            }

            I dont know what it does but if it does something special let me know



             */



        });
    }
}


