package com.imaginationcreators.bvnlibrary;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity {
    private TextView registerEmail;
    private TextView registerPassword;
    private TextView backToLogin;
    private Button createAccount;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText nameOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        // Create views
        registerEmail = (TextView) findViewById(R.id.registerEmail);
        registerPassword = (TextView) findViewById(R.id.registerPassword);
        createAccount = (Button) findViewById(R.id.createAccount);
        backToLogin = (TextView) findViewById(R.id.backToLogin);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar_Register);
        nameOfUser = (EditText) findViewById(R.id.name);

        // Set up user creation
        mAuth = FirebaseAuth.getInstance();

        // Set listeners for buttons
        createAccount.setOnClickListener(listener);
        backToLogin.setOnClickListener(listener);
    }

    // Listener for both buttons
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == createAccount) {
                // If view is create account button, call method to create account
                createNewAccount(registerEmail.getText().toString().trim(), registerPassword.getText().toString().trim());
            } else if (view == backToLogin) {
                // Otherwise, go back to auth screen
                startActivity(new Intent(CreateUser.this, AuthScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }
    };

    // Method to create account, returns true for success and false for fail
    private boolean createNewAccount(String email, String password) {
        // Get user's name from text field
       final String name = nameOfUser.getText().toString();

       // Test for valid names, emails and passwords greater than 6 characters
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password has to be a minimum of 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Display the progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Create the user in Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Set Firebase user to current one if creation is a success
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(CreateUser.this, HomeScreen.class));
                                            }
                                        }
                                    });

                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // Display error message if user already exists
                            Toast.makeText(CreateUser.this, "This account already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreateUser.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        // Remove progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return false;
    }
}
