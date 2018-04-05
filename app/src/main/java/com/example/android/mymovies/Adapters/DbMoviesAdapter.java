package com.example.android.mymovies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies.NetworkUtils.Movie;
import com.example.android.mymovies.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class DbMoviesAdapter extends RecyclerView.Adapter<DbMoviesAdapter.TaskViewHolder> {

    public List<Movie> mFavMovies;
    private Context mContext;
    private DbMovieAdapterOnClickHandler mClickHandler;

    public DbMoviesAdapter(DbMovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layoutIdForListItem = R.layout.image_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        Movie movie = mFavMovies.get(position);
        String moviePath = movie.getPoster();

        try {
            File f = new File(moviePath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.mMovieImageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (mFavMovies == null) {
            return 0;
        }
        return mFavMovies.size();
    }


    public void setMovieData(List<Movie> favmovies) {
        mFavMovies = favmovies;
        notifyDataSetChanged();
    }

    public interface DbMovieAdapterOnClickHandler {
        void onClick(int position);

    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mMovieImageView;


        public TaskViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.image_gv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
