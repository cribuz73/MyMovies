package com.example.android.mymovies.Retrofit;

import com.example.android.mymovies.Retrofit.Model.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cristi on 3/23/2018.
 */

public interface API_Review_Interface {

    @GET("{id}/reviews?")
    Call<ReviewResponse> getAnswer(@Path("id") int movieID, @Query("api_key") String API_key);
}
