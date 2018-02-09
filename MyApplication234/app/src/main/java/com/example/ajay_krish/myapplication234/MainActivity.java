package com.example.ajay_krish.myapplication234;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword("aruerp@gmail.com", "password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "Inside the firebaseAuth method", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
