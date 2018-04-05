package com.example.android.mymovies;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymovies.Data.DataContract.MoviesEntry;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cristi on 3/9/2018.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "DetailActivity";
    private static final int FAV_MOVIE_LOADER_ID = 0;
    public static int movieID;
    public static List<Review> reviewsList;
    public ReviewAdapter revAdapter;
    @BindView(R.id.checkBox)
    CheckBox favorites_cb;
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
    private String voteAverage;
    private String yearRelease;
    private API_Interface apiInterface;
    private API_Review_Interface apiRevInterface;
    private Movie mCurrentMovie;
    private List<Result> videoList;
    private int videoPosition = 0;
    private String trailerKey;
    private RecyclerView revRecyclerView;
    private int reviews_no;
    private Cursor mMoviesId;
    private ArrayList favMoviesIdList;
    private boolean verifID;
    private int rowID;
    private int favMovieIndex;
    private String path;

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
        voteAverage = String.valueOf(vote);

        String releaseDate = mCurrentMovie.getReleaseDate();
        yearRelease = releaseDate.substring(0, Math.min(releaseDate.length(), 4));


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
        favorites_cb.setOnClickListener(this);


        getTrailerData();
        getReviewData();

        getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER_ID, null, this);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error !!!!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageViewTrailer1:
                videoPosition = 0;
                startVideo();
                break;
            case R.id.imageView2:
                videoPosition = 1;
                startVideo();
                break;
            case R.id.imageView3:
                videoPosition = 2;
                startVideo();
                break;
            case R.id.imageView4:
                videoPosition = 3;
                startVideo();
                break;
            case R.id.checkBox:
                onFavoritesClicked();
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

    public void onFavoritesClicked() {

        if (verifID) {
            removeMovieFromFav();
        } else {
            addMovieToFav();
        }
    }

    private void startVideo() {
        trailerKey = videoList.get(videoPosition).getKey();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
        intent.putExtra("VIDEO_ID", trailerKey);
        startActivity(intent);
    }

    private String savePosterToInternalStorage(Bitmap posterImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir("posterDir", Context.MODE_PRIVATE);
        File myPosterPath = new File(directory, String.valueOf(movieID) + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPosterPath);
            posterImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPosterPath.getAbsolutePath();
    }

    private String saveBackdropToInternalStorage(Bitmap posterImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir("backdropDir", Context.MODE_PRIVATE);
        File myBackdropPath = new File(directory, String.valueOf(movieID) + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myBackdropPath);
            posterImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myBackdropPath.getAbsolutePath();
    }

    private boolean isInFavoriteDB() {
        verifID = favMoviesIdList.contains(movieID);
        return verifID;
    }

    private void addMovieToFav() {
        String posterPath = savePosterToInternalStorage(((BitmapDrawable) poster_iv.getDrawable()).getBitmap());
        String backdropPath = saveBackdropToInternalStorage(((BitmapDrawable) backdrop_iv.getDrawable()).getBitmap());

        ContentValues values = new ContentValues();
        values.put(MoviesEntry.COLUMN_MOVIE_ID, movieID);
        values.put(MoviesEntry.COLUMN_TITLE, mCurrentMovie.getMovieName());
        values.put(MoviesEntry.COLUMN_RATING, voteAverage);
        values.put(MoviesEntry.COLUMN_OVERVIEW, mCurrentMovie.getOverview());
        values.put(MoviesEntry.COLUMN_RELEASE_DATE, yearRelease);
        values.put(MoviesEntry.COLUMN_POSTER, posterPath);
        values.put(MoviesEntry.COLUMN_BACKDROP, backdropPath);


        Uri newUri = getContentResolver().insert(MoviesEntry.CONTENT_URI, values);
        Toast add_message = Toast.makeText(this, getString(R.string.add_to_fav), Toast.LENGTH_SHORT);
        add_message.show();
    }

    private void removeMovieFromFav() {
        String selection = "id =" + String.valueOf(mCurrentMovie.getId());
        String[] rowID = {MoviesEntry._ID};
        Cursor cursor = getContentResolver().query(MoviesEntry.CONTENT_URI, rowID, selection, null, null);
        cursor.moveToFirst();
        favMovieIndex = cursor.getInt(cursor.getColumnIndex(MoviesEntry._ID));
        String stringId = String.valueOf(favMovieIndex);
        Uri uri = MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        getContentResolver().delete(uri, null, null);
        cursor.close();
        Toast remove_message = Toast.makeText(this, getString(R.string.remove_from_fav), Toast.LENGTH_SHORT);
        remove_message.show();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {


            @Override
            public Cursor loadInBackground() {
                try {
                    String[] projection = {
                            MoviesEntry.COLUMN_MOVIE_ID
                    };
                    return getContentResolver().query(MoviesEntry.CONTENT_URI,
                            projection,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mMoviesId != null) {
                    deliverResult(mMoviesId);
                } else {
                    forceLoad();
                }
            }

            public void deliverResult(Cursor data) {
                mMoviesId = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favMoviesIdList = new ArrayList();
        data.moveToFirst();
        try {
            do {
                int favMovieID = data.getInt(data.getColumnIndex(MoviesEntry.COLUMN_MOVIE_ID));
                favMoviesIdList.add(favMovieID);
            } while (data.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        }
        isInFavoriteDB();
        favorites_cb.setChecked(verifID);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}