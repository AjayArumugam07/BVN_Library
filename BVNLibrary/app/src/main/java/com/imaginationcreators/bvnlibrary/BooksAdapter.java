package com.imaginationcreators.bvnlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rnagp on 2/21/2018.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder>{
    private Context mCtx;
    private List<Books> books;

    public BooksAdapter(Context mCtx, List<Books> books) {
        this.mCtx = mCtx;
        this.books = books;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.book_list, null);
        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, int position) {
        Books book = books.get(position);

        holder.title.setText(book.getTitle());

        String author = book.getAuthorLastName() + ", " +  book.getAuthorFirstName();
        holder.author.setText(author);


        // TODO set image by holder.cover. (get image from firebase
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BooksViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title, author;

        public BooksViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.bookCover);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
        }
    }
}
