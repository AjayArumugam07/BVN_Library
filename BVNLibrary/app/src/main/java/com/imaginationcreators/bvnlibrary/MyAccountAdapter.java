package com.imaginationcreators.bvnlibrary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

/**
 * Created by rnagp on 2/27/2018.
 */

// Recycler view adapter for list of books in my account
public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.ReservationsViewHolder>{
    // Set up fields
    private Context mCtx;
    private List<Books> books;
    public FirebaseStorage storage = FirebaseStorage.getInstance();

    // Constructor that sets context and list of books
    public MyAccountAdapter(Context mCtx, List<Books> books) {
        this.mCtx = mCtx;
        this.books = books;
    }

    // Inflate layout based on context and target layout
    @Override
    public ReservationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.my_account_list, null);
        return new ReservationsViewHolder(view);
    }

    // Bind book with recycler view's current position
    @Override
    public void onBindViewHolder(final MyAccountAdapter.ReservationsViewHolder holder, final int position) {
        // Create instance of book based on current position
        final Books book = books.get(position);

        // Set title and author in layout
        holder.title.setText(book.getTitle());
        String author = book.getAuthorLastName() + ", " +  book.getAuthorFirstName();
        holder.author.setText(author);
        holder.reserveCheckout.setText("Return");

        // Set image view to book's cover
        Glide.with(mCtx)
                .using(new FirebaseImageLoader())
                .load(storage.getReferenceFromUrl(book.getUrl()))
                .into(holder.cover);

        final AssignBook assignBook5 = new AssignBook();


        assignBook5.dueDate(book);
        assignBook5.dbSource1.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                holder.dueDate.setText(assignBook5.dueDate);
                assignBook5.dbSource1 = new TaskCompletionSource<>();
            }
        });

        holder.reserveCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBook5.returnBook(book);
                //books.remove(position); // just commented
                //notifyItemRemoved(position); // just commented
                if(books.size() == 1){
                    Toast.makeText(mCtx, "No More Books Checked Out", Toast.LENGTH_SHORT).show();

                    mCtx.startActivity(new Intent(mCtx, HomeScreen.class));
                    holder.reserveCheckout.setOnClickListener(null);


                }
                else{
                    Toast.makeText(mCtx, book.getTitle() + " returned", Toast.LENGTH_SHORT).show();

                    mCtx.startActivity(new Intent(mCtx, MyAccount.class));
                    holder.reserveCheckout.setOnClickListener(null);
                }
            }
        });
    }

    // Return total number of books to be displayed
    @Override
    public int getItemCount() {
        return books.size();
    }

    // Class to hold different views
    class ReservationsViewHolder extends RecyclerView.ViewHolder{
        // Set up views
        ImageView cover;
        TextView title, author, dueDate;
        Button reserveCheckout;

        public ReservationsViewHolder(View itemView) {
            super(itemView);

            // Set each view based on ID in layout
            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            dueDate = itemView.findViewById(R.id.dueDate);
            reserveCheckout = itemView.findViewById(R.id.reserveCheckout);
        }
    }
}
