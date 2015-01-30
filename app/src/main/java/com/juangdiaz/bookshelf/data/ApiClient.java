package com.juangdiaz.bookshelf.data;

import com.juangdiaz.bookshelf.model.Book;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by juangdiaz on 1/14/15.
 */
public class ApiClient {

    private static BooksApiInterface sBooksService;


    public static BooksApiInterface getsBooksApiClient() {
       // if (sBooksService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://prolific-interview.herokuapp.com/54b5c027522f9c0007f3b2a6")
                    .build();

         sBooksService = restAdapter.create(BooksApiInterface.class);
        //}
        return sBooksService;
    }

    public interface BooksApiInterface {
        @GET("/books/")
        void listBook(Callback<List<Book>> response);

        @GET("/books/{id}/")
        void detailBook(@Path("id") int id,Callback<Book> response);

    }

}