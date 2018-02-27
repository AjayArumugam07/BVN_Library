package com.imaginationcreators.bvnlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class Reservations extends DrawerMenu {
    RecyclerView recyclerView;
    ReservationsAdapter adapter;

    private ArrayList<Books> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.frameContent);
        getLayoutInflater().inflate(R.layout.reservations_screen, contentFrameLayout);

        books = new ArrayList<>();
        books.add(new Books("Hi", "Rajin", "Nagpal", "93040943", "Available", "www.abc.com"));
        books.add(new Books("asdf", "asdf", "adf", "93040943", "Available", "www.abc.com"));
        books.add(new Books("ad", "asdf", "adf", "93040943", "Available", "www.abc.com"));
        books.add(new Books("adf", "adf", "adsf", "93040943", "Available", "www.abc.com"));
        books.add(new Books("asdf", "adf", "adf", "93040943", "Available", "www.abc.com"));
        // TODO Ajay set ArrayList books to array of books on hold here

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Reservations.this));

        adapter = new ReservationsAdapter(Reservations.this, books);
        recyclerView.setAdapter(adapter);
    }
}
