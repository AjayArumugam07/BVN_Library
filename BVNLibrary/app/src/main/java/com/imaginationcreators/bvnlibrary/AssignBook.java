package com.imaginationcreators.bvnlibrary;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
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
    public String dueDate;
    int j = 0;

    public TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();
    public TaskCompletionSource<String> dbSource1 = new TaskCompletionSource<>();
    public TaskCompletionSource<String> dbSource2 = new TaskCompletionSource<>();

    public void checkoutReserveBook(final Books book) {
        if (book.getAvailablity().equalsIgnoreCase("Available")) {
            Log.d("Search", "T");
            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Checked Out").child("checkout").push().setValue(book.getTitle());
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Unavailable");
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("User Id").setValue(mAuth.getUid());
            Date date = new Date();
            date = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dateFormat.format(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DAY_OF_MONTH, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
            String output = sdf1.format(c.getTime());
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("Check Out Date").setValue(dateFormat.format(date));
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("Due Date").setValue(output);
        }

        else if (book.getAvailablity().equalsIgnoreCase(("Unavailable"))) {
            Log.d("Search", "T1");
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

    public void setButtonText(final BooksAdapter.BooksViewHolder holder, final List<Books> searchSample, final Books book) {
        if (book.getAvailablity().contains("Available")) {
            holder.reserveCheckout.setText("Checkout");
            holder.reserveCheckout.setEnabled(true);
            return;
        }
        /*
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();
        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {

                dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        for(int i = 0; i < reservedBooks.size(); i++){
                            if(reservedBooks.get(i) == book){
                                holder.reserveCheckout.setText("Return");
                                holder.reserveCheckout.setEnabled(true);
                                return;
                            }
                        }
                    }
                });
            }
        });*/


        holder.reserveCheckout.setText("Not Available");
        holder.reserveCheckout.setEnabled(false);
        return;
    }

    public final ArrayList<Books> reservedBooks = new ArrayList<Books>();
    public List<Books> getUserCheckedoutBooks(final List<Books> searchSample) {

        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("12345678910", dataSnapshot1.getValue().toString());
                    for (int i = 0; i < searchSample.size(); i++) {
                        if (dataSnapshot1.getValue().toString().contains(searchSample.get(i).getTitle())) {
                            reservedBooks.add(searchSample.get(i));
                            Log.d("123456789", searchSample.get(i).getTitle());
                        }


                    }

                }
                Log.d("123456789", reservedBooks.size() + " hi");

                dbSource.setResult(reservedBooks);

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
        return reservedBooks;
    }
    public void dueDate(Books book) {
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().contains("Due Date")) {
                    dueDate = "Due Date: " + dataSnapshot.getValue().toString();
                    dbSource1.setResult(dueDate);
                }
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
    public void returnBook(final Books book){
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Available");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").removeValue();
        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").child("checkout").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue().toString().contains(book.getTitle()))
                {
                    dataSnapshot.getRef().removeValue();
                }
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
