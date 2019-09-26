package com.example.karim.muzzafapp;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private  LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MyItem(double lat, double lng) {
        this.mTitle = mTitle;
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(LatLng mPosition, String mTitle, String mSnippet, String image) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.image = image;
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
