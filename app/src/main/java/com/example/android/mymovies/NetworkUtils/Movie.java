package com.example.android.mymovies.NetworkUtils;

/**
 * Created by Cristi on 3/9/2018.
 */

public class Movie {

    private String movieName;
    private String releaseDate;
    private double voteAverage;
    private String overview;
    private String image;

    public Movie() {

    }

    public Movie(String movieName, String releaseDate, double voteAverage, String overview, String image) {

        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
