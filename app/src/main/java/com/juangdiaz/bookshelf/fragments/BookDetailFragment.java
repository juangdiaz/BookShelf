package com.juangdiaz.bookshelf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.model.Book;


import com.google.common.base.Strings;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookDetailFragment extends Fragment {


    public static final String ARG_ITEM = "selected_book_id";

    private static final String SAVED_LAST_TITLE = "last_title";

    private Book mBook; // the selected item
    private int bookId;



    @InjectView(R.id.book_detail_title)
    TextView bookDetailTitle;
    
    @InjectView(R.id.book_detail_author)
    TextView bookDetailAuthor;
    
    @InjectView(R.id.book_detail_publisher)
    TextView bookDetailPublisher;
    
    @InjectView(R.id.book_detail_categories)
    TextView bookDetailCategories;
    
    @InjectView(R.id.book_detail_lastcheckout)
    TextView bookDetailLastCheckout;
    
    @InjectView(R.id.book_detail_lastcheckoutby)
    TextView bookDetailLastCheckoutBy;


    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments().containsKey(ARG_ITEM)) {
            bookId = getArguments().getInt(ARG_ITEM); // get item from bundle
            if(bookId > 0) {
                downloadData(bookId);
            }
        }
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        ButterKnife.inject(this, rootView);

        // Show the content.
        if (mBook != null) {
            if (!Strings.isNullOrEmpty(mBook.getTitle())) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml(mBook.getTitle()).toString()); // title in the action bar
                bookDetailTitle.setText(Html.fromHtml(mBook.getTitle()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getAuthor())) {
                bookDetailAuthor.setText(Html.fromHtml(mBook.getAuthor()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getPublisher())) {
                bookDetailPublisher.setText(Html.fromHtml("Publisher: " + mBook.getPublisher()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getCategories())) {
                bookDetailCategories.setText(Html.fromHtml("Tags: " + mBook.getCategories()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getLastCheckedOut())) {
                bookDetailLastCheckout.setText(Html.fromHtml("Last Checkout: " + mBook.getLastCheckedOut()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getLastCheckedOutBy())) {
                bookDetailLastCheckoutBy.setText(Html.fromHtml(mBook.getLastCheckedOutBy()).toString());
            }
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_LAST_TITLE)) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(savedInstanceState.getString(SAVED_LAST_TITLE));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CharSequence actionBarTitle = ((ActionBarActivity) getActivity()).getSupportActionBar().getTitle();
        if (actionBarTitle != null && actionBarTitle.length() > 0) {
            outState.putString(SAVED_LAST_TITLE, actionBarTitle.toString());
        }
    }


    private void downloadData(int bookID) {
        ApiClient.getsBooksApiClient().detailBook(bookID,new Callback<Book>() {
            @Override
            public void success(Book books, Response response) {
                mBook = books;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
