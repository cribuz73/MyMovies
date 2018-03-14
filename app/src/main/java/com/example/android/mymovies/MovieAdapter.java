package com.example.android.mymovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cristi on 3/9/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {

    private static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/w185/";

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movieList;

    private MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ImageViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        Movie movie = movieList.get(position);

        String imageSmallURL = movie.getImage();
        String imageURL = IMAGE_URL_BASE + imageSmallURL;


        Picasso.with(holder.itemView.getContext())
                .load(imageURL)
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    public void setMovieData(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }


    public interface MovieAdapterOnClickHandler {
        void onClick(int position);

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mMovieImageView;

        public ImageViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.image_gv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

}
