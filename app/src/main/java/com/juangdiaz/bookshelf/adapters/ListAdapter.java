package com.juangdiaz.bookshelf.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juangdiaz on 1/14/15.
 */
public class ListAdapter extends ArrayAdapter<Book> {


    List<Book> Books;

    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    
    
    
    public ListAdapter(final Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId,objects);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BooksViewHolder holder;
        if(row == null) { //good for optimization view recycling

            row = mLayoutInflater.inflate(R.layout.book_list, parent, false);
            holder = new BooksViewHolder(row);

            row.setTag(holder);
        }
        else {
            holder = (BooksViewHolder) row.getTag();
        }


        Book temp = getItem(position);

        holder.txtBookTitle.setText(temp.getTitle());
        holder.txtBookAuthors.setText(temp.getAuthor());

        return row;
    }
    



    // Holder class used to efficiently recycle view positions 
    private static final class BooksViewHolder {

        public TextView txtBookTitle;
        public TextView txtBookAuthors;

        public BooksViewHolder(View v) {
            txtBookTitle = (TextView) v.findViewById(R.id.txtBookTitle);
            txtBookAuthors = (TextView) v.findViewById(R.id.txtBookAuthors);
        }
    }


}
