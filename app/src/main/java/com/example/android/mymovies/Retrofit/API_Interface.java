package com.example.android.mymovies.Retrofit;

import com.example.android.mymovies.Retrofit.Model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cristi on 3/19/2018.
 */

public interface API_Interface {

    @GET("{id}/videos?")
    Call<VideoResponse> getAnswer(@Path("id") int movieID, @Query("api_key") String API_key);
}
