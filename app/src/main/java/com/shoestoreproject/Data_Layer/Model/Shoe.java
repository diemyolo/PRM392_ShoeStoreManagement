package com.shoestoreproject.Data_Layer.Model;

import java.util.ArrayList;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Shoe implements Parcelable {
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private ArrayList<String> size;
    private double price;
    private double rating;
    private int numberInCart;

    public Shoe() {
        this.title = "";
        this.description = "";
        this.picUrl = new ArrayList<>();
        this.size = new ArrayList<>();
        this.price = 0.0;
        this.rating = 0.0;
        this.numberInCart = 0;
    }

    public Shoe(Parcel in) {
        title = in.readString();
        description = in.readString();
        picUrl = in.createStringArrayList();
        size = in.createStringArrayList();
        price = in.readDouble();
        rating = in.readDouble();
        numberInCart = in.readInt();
    }

    public static final Creator<Shoe> CREATOR = new Creator<Shoe>() {
        @Override
        public Shoe createFromParcel(Parcel in) {
            return new Shoe(in);
        }

        @Override
        public Shoe[] newArray(int size) {
            return new Shoe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(picUrl);
        dest.writeStringList(size);
        dest.writeDouble(price);
        dest.writeDouble(rating);
        dest.writeInt(numberInCart);
    }

    // Getters and Setters for all fields

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}

