package com.wills.carl.hotmoviesearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Utils.JsonUtils;
import com.wills.carl.hotmoviesearch.Utils.MovieItemDecoration;
import com.wills.carl.hotmoviesearch.Utils.NetworkUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Spinner orderSpinner;
    RecyclerView movieListView;
    ArrayList<Movie> movieList;
    MovieViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = getPopularMovies();


        movieListView = (RecyclerView) findViewById(R.id.movie_grid_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.canScrollVertically();
        movieListView.addItemDecoration(new MovieItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_spacing)));
        movieListView.setLayoutManager(layoutManager);
        adapter = new MovieViewAdapter(this, movieList);
        movieListView.setAdapter(adapter);

        orderSpinner = (Spinner) findViewById(R.id.order_spinner);

        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.order_choice_array, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinAdapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object selected = adapterView.getItemAtPosition(i);
                if("Most Popular".equalsIgnoreCase((String) selected)){
                    movieList = getPopularMovies();
                }
                if("Top Rated".equalsIgnoreCase((String) selected)){
                    getTopRatedMovies();
                    movieList = getTopRatedMovies();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                //Do nothing yet
            }

        });

    }



    private ArrayList<Movie> getTopRatedMovies(){
        URL topRatedMoviesUrl = NetworkUtils.buildTopRatedUrl();
        ArrayList<Movie> mList = new ArrayList<>();
        try {
            String results = new MovieRetrieverAsyncTask().execute(topRatedMoviesUrl).get();
            mList = (ArrayList<Movie>) JsonUtils.parseMovies(results);
        } catch (ExecutionException | InterruptedException e){
            Log.d("ERROR", e.getMessage());
        }
        return mList;
    }

    private ArrayList<Movie> getPopularMovies(){
        URL popularMoviesUrl = NetworkUtils.buildPopularUrl();
        ArrayList<Movie> mList = new ArrayList<>();
        try {
            String results = new MovieRetrieverAsyncTask().execute(popularMoviesUrl).get();
            mList = (ArrayList<Movie>) JsonUtils.parseMovies(results);
        } catch (ExecutionException | InterruptedException e){
            Log.d("ERROR", e.getMessage());
        }
        return mList;
    }


    public static class MovieRetrieverAsyncTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... params){
            URL queryUrl = params[0];
            String results = null;
            try{
                results = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch(IOException e){
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String queryResults){
            if (queryResults != null && !queryResults.equals("")){
                List<Movie> movies = JsonUtils.parseMovies(queryResults);
                for(Movie m: movies){
                    Log.i(m.getTitle().toUpperCase() , m.toLog());
                }
                //TODO: populate all of the stuff
            }
        }
    }
}
