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
import java.time.LocalDate;
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
    public String dueDate1;

    public TaskCompletionSource<ArrayList<Books>>dbSource = new TaskCompletionSource<>();
    public TaskCompletionSource<String> dbSource1 = new TaskCompletionSource<>();
    public TaskCompletionSource<ArrayList<Books>> dbSource2 = new TaskCompletionSource<>();
    final ArrayList<Books> overdueBooks = new ArrayList<Books>();
    public boolean check = false;
    public ChildEventListener childEventListener;

    public void checkoutReserveBook(final Books book) {
        if (book.getAvailablity().equalsIgnoreCase("Available")) {
            Log.d("donkey", "A");
            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Checked Out").child("checkout").child(book.getTitle()).setValue("1");
            Log.d("Search", "T7");
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
         else {
            Log.d("donkey", "Text in Database doesn't match: " + book.getAvailablity());
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
                        if (dataSnapshot1.getKey().toString().contains(searchSample.get(i).getTitle())) {
                            reservedBooks.add(searchSample.get(i));
                            Log.d("123456789", searchSample.get(i).getTitle());
                        }


                    }

                }
                Log.d("123456789", reservedBooks.size() + " hi");

                dbSource.setResult(reservedBooks);
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
        return reservedBooks;
    }
    public void dueDate(Books book) {
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().contains("Due Date")) {
                    dueDate = "Due Date: " + dataSnapshot.getValue().toString();
                    dueDate1 = dataSnapshot.getValue().toString();
                    dbSource1.setResult(dueDate);
                    dbSource1 = new TaskCompletionSource<>();
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
        Log.d(TAG, "returnBook: asdf");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Available");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").removeValue();
        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").child("checkout").child(book.getTitle()).removeValue();
    }

    public void getOverDueBooks() throws Exception{
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();

        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {


                getUserCheckedoutBooks(search.searchSample);
                dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        for(Books book : reservedBooks){
                            dueDate(book);
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                            Date date1 = new Date();
                            Date date3 = new Date();


                            Date date2 = new Date();
                            String s = sdf.format(date2);

                            try {
                                date1 = sdf.parse(dueDate1);
                                date3 = sdf.parse(s);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if(date1.after(date3)){
                                overdueBooks.add(book);
                            }

                        }

                        dbSource2.setResult(overdueBooks);
                        dbSource2 = new TaskCompletionSource<>();
                        dbSource = new TaskCompletionSource<>();

                    }

                });
            }
        });

    }
}
