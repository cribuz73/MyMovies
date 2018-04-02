package com.example.android.mymovies.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class DbMoviesAdapter extends RecyclerView.Adapter<DbMoviesAdapter.TaskViewHolder> {

    public List<String> mFavMoviesPath;
    private Context mContext;

    public DbMoviesAdapter(Context mContext) {
        this.mContext = mContext;
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

        String moviePath = mFavMoviesPath.get(position);

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
        if (mFavMoviesPath == null) {
            return 0;
        }
        return mFavMoviesPath.size();
    }


    public void setMovieData(List<String> favmovies) {
        mFavMoviesPath = favmovies;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        public ImageView mMovieImageView;


        public TaskViewHolder(View itemView) {
            super(itemView);

            mMovieImageView = itemView.findViewById(R.id.image_gv);

        }
    }
}
