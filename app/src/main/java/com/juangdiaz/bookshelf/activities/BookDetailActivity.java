package com.juangdiaz.bookshelf.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;
import com.juangdiaz.bookshelf.fragments.BookEditFragment;
import com.juangdiaz.bookshelf.model.Book;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookDetailActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private Book mBook;

    @InjectView(R.id.item_detail_empty)
    TextView bookDetailEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

        if (savedInstanceState == null) {
             mBook = getIntent().getParcelableExtra(BookDetailFragment.ARG_ITEM);
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
        }else if (id == R.id.action_edit) {




                // In single-pane mode, simply start the Edit activity
                // for the selected book ID.
                Intent editIntent = new Intent(this, BookEditActivity.class);
                editIntent.putExtra(BookEditFragment.ARG_ITEM, mBook);
                startActivity(editIntent);

            
            

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        if (item != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }

        setShareIntent();

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent() {

        // create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mBook.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Going to read this cool book" + mBook.getTitle());




        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    
    
}
