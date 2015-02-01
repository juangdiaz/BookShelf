package com.juangdiaz.bookshelf.data;

import com.juangdiaz.bookshelf.model.Book;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import rx.Observable;

/**
 * Created by juangdiaz on 1/14/15.
 */
public class ApiClient {

    private static BooksApiInterface sBooksService;


    public static BooksApiInterface getsBooksApiClient() {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint("http://prolific-interview.herokuapp.com/54b5c027522f9c0007f3b2a6")
                    .build();

         sBooksService = restAdapter.create(BooksApiInterface.class);
        return sBooksService;
    }

    public interface BooksApiInterface {
        @GET("/books/")
        Observable<List<Book>> listBook();

        @GET("/books/{id}/")
        Observable<Book>  detailBook(
                @Path("id") int id);

        @FormUrlEncoded 
        @POST("/books/") 
        Observable<Book> createBook(
                @Field("title") String title,
                @Field("author") String author,
                @Field("publisher") String publisher,
                @Field("categories") String categories,
                @Field("lastCheckedOutBy") String lastCheckedOutBy);

        @FormUrlEncoded
        @POST("/books/{id}")
        Observable<Book> updateBook(
                @Path("id") int id,
                @Field("title") String title,
                @Field("author") String author,
                @Field("publisher") String publisher,
                @Field("categories") String categories);

        
        @FormUrlEncoded
        @PUT("/books/{id}") 
        Observable<Book> checkoutBook(
                @Path("id") int id,
                @Field("lastCheckedOutBy") String lastCheckedOutBy);


        @DELETE("/books/{id}/") 
        Observable<String> deleteBookById(@Path("id") int id);


        @DELETE("/clean") 
        void deleteAllBooks();
    }

}