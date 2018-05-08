package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchScreen extends DrawerMenu {
    // Declare views to display books
    RecyclerView recyclerView;
    BooksAdapter adapter;

    TextView noResults;

    private ArrayList<Books> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add drawer menu option
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.search_screen, contentFrameLayout);

        noResults = (TextView) findViewById(R.id.noResults);

        // Create search object
        final Search search = new Search();
        // Call search method passing in search query and filter based on strings passed from intent
        books = search.searchFromSample(getIntent().getStringExtra("TitleTag"), getIntent().getStringExtra("SearchByTag"));

        // Add listener for when search is complete
        search.dbSource1.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                if(books.size() == 0){
                    noResults.setText("No search results");
                }
                else{
                    noResults.setText("");
                }

                // Set recycler view properties
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchScreen.this));

                // Add adapter to recycler view
                adapter = new BooksAdapter(SearchScreen.this, books);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
