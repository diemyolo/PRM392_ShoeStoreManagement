package com.shoestoreproject.Data_Layer.Model;

public class OrderDetail {
    private String orderDetailId;
    private String shoeId;
    private String orderId;
    private int quantity;
    private double unitPrice;

    // Default constructor for Firebase
    public OrderDetail() {
    }

    // Constructor
    public OrderDetail(String orderDetailId, String shoeId, String orderId, int quantity, double unitPrice) {
        this.orderDetailId = orderDetailId;
        this.shoeId = shoeId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getShoeId() {
        return shoeId;
    }

    public void setShoeId(String shoeId) {
        this.shoeId = shoeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}

