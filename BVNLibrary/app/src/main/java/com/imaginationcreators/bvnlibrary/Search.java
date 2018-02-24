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

public class Search {
    private static final String TAG = "Search";


    private ArrayList<Books> searchResults = new ArrayList<>();
    private ArrayList<Books> searchSample = new ArrayList<>();


    private int i = 0;
    private TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();

    public TaskCompletionSource<ArrayList<Books>> dbSource1 = new TaskCompletionSource<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ArrayList<String> genre = new ArrayList<>();

    public Search()
    {
        Log.d(TAG, "Search Created");

        if(dbSource == null){
            Log.d(TAG, "Search: DB source is null");
        }
    }
    public ArrayList searchFromSample(String title, final String searchType)
    {
        Log.d(TAG, "1");
        setLocalDatabaseForSearchTitle();
        title = title.toLowerCase();
        final String[] titleArray = title.split(" ");
        dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                for(int i = 0; i < titleArray.length; i++)
                {
                    for(int j = 0; j < searchSample.size(); j++)
                    {
                        if (searchType.equals("Title") && searchSample.get(j).getTitle().toLowerCase().contains(titleArray[i]))
                        {
                            searchResults.add(searchSample.get(j));
                            Log.d(TAG, searchSample.get(j).getTitle());
                        }
                        if (searchType.equals("Author") && (searchSample.get(j).getAuthorFirstName().toLowerCase().contains(titleArray[i]) || searchSample.get(j).getAuthorLastName().toLowerCase().contains(titleArray[i])))
                        {
                            searchResults.add(searchSample.get(j));
                            Log.d(TAG, searchSample.get(j).getTitle());
                        }
                        if (searchType.equals("ISBN") && searchSample.get(j).getISBN().contains(titleArray[i]))
                        {
                            searchResults.add(searchSample.get(j));
                            Log.d(TAG, searchSample.get(j).getTitle());
                        }
                        if (searchType.equals("Any") && (searchSample.get(j).getAuthorFirstName().toLowerCase().contains(titleArray[i]) ||
                                searchSample.get(j).getAuthorLastName().toLowerCase().contains(titleArray[i])) ||
                                searchSample.get(j).getTitle().toLowerCase().contains(titleArray[i]) ||
                                searchSample.get(j).getISBN().contains(titleArray[i]))
                        {
                            searchResults.add(searchSample.get(j));
                            Log.d(TAG, searchSample.get(j).getTitle());
                        }
                    }
                }
                dbSource1.setResult(searchResults);
            }
        });
        return searchResults;
    }

    public void setLocalDatabaseForSearchTitle()
    {
        Log.d(TAG, "2");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                i = i + 1;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {

                    searchSample.add(new Books(dataSnapshot1.getKey(),
                            dataSnapshot1.child("Author_First_Name").getValue().toString(),
                            dataSnapshot1.child("Author_Last_Name").getValue().toString(),
                            dataSnapshot1.child("ISBN").getValue().toString(),
                            dataSnapshot1.child("Availablility").getValue().toString(),
                            dataSnapshot1.child("Reference").getValue().toString()));
                }
                if(i >= 5)
                {
                    dbSource.setResult(searchSample);
                }
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
