package com.example.android.mymovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.mymovies.AsyncUtils.MyAsyncTask;
import com.example.android.mymovies.NetworkUtils.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static String option = "popular";
    public MovieAdapter mAdapter;
    public List<Movie> movies;
    MyAsyncTask asyncTask = new MyAsyncTask(new MyAsyncTask.AsyncInterface() {

        @Override
        public void onTaskComplete(List movies) {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            if (movies != null) {
                mAdapter.setMovieData(movies);

            }
        }

    });
    private RecyclerView mImagesItems;
    private TextView mEmptyView;

    public static String getOpt() {
        return option;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImagesItems = findViewById(R.id.image_rv);
        mEmptyView = findViewById(R.id.empty_view);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mImagesItems.setLayoutManager(layoutManager);

        mImagesItems.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mImagesItems.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            loadMovieData();

        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyView.setText(R.string.no_internet_connection);
        }
    }

    private void loadMovieData() {
        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.most_popular:
                option = "popular";
                loadMovieData();
                return true;
            case R.id.top_rated:
                option = "top_rated";
                loadMovieData();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {

        Movie movie = movies.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("currentMovie", movie);
        startActivity(intent);

    }

//    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
//
//        @Override
//        protected List<Movie> doInBackground(String... lists) {


//            URL jsonURL = NetworkUtils.buildUrl();

    //          String movieJson = null;


    //          try {
    //              movieJson = NetworkUtils.getResponseFromHttpUrl(jsonURL);

//                movies = NetworkJson.extractMovieFromJson(movieJson);
//                return movies;

    //           } catch (IOException e) {
    //               Log.e(TAG, "Problem making the HTTP request.", e);
    //           }
    //           return null;
    //       }

    //       @Override
    //       protected void onPreExecute() {
    //           mAdapter.setMovieData(null);
    //       }

    //      @Override
    //      protected void onPostExecute(List<Movie> movies) {

//            View loadingIndicator = findViewById(R.id.loading_indicator);
    //           loadingIndicator.setVisibility(View.GONE);

//            super.onPostExecute(movies);
//            if (movies != null) {
//                mAdapter.setMovieData(movies);
//            }
    //       }
    //   }

}
