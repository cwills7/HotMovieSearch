package com.wills.carl.hotmoviesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wills.carl.hotmoviesearch.Model.Movie;

public class MovieDetail extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title;
    private TextView rating;
    private TextView releaseDate;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);

        Intent i = getIntent();
        Movie m = (Movie) i.getSerializableExtra("movie");

        thumbnail = findViewById(R.id.detail_thumbnail);
        title = findViewById(R.id.detail_title);
        rating = findViewById(R.id.detail_vote);
        releaseDate = findViewById(R.id.detail_release_date);
        description = findViewById(R.id.detail_description);

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

    }
}
