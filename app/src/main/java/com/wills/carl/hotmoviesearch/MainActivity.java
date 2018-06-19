package com.wills.carl.hotmoviesearch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.stetho.Stetho;
import com.wills.carl.hotmoviesearch.Data.MovieContract;
import com.wills.carl.hotmoviesearch.Data.MovieDbHelper;
import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Utils.JsonUtils;
import com.wills.carl.hotmoviesearch.Utils.MovieItemDecoration;
import com.wills.carl.hotmoviesearch.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner orderSpinner;
    public static RecyclerView movieListView;
    public static ArrayList<Movie> movieList;
    public static MovieViewAdapter adapter;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        movieList = new ArrayList<>();
        getPopularMovies();

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        movieListView = findViewById(R.id.movie_grid_list);
        movieListView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.canScrollVertically();
        movieListView.addItemDecoration(new MovieItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_spacing)));
        movieListView.setLayoutManager(layoutManager);
        adapter = new MovieViewAdapter(this, movieList);
        movieListView.setAdapter(adapter);

        orderSpinner = findViewById(R.id.order_spinner);

        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.order_choice_array, R.layout.spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinAdapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object selected = adapterView.getItemAtPosition(i);
                if("Most Popular".equalsIgnoreCase((String) selected)){
                    getPopularMovies();
                }
                if("Top Rated".equalsIgnoreCase((String) selected)){
                    getTopRatedMovies();
                }
                if("Favorites".equalsIgnoreCase((String) selected)){
                    getFavorites();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                //Do nothing yet
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //May have unfavorited a movie, so when we return, we should re-create our favorites
        String selected = orderSpinner.getSelectedItem().toString();
        if("Favorites".equalsIgnoreCase((String) selected)){
            getFavorites();
        }
    }

    private void getFavorites(){
        Cursor cur = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        ArrayList<Movie> favList = new ArrayList<>();
        cur.moveToFirst();

        while(!cur.isAfterLast()){
            favList.add(
                    new Movie(
                            cur.getInt(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)),
                            cur.getDouble(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVG)),
                            cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                            cur.getDouble(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)),
                            cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)),
                            cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                            cur.getString(cur.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))
                            ));
            cur.moveToNext();
        }
        cur.close();
        adapter.reset(favList);

    }

    private void getTopRatedMovies(){
        URL topRatedMoviesUrl = NetworkUtils.buildTopRatedUrl();
        try {
            new MovieRetrieverAsyncTask().execute(topRatedMoviesUrl);
        } catch (NullPointerException e){
            Log.d("ERROR", e.getMessage());
        }
    }

    private void getPopularMovies(){
        URL popularMoviesUrl = NetworkUtils.buildPopularUrl();
        try {
            new MovieRetrieverAsyncTask().execute(popularMoviesUrl);
        } catch (NullPointerException e){
            Log.d("ERROR", e.getMessage());
        }
    }


    private static class MovieRetrieverAsyncTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... params){
            URL queryUrl = params[0];
            String results = null;
            try{
                results = NetworkUtils.getHttpResponse(queryUrl);
            } catch(IOException e){
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String queryResults){
            movieList = (ArrayList<Movie>) JsonUtils.parseMovies(queryResults);
            adapter.reset(movieList);
            //Do Nothing. We get the data in the methods above
        }
    }
}
