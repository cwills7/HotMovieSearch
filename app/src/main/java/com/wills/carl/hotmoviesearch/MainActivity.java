package com.wills.carl.hotmoviesearch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    Spinner orderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderSpinner = (Spinner) findViewById(R.id.order_spinner);



        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.order_choice_array, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinAdapter);

        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object selected = adapterView.getItemAtPosition(i);
                getTopRatedMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
                //Do nothing
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
