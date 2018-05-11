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

public class Search {                                                       // Search Class: Iterate through database to find book



    public ArrayList<Books> searchResults = new ArrayList<>();              // Query Result Array List
    public ArrayList<Books> searchSample = new ArrayList<>();               // Array List of all books in library


    private int i = 0;
    public TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();      // declaring tasks

    public TaskCompletionSource<ArrayList<Books>> dbSource1 = new TaskCompletionSource<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Books"); // Firebase database reference

    public ArrayList searchFromSample(final String title, final String searchType)      // search user input in database of books
    {

        setLocalDatabaseForSearchTitle();                                               // Compile list of books from firebase database


        dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {                              // Completion listener for task


                    for(int j = 0; j < searchSample.size(); j++)                           // iterate through list of all books
                    {
                        if (searchType.equals("Title") && searchSample.get(j).getTitle().toLowerCase().contains(title))     // check if the user input matches any of the database book titles
                        {

                            if(!searchResults.contains(searchSample.get(j))){
                                searchResults.add(searchSample.get(j));                                             // add the book to results

                            }


                        }
                        else if (searchType.equals("Author") && (searchSample.get(j).getAuthorFirstName().toLowerCase().contains(title) || searchSample.get(j).getAuthorLastName().toLowerCase().contains(title)))  // check if user input matches any of the author first and last names in the firebase database
                        {
                            searchResults.add(searchSample.get(j));     // add any of the matched books to the results List

                        }
                        else if (searchType.equals("Any") && ((searchSample.get(j).getAuthorFirstName().toLowerCase().contains(title) ||        // check if user input matches the author name or title of a book
                                searchSample.get(j).getAuthorLastName().toLowerCase().contains(title)) ||
                                searchSample.get(j).getTitle().toLowerCase().contains(title)))
                        {
                            searchResults.add(searchSample.get(j));                 // add any of the matched books to the results List

                        }
                    }

                dbSource1.setResult(searchResults);                                  // Complete Task by setting result
                dbSource1 = new TaskCompletionSource<>();
            }
        });
        return searchResults;
    }

    public void setLocalDatabaseForSearchTitle()                                                                        // Create a local version of the online database
    {

        databaseReference.addChildEventListener(new ChildEventListener() {                                               // Add a listener to the children of the database
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())                                              // loop through all the children
                {

                    searchSample.add(new Books(dataSnapshot1.getKey(),                                                      // add a new book to the local version of database
                            dataSnapshot1.child("Author_First_Name").getValue().toString(),
                            dataSnapshot1.child("Author_Last_Name").getValue().toString(),
                            dataSnapshot1.child("ISBN").getValue().toString(),
                            dataSnapshot1.child("Availablility").getValue().toString(),
                            dataSnapshot1.child("Reference").getValue().toString()));
                }

                    dbSource.setResult(searchSample);                                                                       // completing the task by setting result
                dbSource = new TaskCompletionSource<>();

            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
