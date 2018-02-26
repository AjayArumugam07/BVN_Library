package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DrawerMenu extends AppCompatActivity {
    private static final String TAG = "DrawerMenu";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private android.support.v7.widget.Toolbar toolbar;

    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_menu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawerView);
        View headerView = navigationView.getHeaderView(0);

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
                    case R.id.home:
                        startActivity(new Intent(DrawerMenu.this, HomeScreen.class));
                        finish();
                        break;
                    case R.id.reservations:
                        startActivity(new Intent(DrawerMenu.this, Reservations.class));
                        finish();
                        break;
                    case R.id.map:
                        startActivity(new Intent(DrawerMenu.this, Map.class));
                        finish();
                        break;
                    case R.id.librarians:
                        startActivity(new Intent(DrawerMenu.this, Librarians.class));
                        finish();
                        break;
                    case R.id.about:
                        startActivity(new Intent(DrawerMenu.this, AboutApp.class));
                        finish();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(DrawerMenu.this, AuthScreen.class));
                        finish();
                        break;
                }
                return false;
            }
        });

        name = (TextView) headerView.findViewById(R.id.name);
        email = (TextView) headerView.findViewById(R.id.email);

        if(user != null){
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
}


