package com.example.android.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Cristi on 3/9/2018.
 */

public class DetailActivity extends AppCompatActivity {


    private Movie mCurrentMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TextView movie_name_tv = findViewById(R.id.movieNameTv);
        TextView release_date_tv = findViewById(R.id.releaseDateTv);
        TextView vote_average_tv = findViewById(R.id.voteAverageTv);
        TextView overview_tv = findViewById(R.id.overviewTv);
        ImageView movie_iv = findViewById(R.id.imageView);

        Bundle data = getIntent().getExtras();
        mCurrentMovie = data.getParcelable("currentMovie");

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        double vote = mCurrentMovie.getVoteAverage();
        String voteAverage = String.valueOf(vote);

        String releaseDate = mCurrentMovie.getReleaseDate();
        String yearRelease = releaseDate.substring(0, Math.min(releaseDate.length(), 4));


        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342/" + mCurrentMovie.getImage())
                .into(movie_iv);

        movie_name_tv.setText(mCurrentMovie.getMovieName());
        release_date_tv.setText(yearRelease);
        vote_average_tv.setText(voteAverage);
        overview_tv.setText(mCurrentMovie.getOverview());


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }
}
