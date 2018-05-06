package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends DrawerMenu {
    // Setup views
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

        ViewPager viewPager = findViewById(R.id.viewPager);
        PageViewAdapter pageViewAdapter = new PageViewAdapter(this);
        viewPager.setAdapter(pageViewAdapter);

        AssignBook assignBook = new AssignBook();
        //assignBook.getOverdueBooks();

        // Setup search bar and spinner dropdown
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerNames));
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
        searchView.setOnQueryTextListener(searchListener);
    }

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