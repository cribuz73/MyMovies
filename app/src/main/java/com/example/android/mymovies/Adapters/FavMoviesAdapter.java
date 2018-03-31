package com.example.android.mymovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.mymovies.Data.DataContract.MoviesEntry;
import com.example.android.mymovies.Data.MovieSQLite;
import com.example.android.mymovies.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Cristi on 3/30/2018.
 */

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.ImageViewHolder> {

    private static final String TAG = FavMoviesAdapter.class.getSimpleName();
    private MovieSQLite mDbMovieHelper;
    private ArrayList<Bitmap> postersBitmap = getPosterBitmapFromDB();
    private Context context;
    private FavMovieAdapterOnClickHandler mClickHandler;

    public FavMoviesAdapter(FavMovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public void setMovieDBData(ArrayList<Bitmap> bitmaps) {
        postersBitmap = bitmaps;
        notifyDataSetChanged();
    }

    @Override
    public FavMoviesAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layoutIdForListItem = R.layout.image_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        FavMoviesAdapter.ImageViewHolder viewHolder = new FavMoviesAdapter.ImageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavMoviesAdapter.ImageViewHolder holder, int position) {

        Log.d(TAG, "#" + position);
        Bitmap b = postersBitmap.get(position);
        holder.mMovieImageView.setImageBitmap(b);

    }

    @Override
    public int getItemCount() {
        return postersBitmap.size();
    }

    public ArrayList<Bitmap> getPosterBitmapFromDB() {

        //   mDbMovieHelper = new MovieSQLite(context);
        SQLiteDatabase database = mDbMovieHelper.getReadableDatabase();


        String selectQuery = "SELECT * FROM " + MoviesEntry.TABLE_NAME;
        String[] projection = {
                MoviesEntry.COLUMN_POSTER
        };
        Cursor cursor = database.query(MoviesEntry.TABLE_NAME, projection, selectQuery, null, null, null, null);

        postersBitmap = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String posterPath = cursor.getString(cursor.getColumnIndex(MoviesEntry.COLUMN_POSTER));
                try {
                    File f = new File(posterPath);
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    postersBitmap.add(b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return postersBitmap;
    }


    public interface FavMovieAdapterOnClickHandler {
        void onClick(int position);

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mMovieImageView;

        public ImageViewHolder(View itemView) {
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
