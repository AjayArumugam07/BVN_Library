package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;

public class HomeScreen extends DrawerMenu {
    SearchView searchView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.home_screen, contentFrameLayout);

        searchView = (SearchView) findViewById(R.id.searchBar);
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(HomeScreen.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinnerNames));
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(listAdapter);
    }
}
