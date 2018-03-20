package com.example.android.mymovies.NetworkUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristi on 3/9/2018.
 */

public class Movie implements Parcelable {


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String movieName;
    private String releaseDate;
    private double voteAverage;
    private String overview;
    private String poster;
    private String backdrop;
    private int id;

    public Movie() {

    }

    public Movie(String movieName,
                 String releaseDate,
                 double voteAverage,
                 String overview,
                 String poster,
                 String backdrop,
                 int id) {

        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.poster = poster;
        this.backdrop = backdrop;
        this.id = id;
    }

    protected Movie(Parcel in) {
        movieName = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        id = in.readInt();
    }

    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String mainName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieName);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(backdrop);
        dest.writeInt(id);
    }
}
