package com.shoestoreproject.Data_Layer.Model;

public class CartItem {
    private String cartItemId;
    private String shoeId;
    private String shoeName;
    private String shoeImage;
    private double price;
    private int quantity;


    public CartItem() {
    }

    public CartItem(String cartItemId, String shoeId, String shoeName, String shoeImage, double price, int quantity) {
        this.cartItemId = cartItemId;
        this.shoeId = shoeId;
        this.shoeName = shoeName;
        this.shoeImage = shoeImage;
        this.price = price;
        this.quantity = quantity;
    }

    public String getShoeId() {
        return shoeId;
    }

    public void setShoeId(String shoeId) {
        this.shoeId = shoeId;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public String getShoeImage() {
        return shoeImage;
    }

    public void setShoeImage(String shoeImage) {
        this.shoeImage = shoeImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }
}
