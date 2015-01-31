package com.juangdiaz.bookshelf.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.juangdiaz.bookshelf.R;
import com.juangdiaz.bookshelf.adapters.ListAdapter;
import com.juangdiaz.bookshelf.data.ApiClient;
import com.juangdiaz.bookshelf.model.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BookListFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener {


    private ProgressDialog loading;
    
    @InjectView(R.id.list_view)
    ListView mListView;
    
    @InjectView(R.id.list_progressbar)
    ProgressBar mProgressBar;


    
    ListAdapter mListAdapter;
    private List<Book> streamBookData = new ArrayList<>();

    // The fragment's current callback object, which is notified of list item clicks.

    private Callbacks mCallbacks = sDummyCallbacks; 



    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // fragment instance retained
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListAdapter == null) {
            mListAdapter = new ListAdapter(getActivity(), 0,streamBookData);
        }
        
        showLoading();
        downloadData();

    }


    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
      //TODO:activate later
      //  mListView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }



    private void downloadData() {
         ApiClient.getsBooksApiClient().listBook(new Callback<List<Book>>() {
            @Override
            public void success(List<Book> books, Response response) {
                streamBookData = books;
                updateDisplay();
                //testing loading Dialog
                //SystemClock.sleep(10000);
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void updateDisplay(){

        mListAdapter = new ListAdapter(getActivity(),0,streamBookData);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(this);

    }
    
    
    
    
    
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallbacks.onItemSelected( mListAdapter.getItem(position));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void showLoading() {
        loading = new ProgressDialog(getActivity());
        loading.setTitle("Loading");
        loading.setMessage("Wait while loading...");
        loading.show();
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Book selectedItem);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Book selectedItem) {
        }
    };
    


}
