package com.shoestoreproject.Data_Layer.Model;

public class Category {
    private String cateId;
    private String cateName;

    // Default constructor for Firebase
    public Category() {
    }

    // Constructor
    public Category(String cateId, String cateName) {
        this.cateId = cateId;
        this.cateName = cateName;
    }

    // Getters and Setters
    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}

