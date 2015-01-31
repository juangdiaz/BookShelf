package com.juangdiaz.bookshelf.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.activities.BookListActivity;
import com.juangdiaz.bookshelf.model.Book;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookEditFragment extends Fragment {

    public static final String ARG_ITEM = "selected_book_id";

    private static final String SAVED_LAST_TITLE = "last_title";

    private Book mBook; // the selected item

    @InjectView(R.id.book_edit_title)
    EditText bookEditTitle;

    @InjectView(R.id.book_edit_author)
    EditText bookEditAuthor;

    @InjectView(R.id.book_edit_publisher)
    EditText bookEditPublisher;

    @InjectView(R.id.book_edit_categories)
    EditText bookEditCategories;

    @InjectView(R.id.book_edit_submit)
    Button submitButton;


    public BookEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) { //edit
            if (getArguments().containsKey(ARG_ITEM)) {
                mBook = getArguments().getParcelable(ARG_ITEM); // get item from bundle
            }
        } else {


        }//Add new
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_edit, container, false);
        ButterKnife.inject(this, rootView);

        // Show the content.
        if (mBook != null) {
            if (!Strings.isNullOrEmpty(mBook.getTitle())) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml(mBook.getTitle()).toString()); // title in the action bar
                bookEditTitle.setHint("");
                bookEditTitle.setText(Html.fromHtml(mBook.getTitle()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getAuthor())) {
                bookEditAuthor.setHint("");
                bookEditAuthor.setText(Html.fromHtml(mBook.getAuthor()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getPublisher())) {
                bookEditPublisher.setHint("");
                bookEditPublisher.setText(Html.fromHtml(mBook.getPublisher()).toString());
            }
            if (!Strings.isNullOrEmpty(mBook.getCategories())) {
                bookEditCategories.setHint("");
                bookEditCategories.setText(Html.fromHtml(mBook.getCategories()).toString());
            }
        } else {

        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBook();
            }
        });


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_done) {
            exitDialog();

            return true;
        } else if (id == android.R.id.home) {
            exitDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onBackPressed() {
        exitDialog();
    }


    public void exitDialog() {

        // Ask for their name
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Are you sure?");
        alert.setMessage("Do you want to continue without saving?");


        // Make an "OK" to exit
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), BookListActivity.class));
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

    private void submitBook() {

        if (isEmpty(bookEditTitle) ||
            isEmpty(bookEditAuthor) ||
            isEmpty(bookEditPublisher) ||
            isEmpty(bookEditCategories)) {
            
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("All Fields are required");

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    return;
                }
            });
            alert.show();
        }
        
        //Save info to API

    }

    //Check for empty EditText Fields
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


}
