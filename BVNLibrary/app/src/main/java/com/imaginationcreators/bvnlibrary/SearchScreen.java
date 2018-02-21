package com.imaginationcreators.bvnlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SearchScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    BooksAdapter adapter;

    private ArrayList<Books> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        Search search = new Search();
        books = search.searchFromSample(getIntent().getStringExtra("TitleTag"), getIntent().getStringExtra("SearchByTag"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BooksAdapter(this, books);
        recyclerView.setAdapter(adapter);
    }
}
