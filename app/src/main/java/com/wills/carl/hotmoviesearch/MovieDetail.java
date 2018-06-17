package com.wills.carl.hotmoviesearch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wills.carl.hotmoviesearch.Data.MovieContract;
import com.wills.carl.hotmoviesearch.Data.MovieDbHelper;
import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Model.Review;
import com.wills.carl.hotmoviesearch.Model.Video;
import com.wills.carl.hotmoviesearch.Utils.JsonUtils;
import com.wills.carl.hotmoviesearch.Utils.NetworkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title;
    private TextView rating;
    private TextView releaseDate;
    private TextView description;
    private RecyclerView videoRv;
    private static Movie m;
    private static VideoViewAdapter videoViewAdapter;
    private Button favoriteButton;
    static ArrayList<Video> videoList;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);

        MovieDbHelper dbHelper = new MovieDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        videoList = new ArrayList<Video>();
        Intent i = getIntent();
        m = (Movie) i.getSerializableExtra("movie");
        getExtraMovieInfo(Integer.toString(m.getId()));
        thumbnail = findViewById(R.id.detail_thumbnail);
        title = findViewById(R.id.detail_title);
        rating = findViewById(R.id.detail_vote);
        releaseDate = findViewById(R.id.detail_release_date);
        description = findViewById(R.id.detail_description);
        videoRv = findViewById(R.id.video_list);

        thumbnail.setContentDescription(m.getTitle() + " poster image.");

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + m.getPosterPath())
                .resize(R.dimen.movie_poster_width, R.dimen.movie_poster_height)
                .placeholder(R.drawable.loading_animator)
                .onlyScaleDown()
                .into(thumbnail);

        title.setText(m.getTitle());
        rating.setText(Double.toString(m.getVoteAverage()));
        releaseDate.setText(m.getReleaseDate());
        description.setText(m.getOverview());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        videoRv.setLayoutManager(llm);
        videoViewAdapter = new VideoViewAdapter(this, m.getVideoList());
        videoRv.setAdapter(videoViewAdapter);

        favoriteButton = (Button) findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Check to see if it's in the favorites already!
                
                if ("Mark As \nFavorite".equalsIgnoreCase(favoriteButton.getText().toString())) {
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_ID, m.getId());
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, m.getOverview());
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, m.getTitle());
                    cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY, m.getPopularity());
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, m.getPosterPath());
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, m.getReleaseDate());
                    cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, m.getVoteAverage());
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                    favoriteButton.setText("Favorite");
                }
                else if ("Favorite".equalsIgnoreCase(favoriteButton.getText().toString())){
                    getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(m.getId()),
                            null,
                            null);
                    favoriteButton.setText("Mark As \nFavorite");

                }

            }
        });

    }


    private void getExtraMovieInfo(String id){
        URL videoUrl = NetworkUtils.buildVideoUrl(id);
        URL reviewUrl = NetworkUtils.buildReviewUrl(id);
        try {
            new VideoRetrieverAsyncTask().execute(videoUrl);
            new ReviewRetrieverAsyncTask().execute(reviewUrl);

        } catch (NullPointerException e){
            Log.d("ERROR", e.getMessage());
        }
    }


    private static class VideoRetrieverAsyncTask extends AsyncTask<URL, Void, String> {

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
            videoList = JsonUtils.parseVideos(queryResults);
            videoViewAdapter.reset(videoList);
            m.setVideoList(videoList);
        }
    }

    private static class ReviewRetrieverAsyncTask extends AsyncTask<URL, Void, String> {

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
            ArrayList<Review> reviewArrayList = JsonUtils.parseReviews(queryResults);
            m.setReviewList(JsonUtils.parseReviews(queryResults));

        }
    }
}
