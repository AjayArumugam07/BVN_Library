package com.imaginationcreators.bvnlibrary;

import android.util.Log;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Ajay_Krish on 2/25/2018.
 */

public class AssignBook {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void checkoutReserveBook(final Books book) {
        if (book.getAvailablity().equalsIgnoreCase("Available")) {
            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Checked Out").child("Issued").setValue(book.getTitle());
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Unavailable");
        }

        if (book.getAvailablity().equalsIgnoreCase(("Unavailable"))) {
            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Reserved Books").child("Reserved").setValue(book.getTitle());
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("Number Of Holds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String numberOfHolds = dataSnapshot.getValue().toString();
                    database.getReference().child("Books").child("Book").child(book.getTitle()).child("Number Of Holds").removeEventListener(this);
                    int numberOfHoldsInteger = Integer.parseInt(numberOfHolds);
                    numberOfHoldsInteger++;
                    database.getReference().child("Books").child("Book").child(book.getTitle()).child("Number Of Holds").setValue(numberOfHoldsInteger);
                    database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").child(numberOfHolds).setValue(mAuth.getUid());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Log.d("Search", "Text in Database doesn't match: " + book.getAvailablity());
        }

    }

    public String textToSet(final List<Books> searchSample, Books book) {
        Log.d(TAG, "textToSet: " + mAuth.getUid());
        if (book.getAvailablity().contains("Available")) {
            return "Checkout";
        }
        List<Books> userReservedBooks = getUserReservedBooks(searchSample);
        List<Books> userCheckedBooks = getUserCheckedoutBooks(searchSample);

        for(int i = 0; i < userReservedBooks.size(); i++){
            if(userReservedBooks.get(i).getTitle().equals(book.getTitle())){
                return "Reserved";
            }
        }

        for(int i = 0; i < userCheckedBooks.size(); i++){
            if(userCheckedBooks.get(i) == book){
                return "Issued";
            }
        }

        return "Reserve";
}

    public List<Books> getUserReservedBooks(final List<Books> searchSample) {
        final ArrayList<Books> reservedBooks = new ArrayList<Books>();
        database.getReference().child("Users").child(mAuth.getUid()).child("Reserved Books").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for (int i = 0; i < searchSample.size(); i++) {
                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
                            reservedBooks.add(searchSample.get(i));
                        }
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for (int i = 0; i < searchSample.size(); i++) {
                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
                            reservedBooks.remove(searchSample.get(i));
                        }
                    }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return reservedBooks;
    }

    public List<Books> getUserCheckedoutBooks(final List<Books> searchSample) {
        final ArrayList<Books> reservedBooks = new ArrayList<Books>();
        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for (int i = 0; i < searchSample.size(); i++) {
                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
                            reservedBooks.add(searchSample.get(i));
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for (int i = 0; i < searchSample.size(); i++) {
                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
                            reservedBooks.remove(searchSample.get(i));
                        }
                    }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return reservedBooks;
    }
}
