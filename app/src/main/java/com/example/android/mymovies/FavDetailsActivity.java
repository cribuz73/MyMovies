package com.example.android.mymovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.NetworkUtils.Movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavDetailsActivity extends AppCompatActivity {

    private static final String TAG = "FavDetailActivity";
    public static int movieID;
    public static List<Movie> favMovies;
    @BindView(R.id.movieNameTv)
    TextView movie_name_tv;
    @BindView(R.id.releaseDateTv)
    TextView release_date_tv;
    @BindView(R.id.voteAverageTv)
    TextView vote_average_tv;
    @BindView(R.id.overviewTv)
    TextView overview_tv;
    @BindView(R.id.posterView)
    ImageView poster_iv;
    @BindView(R.id.backdropView)
    ImageView backdrop_iv;
    @BindView(R.id.imageViewTrailer1)
    ImageView imageTrailer1;
    @BindView(R.id.textView3)
    TextView imageTrailer1_tv;
    @BindView(R.id.imageView2)
    ImageView imageTrailer2;
    @BindView(R.id.textView4)
    TextView imageTrailer2_tv;
    @BindView(R.id.imageView3)
    ImageView imageTrailer3;
    @BindView(R.id.textView5)
    TextView imageTrailer3_tv;
    @BindView(R.id.imageView4)
    ImageView imageTrailer4;
    @BindView(R.id.textView6)
    TextView imageTrailer4_tv;
    @BindView(R.id.textView7)
    TextView reviews_tv;
    @BindView(R.id.textView10)
    TextView reviews_no_tv;
    @BindView(R.id.checkBox)
    CheckBox favorites_cb;
    private String favVoteAverage;
    private Movie favCurrentMovie;
    private String favYearRelease;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        favCurrentMovie = data.getParcelable("currentFavMovie");

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        double vote = favCurrentMovie.getVoteAverage();
        favVoteAverage = String.valueOf(vote);

        String releaseDate = favCurrentMovie.getReleaseDate();
        favYearRelease = releaseDate.substring(0, Math.min(releaseDate.length(), 4));


        String favMoviePath = favCurrentMovie.getPoster();

        try {
            File f = new File(favMoviePath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            poster_iv.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String favMovieBackPath = favCurrentMovie.getBackdrop();

        try {
            File f = new File(favMovieBackPath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            backdrop_iv.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        movie_name_tv.setText(favCurrentMovie.getMovieName());
        release_date_tv.setText(favYearRelease);
        vote_average_tv.setText(favVoteAverage);
        overview_tv.setText(favCurrentMovie.getOverview());

        imageTrailer1.setVisibility(View.INVISIBLE);
        imageTrailer2.setVisibility(View.INVISIBLE);
        imageTrailer3.setVisibility(View.INVISIBLE);
        imageTrailer4.setVisibility(View.INVISIBLE);
        imageTrailer1_tv.setVisibility(View.INVISIBLE);
        imageTrailer2_tv.setVisibility(View.INVISIBLE);
        imageTrailer3_tv.setVisibility(View.INVISIBLE);
        imageTrailer4_tv.setVisibility(View.INVISIBLE);
        reviews_tv.setVisibility(View.INVISIBLE);
        reviews_no_tv.setVisibility(View.INVISIBLE);
        favorites_cb.setVisibility(View.INVISIBLE);


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }

}
