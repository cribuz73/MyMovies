package com.example.android.mymovies.NetworkUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristi on 3/18/2018.
 */

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {

            return new Review[size];
        }
    };
    private String revAuthor;
    private String revContent;

    protected Review(Parcel in) {
        revAuthor = in.readString();
        revContent = in.readString();
    }

    public Review() {
    }

    public Review(String revAuthor, String revContent) {
        this.revAuthor = revAuthor;
        this.revContent = revContent;
    }


    public String getRevAuthor() {
        return revAuthor;
    }

    public void setRevAuthor(String name) {
        this.revAuthor = revAuthor;
    }

    public String getRevContent() {
        return revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(revAuthor);
        dest.writeString(revContent);
    }
}
