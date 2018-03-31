package com.imaginationcreators.bvnlibrary;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MyAccount extends DrawerMenu {
    RecyclerView recyclerView;
    MyAccountAdapter adapter;

    private ArrayList<Books> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.my_account, contentFrameLayout);

        books = new ArrayList<>();
        final AssignBook assignBook = new AssignBook();
        final Search search = new Search();
        search.setLocalDatabaseForSearchTitle();
        Log.d("12345", "check");
        search.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                Log.d("123456", "check1");
                assignBook.getUserCheckedoutBooks(search.searchSample);
                assignBook.dbSource.getTask().addOnCompleteListener(new OnCompleteListener<ArrayList<Books>>() {
                    @Override
                    public void onComplete(@NonNull Task<ArrayList<Books>> task) {
                        Log.d("12345678", "check 2");
                        Log.d("123457", assignBook.reservedBooks.get(0).getTitle());

                        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyAccount.this));

                        adapter = new MyAccountAdapter(MyAccount.this, assignBook.reservedBooks);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}




