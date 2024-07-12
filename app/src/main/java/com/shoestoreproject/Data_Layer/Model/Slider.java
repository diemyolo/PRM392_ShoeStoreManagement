package com.shoestoreproject.Data_Layer.Model;

public class Slider {

    private String url;

    public Slider() {
        this.url = "";
    }

    public Slider(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
