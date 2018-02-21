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
    private static final String TAG = "AuthScreen";
    private Button signIn;
    private LoginButton loginButton;
    private EditText username;
    private EditText password;
    private TextView registerUser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_screen);

        signIn = (Button) findViewById(R.id.signIn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerUser = (TextView) findViewById(R.id.registerUser);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_SignIn);
        loginButton = (LoginButton) findViewById(R.id.login_Button_Facebook);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(listener);
        registerUser.setOnClickListener(listener);

        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            updateUI();
        }
    }

    private void updateUI()
    {
        Toast.makeText(AuthScreen.this, "Logged In", Toast.LENGTH_LONG);
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
                    startActivity(new Intent(AuthScreen.this, HomeScreen.class));

                } else {
                    Toast.makeText(AuthScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        return true;
    }
}
