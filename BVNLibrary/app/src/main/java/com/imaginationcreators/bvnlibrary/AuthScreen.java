package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AuthScreen extends AppCompatActivity {
    private static final String TAG = "AuthScreen";
    private Button signIn;
    private EditText username;
    private EditText password;
    private TextView registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_screen);

        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);

        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == registerUser) {
                startActivity(new Intent(AuthScreen.this, CreateUser.class));
            }
            if (view == signIn) {
                if(loginSucess(username.getText().toString().trim(), password.getText().toString())){
                    Log.d(TAG, "onClick: Login Sucess");
                    Toast.makeText(AuthScreen.this, "Login Success", Toast.LENGTH_SHORT).show();
                    // TODO go to main menu from here
                }
                else{
                    Log.d(TAG, "onClick: Login fail");
                    Toast.makeText(AuthScreen.this, "Login Fail", Toast.LENGTH_SHORT).show();
                    
                }
            }
        }


    };

    private boolean loginSucess(String username, String password){

        return true;
    }
}
