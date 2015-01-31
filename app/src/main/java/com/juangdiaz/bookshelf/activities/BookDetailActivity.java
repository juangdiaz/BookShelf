package com.juangdiaz.bookshelf.activities;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.fragments.BookDetailFragment;
import com.juangdiaz.bookshelf.model.Book;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BookDetailActivity extends ActionBarActivity {


    private Book mBook;
    private ProgressDialog loading;
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

                int bookId = getIntent().getIntExtra(ARG_BOOK_ID ,0); // get book id from intent
                if(bookId > 0) {
                    showLoading();
                    downloadData(bookId);
                }
            } else {
                bookDetailEmpty.setVisibility(View.VISIBLE); // show empty view
            }
        }
        
        
    }

    private void downloadData(int bookID) {
        ApiClient.getsBooksApiClient().detailBook(bookID, new Callback<Book>() {
            @Override
            public void success(Book books, Response response) {
                mBook = books;

                // Create the detail fragment and add it to the activity using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(BookDetailFragment.ARG_BOOK, mBook); // put selected item
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.book_detail_container, fragment)
                        .commit();

                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

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
