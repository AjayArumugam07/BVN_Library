package com.imaginationcreators.bvnlibrary;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.ArrayList;

public class MyAccount extends DrawerMenu {
    // Create views
    RecyclerView recyclerView;
    MyAccountAdapter adapter;
    private TextView noBooksChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add drawer menu option
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.my_account, contentFrameLayout);

        // Find views
        noBooksChecked = (TextView) findViewById(R.id.noCheckOutBooks);

        // Get all the books from database for a search sample
        final AssignBook assignBook = new AssignBook();
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();
        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                // Set text of no books checked out
                noBooksChecked.setText("No Books Checked Out");

                // Get all the book's the user has checked out
                assignBook.getUserCheckedoutBooks(search.searchSample, false);
                assignBook.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        // User has books so no longer display message saying user has no books
                        noBooksChecked.setText("");

                        // Find recycler view and set it up
                        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyAccount.this));

                        // Search for any overdue books user has
                        final AssignBook assignBook2 = new AssignBook();
                        assignBook2.getOverdueBooks();
                        assignBook2.dbSource2.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                            @Override
                            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                                // Create adapter for recycler view based on the user's books and overdue books
                                adapter = new MyAccountAdapter(MyAccount.this, assignBook.userBooks, assignBook2.overdueBooks);

                                // Attach adapter to recycler view
                                recyclerView.setAdapter(adapter);

                                // Reset db sources
                                assignBook.dbSource = new TaskCompletionSource<>();
                                assignBook2.dbSource2 = new TaskCompletionSource<>();
                            }
                        });
                    }

                });
            }
        });
    }
}




