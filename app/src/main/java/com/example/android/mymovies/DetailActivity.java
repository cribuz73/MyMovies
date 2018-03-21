package com.example.android.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.AsyncUtils.TrailersAsynkTask;
import com.example.android.mymovies.NetworkUtils.Movie;
import com.example.android.mymovies.NetworkUtils.NetworkUtils;
import com.example.android.mymovies.NetworkUtils.Review;
import com.example.android.mymovies.Retrofit.API_Interface;
import com.example.android.mymovies.Retrofit.API_Trailer;
import com.example.android.mymovies.Retrofit.Model.Result;
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
    @BindView(R.id.trailer1IV)
    Button trailer1_iv;
    private API_Interface apiInterface;
    private Movie mCurrentMovie;
    private List<String> trailersList;
    private List<Review> reviewsList;

    public static int getID() {
        return movieID;
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
                .load("http://image.tmdb.org/t/p/w342/" + mCurrentMovie.getPoster())
                .into(poster_iv);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500/" + mCurrentMovie.getBackdrop())
                .into(backdrop_iv);

        movie_name_tv.setText(mCurrentMovie.getMovieName());
        release_date_tv.setText(yearRelease);
        vote_average_tv.setText(voteAverage);
        overview_tv.setText(mCurrentMovie.getOverview());

        movieID = mCurrentMovie.getId();

        Button trailer1 = trailer1_iv;
        trailer1.setOnClickListener(this);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }

    private List<String> trailerLoader() {

        TrailersAsynkTask trailerAsync = new TrailersAsynkTask(new TrailersAsynkTask.TrailerAsyncIntf() {

            @Override
            public void onTaskComplete(List trailers) {

                if (trailers != null) {
                    trailersList = trailers;
                }
            }
        });
        trailerAsync.execute();
        return trailersList;
    }


    @Override
    public void onClick(View v) {

        apiInterface = API_Trailer.getClient().create(API_Interface.class);
        String a = apiInterface.getAnswer(movieID, NetworkUtils.API_KEY).request().url().toString();
        apiInterface.getAnswer(movieID, NetworkUtils.API_KEY).enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                Log.d(TAG, call.request().url().toString());
                VideoResponse resource = response.body();
                List<Result> results = resource.getResults();
                //     for (results : results) {
                //                trailersList.add(results.getKey());
                //       }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {

            }
        });


        switch (v.getId()) {
            case R.id.trailer1IV:
                String trailerKey = trailersList.get(0).toString();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
                intent.putExtra("VIDEO_ID", trailerKey);
                startActivity(intent);
                break;
            //           case R.id.button2:
            // handle button3 click
            //               break;
            //           case R.id.button3:
            // handle button3 click
//                break;
            // etc...
        }
    }
}