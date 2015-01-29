package com.juangdiaz.bookshelf.activities;

import android.app.ProgressDialog;
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
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;
import com.juangdiaz.bookshelf.fragments.BookEditFragment;
import com.juangdiaz.bookshelf.model.Book;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BookDetailActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    private Book mBook;
    private ProgressDialog loading;
    private int bookId;
    public static final String ARG_BOOK_ID = "selected_book_id";

    

    @InjectView(R.id.item_detail_empty)
    TextView bookDetailEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

        if (savedInstanceState == null) {


            if (getIntent().hasExtra(ARG_BOOK_ID)) {

                bookId = getIntent().getIntExtra(ARG_BOOK_ID ,0); // get item from bundle
                if(bookId > 0) {
                    showLoading();
                    downloadData(bookId);
                }

                // Create the detail fragment and add it to the activity using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(BookDetailFragment.ARG_BOOK, mBook); // put selected item
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


    private void downloadData(int bookID) {
        ApiClient.getsBooksApiClient().detailBook(bookID,new Callback<Book>() {
            @Override
            public void success(Book books, Response response) {
                mBook = books;
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
        private void showLoading(){
                loading = new ProgressDialog(this);
               loading.setTitle("Loading");
                loading.setMessage("Wait while loading...");
                loading.show();
            }


    // Call to update the share intent
    private void setShareIntent() {
/*
        // create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mBook.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Going to read this cool book" + mBook.getTitle());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
        */
    }

    
}
