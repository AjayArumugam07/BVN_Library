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

public class ReportBug extends DrawerMenu {
    EditText name;
    EditText email;
    EditText phoneNumber;
    Button submit;

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
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(name.getText().toString(), email.getText().toString(), phoneNumber.getText().toString());
                startActivity(new Intent(ReportBug.this, HomeScreen.class));
                finish();
            }
        });
    }

    private void sendData(String name, String email, String phoneNumber){
        Toast.makeText(ReportBug.this, "Submitted", Toast.LENGTH_SHORT).show();

        // Ajay finish method using Strings passed in
    }
}
