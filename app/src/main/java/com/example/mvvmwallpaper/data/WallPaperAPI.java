package com.example.mvvmwallpaper.data;

import com.example.mvvmwallpaper.pojo.WallpaperResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WallPaperAPI {

    @GET("popular/?page=1&per_page=80")
    Call<WallpaperResponse> getWallpapers(
            @Header("Authorization") String credentials
    );

    @GET("search?/page=1&per_page=80")
    Call<WallpaperResponse> getCategories(

            @Header("Authorization") String credentials,
            @Query("query") String queryText
    );
}
