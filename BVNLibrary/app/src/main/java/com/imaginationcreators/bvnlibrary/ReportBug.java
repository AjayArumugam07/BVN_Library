package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ReportBug extends DrawerMenu {
    // Declare views
    EditText name;
    EditText email;
    EditText phoneNumber;
    EditText report;
    Button submit;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add drawer menu option
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.report_bug, contentFrameLayout);

        // Find views
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.email);
        report = (EditText) findViewById(R.id.report);
        submit = (Button) findViewById(R.id.submit);

        // Set on click listener for button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to send submitted data
                sendData(name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString(), report.getText().toString());

                // Go to home screen
                startActivity(new Intent(ReportBug.this, HomeScreen.class));
                finish();
            }
        });
    }

    // Send data to Firebase
    private void sendData(String name, String email, String phoneNumber, String report){
        Toast.makeText(ReportBug.this, "Submitted", Toast.LENGTH_SHORT).show();

         database.getReference().child("Bug Report").child(name).child("Email").setValue(email);
         database.getReference().child("Bug Report").child(name).child("Phone Number").setValue(phoneNumber);
         database.getReference().child("Bug Report").child(name).child("report").setValue(report);
    }
}
