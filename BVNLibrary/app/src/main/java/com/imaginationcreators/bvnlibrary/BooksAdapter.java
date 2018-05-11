package com.imaginationcreators.bvnlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnagp on 2/21/2018.
 */

// Adapter to be used with recylcer view in displaying list of books on search screen
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder>{
    // Create variables
    private Context mCtx;
    private List<Books> books;
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public StorageReference storageRef = storage.getReference();
    StorageReference gsReference = storage.getReferenceFromUrl("gs://bvnlibrary-a0e90.appspot.com/Book_Images/abcmurders.png");


    // Constructor passing in context and array of books to be displayed
    public BooksAdapter(Context mCtx, List<Books> books) {
        this.mCtx = mCtx;
        this.books = books;
    }

    // Inflates layout with book holder
    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.book_list, null);
        return new BooksViewHolder(view);
    }

    // Bind book to position recycler view is on
    @Override
    public void onBindViewHolder(final BooksViewHolder holder, final int position) {
        // Create object of book currently on
        final Books book = books.get(position);

        // Set title and author on text views
        holder.title.setText(book.getTitle());
        String author = book.getAuthorLastName() + ", " +  book.getAuthorFirstName();
        holder.author.setText(author);

        // Add image of book cover
        Glide.with(mCtx)
                .using(new FirebaseImageLoader())
                .load(storage.getReferenceFromUrl(book.getUrl()))
                .into(holder.cover);

        AssignBook assignBook1 = new AssignBook();
        assignBook1.setButtonText(holder, books, book);

        holder.reserveCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                AssignBook assignBook = new AssignBook();
                switch(button.getText().toString()){
//                    case "Reserve":
//                        Log.d("Search", "T1");
//                        assignBook.checkoutReserveBook(book);
//
//                        Log.d("donkey", "checked out");
//                        holder.reserveCheckout.setText("Remove Hold");
//                        break;
                    case "Checkout":
                        Log.d("Search", "T1");
                        assignBook.checkoutReserveBook(book);

                        Log.d("donkey", "checked out");
                        holder.reserveCheckout.setText("Return");
                        break;
                    case "Return":
                        Log.d("donkey", "returned");
                        assignBook.returnBook(book);
                        holder.reserveCheckout.setText("Checkout");
                        break;
//                    case "Remove Hold":
//                        assignBook.removeHold(book);
//                        holder.reserveCheckout.setText("Reserve");
//                        break;
                }
            }
        });
    }

    // Returns number of books total to be displayed
    @Override
    public int getItemCount() {
        return books.size();
    }

    // Class to hold books
    public class BooksViewHolder extends RecyclerView.ViewHolder{
        // Creates fields to be displayed
        ImageView cover;
        TextView title, author;
        Button reserveCheckout;

        // Find each field in layout
        public BooksViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.dueDate);
            reserveCheckout = itemView.findViewById(R.id.reserveCheckout);
        }
    }
}
