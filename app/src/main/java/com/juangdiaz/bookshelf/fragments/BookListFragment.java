package com.juangdiaz.bookshelf.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.support.v4.app.Fragment;
import android.widget.ListView;

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

    ListView mListView;
    @InjectView(R.id.list_view)


    ListView bookList;
    ListAdapter mListAdapter;
    private List<Book> streamBookData = new ArrayList<>();

    // The fragment's current callback object, which is notified of list item clicks.

    private Callbacks mCallbacks = sDummyCallbacks; 

    private OnFragmentInteractionListener mListener;


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
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void updateDisplay(){

        mListAdapter = new ListAdapter(getActivity(),0,streamBookData);
        bookList.setAdapter(mListAdapter);

        bookList.setOnItemClickListener(this);
        //mListView.setOnScrollListener(this);

    }
    
    
    
    
    
    
    
    
    
    

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //TODO:add code
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int po = position;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
    
    
    
    
    
    
    
    

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
