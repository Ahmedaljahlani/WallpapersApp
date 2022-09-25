package com.example.mvvmwallpaper.pojo;

import com.google.gson.annotations.SerializedName;

public class WallpapersModel {
    @SerializedName("src")
    private ImagesDimensions src;
    private String avg_color;

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


    public class ImagesDimensions {
        @SerializedName("medium")
        private String medium;

        @SerializedName("large")
        private String large;

        public ImagesDimensions(String medium, String large) {
            this.medium = medium;
            this.large = large;
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
    }}
