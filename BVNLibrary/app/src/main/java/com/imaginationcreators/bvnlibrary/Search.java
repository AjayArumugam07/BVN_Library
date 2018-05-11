package com.imaginationcreators.bvnlibrary;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

// Search Class: Iterate through database to find book
public class Search {
    // Query Result Array List
    public ArrayList<Books> searchResults = new ArrayList<>();

    // Array List of all books in library
    public ArrayList<Books> searchSample = new ArrayList<>();

    // Declaring tasks
    public TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();
    public TaskCompletionSource<ArrayList<Books>> dbSource1 = new TaskCompletionSource<>();

    // Firebase database reference
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");

    // Search based on user input in database of books
    public ArrayList searchFromSample(final String title, final String searchType) {
        // Compile list of books from Firebase database
        setLocalDatabaseForSearchTitle();

        // Completion listener for task
        dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                // Iterate through list of all books
                for (int j = 0; j < searchSample.size(); j++) {
                    // Check if the user input matches any of the database book titles
                    if (searchType.equals("Title") && searchSample.get(j).getTitle().toLowerCase().contains(title)) {
                        if (!searchResults.contains(searchSample.get(j))) {
                            // Add the book to results
                            searchResults.add(searchSample.get(j));
                        }
                    } else if (searchType.equals("Author") && (searchSample.get(j).getAuthorFirstName().toLowerCase().contains(title) || searchSample.get(j).getAuthorLastName().toLowerCase().contains(title)))  // check if user input matches any of the author first and last names in the firebase database
                    {
                        // Add any of the matched books to the results List
                        searchResults.add(searchSample.get(j));

                    }

                    // Check if user input matches the author name or title of a book
                    else if (searchType.equals("Any") && ((searchSample.get(j).getAuthorFirstName().toLowerCase().contains(title) ||
                            searchSample.get(j).getAuthorLastName().toLowerCase().contains(title)) ||
                            searchSample.get(j).getTitle().toLowerCase().contains(title))) {
                        // Add any of the matched books to the results List
                        searchResults.add(searchSample.get(j));
                    }
                }

                // Complete Task by setting result
                dbSource1.setResult(searchResults);
                dbSource1 = new TaskCompletionSource<>();
            }
        });
        return searchResults;
    }

    // Create a local version of the online database
    public void setLocalDatabaseForSearchTitle() {
        // Add a listener to the children of the database
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Loop through all the children
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Add a new book to the local version of database
                    searchSample.add(new Books(dataSnapshot1.getKey(),
                            dataSnapshot1.child("Author_First_Name").getValue().toString(),
                            dataSnapshot1.child("Author_Last_Name").getValue().toString(),
                            dataSnapshot1.child("ISBN").getValue().toString(),
                            dataSnapshot1.child("Availablility").getValue().toString(),
                            dataSnapshot1.child("Reference").getValue().toString()));
                }

                // Completing the task by setting result
                dbSource.setResult(searchSample);
                dbSource = new TaskCompletionSource<>();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
