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
    int j = 0;

    public TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();

    public void checkoutReserveBook(final Books book) {
        if (book.getAvailablity().equalsIgnoreCase("Available")) {
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
//        List<Books> userReservedBooks = getUserReservedBooks(searchSample);
        List<Books> userCheckedBooks = getUserCheckedoutBooks(searchSample);

//        for(int i = 0; i < userReservedBooks.size(); i++){
//            if(userReservedBooks.get(i).getTitle().equals(book.getTitle())){
//                return "Reserved";
//            }
//        }

        for(int i = 0; i < userCheckedBooks.size(); i++){
            if(userCheckedBooks.get(i) == book){
                return "Issued";
            }
        }

        return "Reserve";
}

//    public List<Books> getUserReservedBooks(final List<Books> searchSample) {
//        final ArrayList<Books> reservedBooks = new ArrayList<Books>();
//        database.getReference().child("Users").child(mAuth.getUid()).child("Reserved Books").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                    for (int i = 0; i < searchSample.size(); i++) {
//                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
//                            reservedBooks.add(searchSample.get(i));
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                    for (int i = 0; i < searchSample.size(); i++) {
//                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
//                            reservedBooks.remove(searchSample.get(i));
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        return reservedBooks;
//    }
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
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                    for (int i = 0; i < searchSample.size(); i++) {
//                        if (dataSnapshot.getValue().toString().equals(searchSample.get(i).getTitle())) {
//                            reservedBooks.remove(searchSample.get(i));
//                        }
//                    }
//
//                }
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
//    public ArrayList<Books> overDueBooks = new ArrayList<Books>();
//    public void getOverdueBooks()
//    {
//        final Search search = new Search();
//        search.setLocalDatabaseForSearchTitle();
//        Log.d("12345", "check");
//        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
//            @Override
//            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
//
//                Log.d("overdue", "1");
//                getUserCheckedoutBooks(search.searchSample);
//
//
////                for(int i = 0; i < search.searchSample.size(); i++)
////                {
//                    dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
//                        @Override
//                        public void onComplete(@NonNull Task<ArrayList<Books>> task) {
//                            Log.d("overdue", reservedBooks.size() + " how are you doign");
//
//                                Log.d("overdue", reservedBooks.get(j).getTitle() + " this is working");
//                                database.getReference().child("Books").child("Book").child(reservedBooks.get(j).getTitle()).child("User Information").child("Due Date").addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
////                                        Log.d("overdue", reservedBooks.get(j).toString() + " hello");
//                                        for (j = 0; j < reservedBooks.size(); j++) {
//                                            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
//                                            Date date = new Date();
//                                            Date date1 = new Date();
//                                            Calendar c = Calendar.getInstance();
//                                            try {
//                                                date = sdf.parse(dataSnapshot.getValue().toString());
//                                                date1 = Calendar.getInstance().getTime();
//
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            Log.d("overdue", date.toString());
//                                            Log.d("overdue", date1.toString());
//                                            if (date.before(date1)) {
//
//                                                Log.d("overdue", "5" + reservedBooks.get(j).getTitle());
//                                            overDueBooks.add(reservedBooks.get(j));
//                                            Log.d("overdue" , overDueBooks.get(j).toString());
//
//                                            } else if (date.after(date1)) {
//                                                Log.d("overdue", "6");
//                                            }
//
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//
//
//
//                        }
//                    });
//
////
////                }
////                database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("Due Date")
//            }
//        });
//    }
}
