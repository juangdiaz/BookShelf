package com.juangdiaz.bookshelf.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;


import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.activities.BookEditActivity;
import com.juangdiaz.bookshelf.activities.BookListActivity;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.model.Book;


import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookDetailFragment extends Fragment {


    public static final String ARG_BOOK = "selected_book";

    private static final String SAVED_LAST_TITLE = "last_title";

    private Book mBook; // the selected item

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";


    // Access the device's key-value storage
    SharedPreferences mSharedPreferences;


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

    @InjectView(R.id.book_detail_checkout_button)
    Button checkoutButton;


    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_BOOK)) {
            mBook = getArguments().getParcelable(ARG_BOOK); // get item from bundle
        }
        setShareIntent();

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
                //Format Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
                String formattedDateStr = dateFormat.format(mBook.getLastCheckedOut()); 
                bookDetailLastCheckout.setText(Html.fromHtml("Last Checkout: @ " + formattedDateStr).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getLastCheckedOutBy())) {
                bookDetailLastCheckoutBy.setText(Html.fromHtml(mBook.getLastCheckedOutBy()).toString());
            }
        }

        //Get shared Preferences
        mSharedPreferences = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);


        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckout();
            }
        });

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), BookListActivity.class));
            return true;
        } else if (id == R.id.action_edit) {
            // In single-pane mode, simply start the Edit activity
            // for the selected book ID.
            Intent editIntent = new Intent(getActivity(), BookEditActivity.class);
            editIntent.putExtra(BookEditFragment.ARG_ITEM, mBook);
            startActivity(editIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_book_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuShare = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(menuShare);

        shareAction.setShareIntent(setShareIntent());
    }


    // Call to update the share intent
    private Intent setShareIntent() {

        // create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mBook.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Going to read this cool book: " + mBook.getTitle());

        return shareIntent;


    }


    public void updateCheckout() {
        //TODO: dialog asking for name
        getCheckoutNameDialog();

        // Read the user's name,
        // or an empty string if nothing found

    }


    public void getCheckoutNameDialog() {


        // Ask for their name
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Book Checkout!");
        alert.setMessage("What is your name?");


        String checkoutBy = mSharedPreferences.getString(PREF_NAME, "");

        // Create EditText for entry
        final EditText input = new EditText(getActivity());

        if (checkoutBy != "") {
            input.setText(checkoutBy);
        }
        alert.setView(input);

        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                // Grab the EditText's input
                String inputName = input.getText().toString();

                // Add to the SharedPref
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.putString(PREF_NAME, inputName);
                e.commit();

                //Call API

                ApiClient.getsBooksApiClient().checkoutBook(mBook.getId(), inputName, new Callback<Book>() {
                    @Override
                    public void success(Book book, Response response) {

                        Toast.makeText(getActivity(), "You have checked out the book: " + mBook.getTitle()
                                , Toast.LENGTH_LONG).show();
                        NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), BookListActivity.class));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "There was an Issue Checking out this book, please try again later"
                                , Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

        // Make a "Cancel" button 
        // that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


}
