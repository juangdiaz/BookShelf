package com.juangdiaz.bookshelf.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookDetailActivity extends ActionBarActivity {


    @InjectView(R.id.item_detail_empty)
    TextView bookDetailEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

        if (savedInstanceState == null) {

            if (getIntent().hasExtra(BookDetailFragment.ARG_BOOK)) {
                // Create the detail fragment and add it to the activity using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putInt(BookDetailFragment.ARG_BOOK, 0); // put selected item
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.book_detail_container, fragment)
                        .commit();
            } else {
                bookDetailEmpty.setVisibility(View.VISIBLE); // show empty view
            }
        }
        
        
    }
}
