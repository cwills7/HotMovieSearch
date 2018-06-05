package com.wills.carl.hotmoviesearch;

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

import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Utils.JsonUtils;
import com.wills.carl.hotmoviesearch.Utils.MovieItemDecoration;
import com.wills.carl.hotmoviesearch.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Spinner orderSpinner;
    public static RecyclerView movieListView;
    public static ArrayList<Movie> movieList;
    public static MovieViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        getPopularMovies();


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
                R.array.order_choice_array, android.R.layout.simple_spinner_item);
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
                    //getTopRatedMovies();
                    getTopRatedMovies();
                   // adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                //Do nothing yet
            }

        });

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
