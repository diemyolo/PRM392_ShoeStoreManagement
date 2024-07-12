package com.shoestoreproject.Data_Layer.Model;

public class Order {
    private String orderId;
    private double totalPrice;
    private String checkoutInfo;
    private String accountId;
    private String status;

    // Default constructor for Firebase
    public Order() {
    }

    // Constructor
    public Order(String orderId, double totalPrice, String checkoutInfo, String accountId, String status) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.checkoutInfo = checkoutInfo;
        this.accountId = accountId;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCheckoutInfo() {
        return checkoutInfo;
    }

    public void setCheckoutInfo(String checkoutInfo) {
        this.checkoutInfo = checkoutInfo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

