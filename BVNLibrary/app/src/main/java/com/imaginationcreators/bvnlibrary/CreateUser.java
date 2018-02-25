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
    private static final String TAG = "AuthScreen";
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

        registerEmail = (TextView) findViewById(R.id.registerEmail);
        registerPassword = (TextView) findViewById(R.id.registerPassword);
        createAccount = (Button) findViewById(R.id.createAccount);
        backToLogin = (TextView) findViewById(R.id.backToLogin);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar_Register);
        nameOfUser = (EditText) findViewById(R.id.name);

        mAuth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(listener);
        backToLogin.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == createAccount) {
                createNewAccount(registerEmail.getText().toString().trim(), registerPassword.getText().toString().trim());
            } else if (view == backToLogin) {
                startActivity(new Intent(CreateUser.this, AuthScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    };

    private boolean createNewAccount(String email, String password) {
       final String name = nameOfUser.getText().toString();
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


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Account Created");
                                                startActivity(new Intent(CreateUser.this, HomeScreen.class));
                                            }
                                        }
                                    });

                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(CreateUser.this, "Looks like you already have an account", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUser.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
        return false;
    }
}
