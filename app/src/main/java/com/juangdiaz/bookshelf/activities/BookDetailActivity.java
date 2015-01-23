package com.juangdiaz.bookshelf.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;
import com.juangdiaz.bookshelf.model.Book;

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
            Book mBook = getIntent().getParcelableExtra(BookDetailFragment.ARG_ITEM);
            if (mBook != null) {
                // Create the detail fragment and add it to the activity using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(BookDetailFragment.ARG_ITEM, mBook); // put selected item
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
