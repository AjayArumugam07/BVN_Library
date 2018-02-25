package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class HomeScreen extends DrawerMenu {
    SearchView searchView;
    Spinner spinner;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.home_screen, contentFrameLayout);


        searchView = (SearchView) findViewById(R.id.searchBar);
        spinner = (Spinner) findViewById(R.id.spinner);
        signOutButton = (Button) findViewById(R.id.sign_Out_Button);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                startActivity(new Intent(HomeScreen.this, AuthScreen.class));
            }
        });


        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerNames));
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        searchView.setOnQueryTextListener(searchListener);
    }

    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // TODO call search class with query as search string and searchBy as search parameter

            Intent openSearch = new Intent(HomeScreen.this, SearchScreen.class);
            openSearch.putExtra("TitleTag", query);
            openSearch.putExtra("SearchByTag", spinner.getSelectedItem().toString());

            startActivity(new Intent(openSearch));
            finish();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // TODO update search suggestions here for nationals
            return false;
        }
    };
}
