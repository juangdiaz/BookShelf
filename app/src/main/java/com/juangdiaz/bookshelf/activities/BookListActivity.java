package com.juangdiaz.bookshelf.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;
import com.juangdiaz.bookshelf.fragments.BookListFragment;
import com.juangdiaz.bookshelf.model.Book;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class BookListActivity extends ActionBarActivity implements BookListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Book mSelectedBook;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.drawable.ic_book);

             if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((BookListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.book_list))
                    .setActivateOnItemClick(true);
        }

    }



    /**
     * Callback method from {@link com.juangdiaz.bookshelf.fragments.BookListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Book selectedItem) {
        showLoading();
        downloadBookDataById(selectedItem.getId());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

         if (id == R.id.action_add) {

            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent createIntent = new Intent(this, BookEditActivity.class);
            startActivity(createIntent);
        } else if (id == R.id.action_delete_all) {
             new AlertDialog.Builder(this)
                     .setTitle("Warning")
                     .setMessage("Are you sure you want to delete all the books?")
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                             ApiClient.getsBooksApiClient().deleteAllBooks();
                             Toast.makeText(getApplicationContext() , "All books have been deleted!", Toast.LENGTH_LONG).show();
                               dialog.dismiss();
                         }
                     })
                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     })
                     .show();
    }

    return super.onOptionsItemSelected(item);
    }

    private void downloadBookDataById(int bookID) {
        ApiClient.getsBooksApiClient().detailBook(bookID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Book>() {
                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        Toast.makeText(BookListActivity.this, "Failed to retrieve book",
                                Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    
                    @Override public void onNext(Book books) {
                        mSelectedBook = books;

                        if (mTwoPane) {
                            // In two-pane mode, show the detail view in this activity by
                            // adding or replacing the detail fragment using a
                            // fragment transaction.
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(BookDetailFragment.ARG_BOOK, mSelectedBook);// put selected item
                            BookDetailFragment fragment = new BookDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.book_detail_container, fragment)
                                    .commit();

                        } else {
                            // In single-pane mode, simply start the detail activity
                            // for the selected item ID.
                            Intent detailIntent = new Intent(getApplicationContext(), BookDetailActivity.class);
                            detailIntent.putExtra(BookDetailFragment.ARG_BOOK, mSelectedBook);
                            startActivity(detailIntent);
                        }
                        loading.dismiss();
                    }
                });
    }

    private void showLoading() {
        loading = new ProgressDialog(this);
        loading.setTitle("Loading");
        loading.setMessage("Wait while loading...");
        loading.show();
    }
    
}
