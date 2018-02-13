package com.imaginationcreators.bvnlibrary;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search {


    public String[][] localDatabse = new String[3][3];
    private ArrayList<Books> searchResults;

    private int numberOfResults;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("https://bvnlibrary-a0e90.firebaseio.com/").child("Books");

    public Search() {Log.d("My item list","test");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    Books post = new Books();
                    post.setAuthorFirstName(ds.child("Mystery").getValue(Books.class).getAuthorFirstName());
                    Log.d("TestAuthor", "Hello");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Log.d("My item list","hei");
            }
        });
    }

    public String[][] getLocalDatabase() {
        return localDatabse;
    }

    public ArrayList<Books> getSearchResults() {
        return searchResults;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public ArrayList searchByTitle(String title)
    {

        return searchResults;
    }
    public ArrayList searchByAuthor(String author)
    {

        return searchResults;
    }
    public ArrayList searchByISBN(String ISBN)
    {

        return searchResults;
    }
}
