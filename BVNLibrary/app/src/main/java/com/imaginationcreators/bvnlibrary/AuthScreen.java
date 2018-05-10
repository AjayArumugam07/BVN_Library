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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthScreen extends AppCompatActivity {
    // Log tag
    private static final String TAG = "AuthScreen";

    // Declare views
    private Button signIn;
    private LoginButton loginButton;
    private EditText username;
    private EditText password;
    private TextView registerUser;
    private ProgressBar progressBar;

    // Create Firebase variables
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_screen);

        // Find views
        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_SignIn);
        loginButton = (LoginButton) findViewById(R.id.login_Button_Facebook);

        // Set up authentication
        mAuth = FirebaseAuth.getInstance();

        // Set on click listeners
        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);

        // Manages callbacks into Facebook SDK
        mCallbackManager = CallbackManager.Factory.create();

        // Setup Facebook login button
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Communication with Facebook for authentication of user
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    // Go to home screen after successful Facebook login
    private void updateUI()
    {
        Toast.makeText(AuthScreen.this, "Login Success", Toast.LENGTH_LONG);
        finish();
        startActivity(new Intent(this, HomeScreen.class));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AuthScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Listener for sign in and create account buttons
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == registerUser) {
                // Go to create user screen
                startActivity(new Intent(AuthScreen.this, CreateUser.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            if (view == signIn) {
                loginSucess(username.getText().toString().trim(), password.getText().toString());
            }
        }
    };

    // returns true if login is success, false for fail
    private void loginSucess(String email, String password) {
        // Checks if user entered email
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return;
        }

        // Checks if user entered password
        if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            this.password.requestFocus();
            return;
        }

        // Checks if user entered valid email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            username.requestFocus();
            return;
        }

        // Checks if password is more than 6 characters
        if (password.length() < 6) {
            Toast.makeText(this, "Password has to be a minimum of 6 characters", Toast.LENGTH_SHORT).show();
            this.password.requestFocus();
            return;
        }

        // Displays progress bar and attempts to authenticate user
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Display success message if login succeeded and go to home screen
                    Toast.makeText(AuthScreen.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AuthScreen.this, HomeScreen.class));
                    finish();
                } else {
                    // Display error message if login failed and return true
                    Toast.makeText(AuthScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
