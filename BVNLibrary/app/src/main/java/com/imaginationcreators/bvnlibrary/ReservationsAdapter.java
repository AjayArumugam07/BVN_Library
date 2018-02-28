package com.imaginationcreators.bvnlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by rnagp on 2/27/2018.
 */

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder>{
    private Context mCtx;
    private List<Books> books;
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public StorageReference storageRef = storage.getReference();
    StorageReference gsReference = storage.getReferenceFromUrl("gs://bvnlibrary-a0e90.appspot.com/Book_Images/abcmurders.png");

    public ReservationsAdapter(Context mCtx, List<Books> books) {
        this.mCtx = mCtx;
        this.books = books;
    }

    @Override
    public ReservationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.reservation_list, null);
        return new ReservationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReservationsAdapter.ReservationsViewHolder holder, int position) {
        final Books book = books.get(position);

        holder.title.setText(book.getTitle());


        String author = book.getAuthorLastName() + ", " +  book.getAuthorFirstName();
        holder.author.setText(author);

        // TODO add pickup.setText() for when book should be picked up

        Glide.with(mCtx)
                .using(new FirebaseImageLoader())
                .load(storage.getReferenceFromUrl(book.getUrl()))
                .into(holder.cover);

        final AssignBook assignBook = new AssignBook();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ReservationsViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title, author, pickup;

        public ReservationsViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            pickup = itemView.findViewById(R.id.pickup);
        }
    }
}
