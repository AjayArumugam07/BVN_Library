package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthScreen extends AppCompatActivity {
    private static final String TAG = "AuthScreen";
    private Button signIn;
    private EditText username;
    private EditText password;
    private TextView registerUser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_screen);

        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_SignIn);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == registerUser) {
                startActivity(new Intent(AuthScreen.this, CreateUser.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            if (view == signIn) {
                if (loginSucess(username.getText().toString().trim(), password.getText().toString())) {
                    Log.d(TAG, "onClick: Login Success");
                    Toast.makeText(AuthScreen.this, "Login Success", Toast.LENGTH_SHORT).show();
                    // TODO go to main menu from here
                } else {
                    Log.d(TAG, "onClick: Login fail");
                    Toast.makeText(AuthScreen.this, "Login Fail", Toast.LENGTH_SHORT).show();

                }
            }
        }


    };

    private boolean loginSucess(String email, String password) {

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            this.password.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password has to be a minimum of 6 characters", Toast.LENGTH_SHORT).show();
            this.password.requestFocus();
            return false;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                   TODO go to home screen code: startActivity(new Intent(AuthScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } else {
                    Toast.makeText(AuthScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        return true;
    }
}
