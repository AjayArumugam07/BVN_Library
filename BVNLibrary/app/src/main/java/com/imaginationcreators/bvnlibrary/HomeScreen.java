package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeScreen extends DrawerMenu {
    // Create views
    SearchView searchView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add drawer menu option
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.home_screen, contentFrameLayout);

        // Set ID's of fields
        searchView = (SearchView) findViewById(R.id.searchBar);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Create and set ViewPager's adapter
        ViewPager viewPager = findViewById(R.id.viewPager);
        PageViewAdapter pageViewAdapter = new PageViewAdapter(this);
        viewPager.setAdapter(pageViewAdapter);

        // Create and set tab's adapter
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);

        // Setup search bar and spinner dropdown
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerNames));
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        searchView.setOnQueryTextListener(searchListener);
    }

    // Set listener for when something is searched
    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // Start new activity when search entered passing search text and spinner value
            Intent openSearch = new Intent(HomeScreen.this, SearchScreen.class);
            openSearch.putExtra("TitleTag", query);
            openSearch.putExtra("SearchByTag", spinner.getSelectedItem().toString());
            startActivity(new Intent(openSearch));
            finish();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}