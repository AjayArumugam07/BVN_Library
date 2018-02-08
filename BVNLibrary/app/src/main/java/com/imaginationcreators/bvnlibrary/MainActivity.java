package com.imaginationcreators.bvnlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button signIn;
    private EditText username;
    private EditText password;
    private TextView registerUser;

    private EditText registerEmail;
    private EditText registerPassword;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);

        registerEmail = (EditText) findViewById(R.id.enterEmail);
        registerPassword = (EditText) findViewById(R.id.enterPassword);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == registerUser){
                    registerUser();
                }
                Button b = (Button) view;

            }
        };

        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);
    }

    private void registerUser(){
        setContentView(R.layout.register_user);
    }

}
