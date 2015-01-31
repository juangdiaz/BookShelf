package com.juangdiaz.bookshelf.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.fragments.BookEditFragment;
import com.juangdiaz.bookshelf.model.Book;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BookEditActivity extends ActionBarActivity {

    private Book mBook;
    BookEditFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

        if (savedInstanceState == null) {
            mBook = getIntent().getParcelableExtra(BookEditFragment.ARG_ITEM);
            if (mBook != null) {
                // Edit a book.
                Bundle arguments = new Bundle();
                arguments.putParcelable(BookEditFragment.ARG_ITEM, mBook); // put selected item
                mFragment = new BookEditFragment();
                mFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.book_edit_container, mFragment)
                        .commit();
            } else { // create a book

                mFragment = new BookEditFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.book_edit_container, mFragment)
                        .commit();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_edit, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        mFragment.onBackPressed();
    }

}
