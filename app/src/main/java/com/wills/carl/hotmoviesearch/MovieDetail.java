package com.wills.carl.hotmoviesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wills.carl.hotmoviesearch.Model.Movie;

public class MovieDetail extends AppCompatActivity {

    ImageView thumbnail;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);

        Intent i = getIntent();
        Movie m = (Movie) i.getSerializableExtra("movie");

        thumbnail = (ImageView) findViewById(R.id.detail_thumbnail);
        title = (TextView) findViewById(R.id.detail_title);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + m.getPosterPath())
                .resize(R.dimen.movie_poster_width, R.dimen.movie_poster_height)
                .placeholder(R.drawable.loading_animator)
                .onlyScaleDown()
                .into(thumbnail);

        title.setText(m.getTitle());

    }
}
