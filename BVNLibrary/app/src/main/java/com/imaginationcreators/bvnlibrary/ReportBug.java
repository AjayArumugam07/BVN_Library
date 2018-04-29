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

public class ReportBug extends DrawerMenu {
    EditText emailFrom;
    EditText report;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add drawer menu option
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.report_bug, contentFrameLayout);

        // Find views
        emailFrom = (EditText) findViewById(R.id.email);
        report = (EditText) findViewById(R.id.report);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("bvnfblalibrary@gmail.com"));
        email.setType("text");

        email.putExtra(Intent.EXTRA_EMAIL, emailFrom.getText().toString());
        email.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
        email.putExtra(Intent.EXTRA_TEXT, report.getText().toString());

        try{
            startActivity(Intent.createChooser(email, "Send mail..."));
        } catch(Exception e){
            Log.d("", "sendEmail: Email send failed");
        }
    }
}
