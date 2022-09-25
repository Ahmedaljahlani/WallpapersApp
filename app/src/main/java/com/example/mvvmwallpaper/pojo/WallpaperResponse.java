package com.example.mvvmwallpaper.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WallpaperResponse {

    @SerializedName("photos")
    private ArrayList<WallpapersModel> photosList;

    public WallpaperResponse(ArrayList<WallpapersModel> photosList) {
        this.photosList = photosList;
    }

    public ArrayList<WallpapersModel> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(ArrayList<WallpapersModel> photosList) {
        this.photosList = photosList;
    }
}
