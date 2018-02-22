package com.imaginationcreators.bvnlibrary;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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


        search.dbSource1.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchScreen.this));

                adapter = new BooksAdapter(SearchScreen.this, books);
                recyclerView.setAdapter(adapter);
            }
        });
    }


}
