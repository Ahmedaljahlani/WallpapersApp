package com.example.mvvmwallpaper.data;

import com.example.mvvmwallpaper.pojo.WallpaperResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientResponse {
    private static String BASE_URL="https://api.pexels.com/v1/";
    private static ClientResponse INSTANCE;
    private static Retrofit retrofit;

    private OkHttpClient.Builder builder=new OkHttpClient.Builder();

    public ClientResponse(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        getAPI();
    }

    public static ClientResponse getINSTANCE() {
        if (INSTANCE==null){
            INSTANCE=new ClientResponse();
        }
        return INSTANCE;
    }

    public WallPaperAPI getAPI(){
        return retrofit.create(WallPaperAPI.class);
    }
}
