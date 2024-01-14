package com.example.mvvmwallpaper.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WallpapersModel implements Parcelable {
    @SerializedName("src")
    private ImagesDimensions src;
    private String avg_color;

    protected WallpapersModel(Parcel in) {
        avg_color = in.readString();
    }

    public static final Creator<WallpapersModel> CREATOR = new Creator<WallpapersModel>() {
        @Override
        public WallpapersModel createFromParcel(Parcel in) {
            return new WallpapersModel(in);
        }

        @Override
        public WallpapersModel[] newArray(int size) {
            return new WallpapersModel[size];
        }
    };

    public WallpapersModel() {

    }

    public String getAvg_color() {
        return avg_color;
    }

    public void setAvg_color(String avg_color) {
        this.avg_color = avg_color;
    }

    public WallpapersModel(ImagesDimensions src) {
        this.src = src;

    }

    public ImagesDimensions getSrc() {
        return src;
    }

    public void setSrc(ImagesDimensions src) {
        this.src = src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(avg_color);
    }


    public class ImagesDimensions {
        @SerializedName("medium")
        private String medium;

        @SerializedName("large")
        private String large;

        @SerializedName("portrait")
        private String portrait;

        public ImagesDimensions(String medium, String large,String portrait) {
            this.medium = medium;
            this.large = large;
            this.portrait=portrait;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }}
