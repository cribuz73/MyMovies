package com.example.android.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.example.android.mymovies.NetworkUtils.NetworkUtils;
import com.example.android.mymovies.Retrofit.API_Interface;
import com.example.android.mymovies.Retrofit.API_Review_Interface;
import com.example.android.mymovies.Retrofit.API_Trailer;
import com.example.android.mymovies.Retrofit.Model.Result;
import com.example.android.mymovies.Retrofit.Model.Review;
import com.example.android.mymovies.Retrofit.Model.ReviewResponse;
import com.example.android.mymovies.Retrofit.Model.VideoResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cristi on 3/9/2018.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailActivity";
    public static int movieID;
    public static List<Review> reviewsList;
    public ReviewAdapter revAdapter;
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
    private API_Interface apiInterface;
    private API_Review_Interface apiRevInterface;
    private Movie mCurrentMovie;
    private List<Result> videoList;
    private int videoPosition = 0;
    private String trailerKey;
    private RecyclerView revRecyclerView;
    private int reviews_no;

    public static int getID() {
        return movieID;
    }

    public static List<Review> getReviewsList() {
        return reviewsList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        ButterKnife.bind(this);

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
                .load("http://image.tmdb.org/t/p/w185/" + mCurrentMovie.getPoster())
                .into(poster_iv);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + mCurrentMovie.getBackdrop())
                .into(backdrop_iv);

        movie_name_tv.setText(mCurrentMovie.getMovieName());
        release_date_tv.setText(yearRelease);
        vote_average_tv.setText(voteAverage);
        overview_tv.setText(mCurrentMovie.getOverview());

        movieID = mCurrentMovie.getId();

        revRecyclerView = findViewById(R.id.image_reviews_rv);
        revRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        imageTrailer1.setOnClickListener(this);
        imageTrailer2.setOnClickListener(this);
        imageTrailer3.setOnClickListener(this);
        imageTrailer4.setOnClickListener(this);
        reviews_tv.setOnClickListener(this);


        getTrailerData();
        getReviewData();


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {

        trailerKey = videoList.get(videoPosition).getKey();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
        intent.putExtra("VIDEO_ID", trailerKey);
        startActivity(intent);

        switch (v.getId()) {
            case R.id.imageViewTrailer1:
                videoPosition = 0;
                break;
            case R.id.imageView2:
                videoPosition = 1;
                break;
            case R.id.imageView3:
                videoPosition = 2;
                break;
            case R.id.imageView4:
                videoPosition = 3;
                break;

        }

    }

    public void getTrailerData() {

        apiInterface = API_Trailer.getClient().create(API_Interface.class);
        apiInterface.getAnswer(movieID, NetworkUtils.API_KEY).enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                VideoResponse resource = response.body();
                videoList = resource.getResults();

                int i = videoList.size();

                switch (i) {
                    case 0:
                        imageTrailer1.setVisibility(View.INVISIBLE);
                        imageTrailer2.setVisibility(View.INVISIBLE);
                        imageTrailer3.setVisibility(View.INVISIBLE);
                        imageTrailer4.setVisibility(View.INVISIBLE);
                        imageTrailer1_tv.setVisibility(View.INVISIBLE);
                        imageTrailer2_tv.setVisibility(View.INVISIBLE);
                        imageTrailer3_tv.setVisibility(View.INVISIBLE);
                        imageTrailer4_tv.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        imageTrailer1.setVisibility(View.VISIBLE);
                        imageTrailer2.setVisibility(View.INVISIBLE);
                        imageTrailer3.setVisibility(View.INVISIBLE);
                        imageTrailer4.setVisibility(View.INVISIBLE);
                        imageTrailer1_tv.setVisibility(View.VISIBLE);
                        imageTrailer2_tv.setVisibility(View.INVISIBLE);
                        imageTrailer3_tv.setVisibility(View.INVISIBLE);
                        imageTrailer4_tv.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        imageTrailer1.setVisibility(View.VISIBLE);
                        imageTrailer2.setVisibility(View.VISIBLE);
                        imageTrailer3.setVisibility(View.INVISIBLE);
                        imageTrailer4.setVisibility(View.INVISIBLE);
                        imageTrailer1_tv.setVisibility(View.VISIBLE);
                        imageTrailer2_tv.setVisibility(View.VISIBLE);
                        imageTrailer3_tv.setVisibility(View.INVISIBLE);
                        imageTrailer4_tv.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        imageTrailer1.setVisibility(View.VISIBLE);
                        imageTrailer2.setVisibility(View.VISIBLE);
                        imageTrailer3.setVisibility(View.VISIBLE);
                        imageTrailer4.setVisibility(View.INVISIBLE);
                        imageTrailer1_tv.setVisibility(View.VISIBLE);
                        imageTrailer2_tv.setVisibility(View.VISIBLE);
                        imageTrailer3_tv.setVisibility(View.VISIBLE);
                        imageTrailer4_tv.setVisibility(View.INVISIBLE);
                        break;

                }

            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
            }
        });
    }

    public void getReviewData() {

        apiRevInterface = API_Trailer.getClient().create(API_Review_Interface.class);
        String a = apiRevInterface.getAnswer(movieID, NetworkUtils.API_KEY).request().url().toString();
        apiRevInterface.getAnswer(movieID, NetworkUtils.API_KEY).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                ReviewResponse obtainReviews = response.body();
                reviewsList = obtainReviews.getReviews();

                reviews_no = reviewsList.size();
                String reviewsNumber = String.valueOf(reviews_no);
                reviews_no_tv.setText(reviewsNumber);

                revRecyclerView.setAdapter(new ReviewAdapter(DetailActivity.this, obtainReviews.getReviews()));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
            }
        });
    }

}