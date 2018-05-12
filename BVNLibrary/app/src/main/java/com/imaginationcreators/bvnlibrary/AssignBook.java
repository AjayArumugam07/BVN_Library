package com.imaginationcreators.bvnlibrary;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static android.content.ContentValues.TAG;

/**
 * Created by Ajay_Krish on 2/25/2018.
 */

public class AssignBook {
    // Create references used for Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public String dueDate;
    public String dueDate1;

    // Declare tasks
    public TaskCompletionSource<ArrayList<Books>> dbSource = new TaskCompletionSource<>();
    public TaskCompletionSource<String> dbSource1 = new TaskCompletionSource<>();
    public TaskCompletionSource<ArrayList<Books>> dbSource2 = new TaskCompletionSource<>();
    public TaskCompletionSource<ArrayList<Books>> dbSource3 = new TaskCompletionSource<>();

    // Declare overdue book List
    final ArrayList<Books> overdueBooks = new ArrayList<Books>();

    // Declare book lists
    public final ArrayList<Books> userBooks = new ArrayList<Books>();
    public final ArrayList<Books> reservedBooks = new ArrayList<Books>();

    private ChildEventListener childEventListener;

    // Checkout or reserve book
    public void checkoutBook(final Books book, String uid) {
        Log.d("fish", "checkoutBook: ");
        if(uid.equalsIgnoreCase("")){
            Log.d("fish", "current id used ");
            uid = mAuth.getUid();
        }

//        // Check if book is available
//        if (book.getAvailablity().equalsIgnoreCase("Available")) {
            //Log.d("fish", "book is available");

            // Set database values to match checking out book
            database.getReference().child("Users").child(uid).child("Checked Out").child("checkout").child(book.getTitle()).setValue("1");
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Unavailable");
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("User Id").setValue(uid);

            // Set up date to track due date
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

            // Number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            c.add(Calendar.DAY_OF_MONTH, 7);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");

            // Set due date of book
            String output = sdf1.format(c.getTime());

            // Set book to checked out on database
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("Check Out Date").setValue(dateFormat.format(date));
            database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").child("Due Date").setValue(output);
//        }
    }

    // Set text of button in search screen
    public void setButtonText(final BooksAdapter.BooksViewHolder holder, final List<Books> searchSample, final Books book) {
        // If book is available, give user option to check out book
        if (book.getAvailablity().contains("Available")) {
            holder.reserveCheckout.setText("Checkout");
            return;
        }
        getUserReservedBooks();
        dbSource3.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                for (int i = 0; i < reservedBooks.size(); i++) {
                    if (reservedBooks.get(i).getTitle().equalsIgnoreCase(book.getTitle())) {
                        holder.reserveCheckout.setText("Remove Hold");
                        return;
                    }
                }

                getUserCheckedoutBooks(searchSample, true);
                dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        for (int i = 0; i < userBooks.size(); i++) {
                            if (userBooks.get(i).getTitle().equalsIgnoreCase(book.getTitle())) {
                                holder.reserveCheckout.setText("Return");
                                return;
                            }
                        }

                        for (int i = 0; i < userBooks.size(); i++) {
                            Log.d("user books: ", userBooks.get(i).getTitle());
                        }

                        holder.reserveCheckout.setText("Reserve");
                    }
                });
            }
        });
    }

    // Get all the books the user has checked out
    public List<Books> getUserCheckedoutBooks(final List<Books> searchSample, final boolean addRandomChild) {
        if (addRandomChild) {
            database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").child("checkout").child("1").setValue("1");
        }

        // Start pulling all the books the user has checked out
        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (int i = 0; i < searchSample.size(); i++) {
                        // For each book pulled, check if it is in the sample
                        if (dataSnapshot1.getKey().toString().contains(searchSample.get(i).getTitle())) {
                            userBooks.add(searchSample.get(i));
                        }
                    }
                }
                if (addRandomChild) {
                    database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").child("checkout").child("1").removeValue();
                }

                // Set result of db source and reset db source
                dbSource.setResult(userBooks);
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
        return userBooks;
    }

    // Check if a book is overdue
    public void dueDate(final Books book) {
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().contains("Due Date")) {
                    dueDate = "Due Date: " + dataSnapshot.getValue().toString();
                    dueDate1 = dataSnapshot.getValue().toString();
                    dbSource1.setResult(dueDate);
                    dbSource1 = new TaskCompletionSource<>();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                    Date date1 = new Date();
                    Date date3 = new Date();

                    Date date2 = new Date();
                    String k = sdf.format(date2);

                    try {

                        date1 = sdf.parse(dueDate1);

                        date3 = sdf.parse(k);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date3.after(date1)) {
                        overdueBooks.add(book);
                    }
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

    // Return a book back to database
    public void returnBook(final Books book, final BooksAdapter.BooksViewHolder holder) {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("fish", "child added");
                ArrayList<String> uids = new ArrayList<String>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    uids.add(dataSnapshot1.getKey());
                }
                database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").child("hold").child(uids.get(uids.size() - 1)).removeValue();
                database.getReference().child("Users").child(uids.get(uids.size() - 1)).child("Holds").child("hold").child(book.getTitle()).removeValue();
                checkoutBook(book, uids.get(uids.size() - 1));
                database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").removeEventListener(childEventListener);
                if(holder != null) {
                    holder.reserveCheckout.setText("Reserve");
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
        };

        Log.d("fish", "returnBook");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Availablility").setValue("Available");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("User Information").removeValue();
        database.getReference().child("Users").child(mAuth.getUid()).child("Checked Out").child("checkout").child(book.getTitle()).removeValue();
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").addChildEventListener(childEventListener);
    }

    // Get overdue books from database
    public void getOverdueBooks() {
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();

        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                // Get books checked out by user
                getUserCheckedoutBooks(search.searchSample, false);
                dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        // Check if a book is overdue
                        for (final Books book : userBooks) {
                            dueDate(book);
                        }
                        if (overdueBooks.size() == 0) {
                            // Add book to overdue list
                            overdueBooks.add(new Books());
                        }

                        // Set result of db source and reset it
                        dbSource2.setResult(overdueBooks);
                        dbSource2 = new TaskCompletionSource<>();
                        dbSource = new TaskCompletionSource<>();
                    }
                });
            }
        });
    }

    private void getUserReservedBooks() {
        final DatabaseReference reserveBookReference = database.getReference().child("Users").child(mAuth.getUid()).child("Holds");
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();
        Log.d("horse", "getUserReservedBooks: ");

        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                database.getReference().child("Users").child(mAuth.getUid()).child("Holds").child("hold").child("1").setValue("1");
                reserveBookReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (Books book : search.searchSample) {
                                if (book.getTitle().equalsIgnoreCase(dataSnapshot1.getKey())) {
                                    reservedBooks.add(book);
                                }
                            }
                        }
                        Log.d(TAG, "onChildAdded: ");
                        database.getReference().child("Users").child(mAuth.getUid()).child("Holds").child("hold").child("1").removeValue();
                        dbSource3.setResult(reservedBooks);
                        dbSource3 = new TaskCompletionSource<>();
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
        });
    }

    public void reserveBook(Books book) {
        Log.d("fish", "reserveBook: ");
        database.getReference().child("Users").child(mAuth.getUid()).child("Holds").child("hold").child(book.getTitle()).setValue("1");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").child("hold").child(mAuth.getUid()).setValue("1");
    }

    // Remove hold from book
    public void removeHold(Books book) {
        Log.d("fish", "removeHold: ");
        database.getReference().child("Books").child("Book").child(book.getTitle()).child("Holds").child("hold").child(mAuth.getUid()).removeValue();
        database.getReference().child("Users").child(mAuth.getUid()).child("Holds").child("hold").child(book.getTitle()).removeValue();
    }

    public void resetListener(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("fish", "child added in blank listener");
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
        };
    }
}
