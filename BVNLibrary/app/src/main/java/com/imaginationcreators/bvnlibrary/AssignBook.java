package com.imaginationcreators.bvnlibrary;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by Ajay_Krish on 2/25/2018.
 */

public class AssignBook {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void checkoutReserveBook(Books book)
    {
        if(book.getAvailablity().equalsIgnoreCase("Available"))
        {
           database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Checked Out").setValue(book.getTitle());
           database.getReference().child("Books").child(book.getTitle()).child("Availablility").setValue("Unavailable");
        }

        if(book.getAvailablity().equalsIgnoreCase(("Unavailable")))
        {


        }
        else
        {
            Log.d("Search", "Text in Database doesn't match");
        }

    }
}
