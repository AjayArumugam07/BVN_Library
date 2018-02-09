package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {
    private TextView registerEmail;
    private TextView registerPassword;
    private TextView backToLogin;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        registerEmail = (TextView) findViewById(R.id.registerEmail);
        registerPassword = (TextView) findViewById(R.id.registerPassword);
        createAccount = (Button) findViewById(R.id.createAccount);
        backToLogin = (TextView) findViewById(R.id.backToLogin);

        createAccount.setOnClickListener(listener);
        backToLogin.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == createAccount){
                if(createNewAccount(registerEmail.getText().toString().trim(), registerPassword.getText().toString())){
                    // TODO go to main menu from here
                }
            }
            else if(view == backToLogin){
                startActivity(new Intent(CreateUser.this, AuthScreen.class));
            }
        }
    };

    private boolean createNewAccount(String email, String password){
        return true;
    }
}
