package com.example.android.mymovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mymovies.Retrofit.Model.Review;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Cristi on 3/23/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    List<Review> reviewList;
    private Context context;


    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        //  reviewList = DetailActivity.getReviewsList();
        this.reviewList = reviewList;
    }

    //   public ReviewAdapter(DetailActivity context, List<Review> reviews) {
    //       reviewList = reviews;
    //       notifyDataSetChanged();
    //   }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {

        Review review = reviewList.get(position);
        holder.review_author_tv.setText(review.getAuthor());
        holder.review_content_tv.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView8)
        TextView review_author_tv;
        @BindView(R.id.textView9)
        TextView review_content_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            review_author_tv = itemView.findViewById(R.id.textView8);
            review_content_tv = itemView.findViewById(R.id.textView9);
        }
    }
}
