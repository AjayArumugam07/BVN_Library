package com.imaginationcreators.bvnlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.authentication_screen);

        // Authentication Screen Widgets
        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);

        // Register User Screen Widgets


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == registerUser) {
                    setContentView(R.layout.register_user);
                }
                if (view == signIn) {

                }
                if (view == createAccount) {
                    registerUser();
                }

            }


        };

        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);
        createAccount.setOnClickListener(listener);
    }

    private void registerUser() {
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        createAccount = (Button) findViewById(R.id.createAccount);
        String newEmail = registerEmail.getText().toString().trim();
        String newPassword = registerPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}


