package com.wills.carl.hotmoviesearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Utils.JsonUtils;
import com.wills.carl.hotmoviesearch.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button topRatedBtn, popularBtn;
    TextView displayTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topRatedBtn = (Button) findViewById(R.id.top_rated_button);
        popularBtn = (Button) findViewById(R.id.popular_botton);
        displayTv = (TextView) findViewById(R.id.display_tv);


        topRatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTopRatedMovies();
            }
        });

        popularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopularMovies();
            }
        });

    }



    private void getTopRatedMovies(){
        URL topRatedMoviesUrl = NetworkUtils.buildTopRatedUrl();
        new MovieRetrieverAsyncTask().execute(topRatedMoviesUrl);
    }

    private void getPopularMovies(){
        URL popularMoviesUrl = NetworkUtils.buildPopularUrl();
        new MovieRetrieverAsyncTask().execute(popularMoviesUrl);
    }


    public class MovieRetrieverAsyncTask extends AsyncTask<URL, Void, String>{

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
