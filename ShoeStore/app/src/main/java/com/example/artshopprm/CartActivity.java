package com.example.artshopprm;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Adapter.CartAdapter;
import com.example.artshopprm.Api.CreateOrder;
import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.Entity.Order;
import com.example.artshopprm.Entity.OrderDetail;
import com.example.artshopprm.Service.ManagementCart;
import com.example.artshopprm.databinding.ActivityCartBinding;
import com.example.artshopprm.databinding.ActivityDetailBinding;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CartActivity extends  BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagementCart managementCart;
    float deliveryFee = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managementCart = new ManagementCart(this);
        initList();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        binding.PlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionOrder();
            }
        });
    }

    private void initList() {

        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        binding.deliveryTxt.setText(String.valueOf(deliveryFee));
        adapter = new CartAdapter(managementCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double total = managementCart.getTotalFee();
        binding.totalTxt.setText("$" + total);
    }

    public double getTotalOrderPrice(){
        List<Art> arts = managementCart.getListCart();
        double totalPrice = 0;
        for (Art art : arts) {
            String orderDetailId = UUID.randomUUID().toString();
            double actualPrice = art.getNumberInCart() * art.getPrice();
            totalPrice+=actualPrice;
        }
        return totalPrice - deliveryFee;
    }
    private void actionOrder(){
        List<Art> arts = managementCart.getListCart();
        if (arts.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }


        String orderId = UUID.randomUUID().toString();
        Date now = new Date();
        Account acc = managementCart.getUserLogined("User");
        String address = binding.addressTxt.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        String currentTime = dateFormat.format(now);
        double total = getTotalOrderPrice();
        // Create an Order object
        Order order = new Order(orderId, currentTime, currentTime, address, String.valueOf(acc.getId()), "Pending", true,total);
        DatabaseReference ordersRef = db.getReference("orders");
        DatabaseReference orderDetailsRef = db.getReference("orderDetails");

        // Save order to Firebase
        ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                saveOrderDetails(orderId, arts);
            } else {
                Toast.makeText(CartActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
            }
        });
        managementCart.emptyListCart();
    }

    private void saveOrderDetails(String orderId, List<Art> arts) {
        DatabaseReference orderDetailsRef = db.getReference("orderDetails");
        for (Art art : arts) {
            String orderDetailId = UUID.randomUUID().toString();
            double actualPrice = art.getNumberInCart() * art.getPrice();
            OrderDetail orderDetail = new OrderDetail(orderDetailId, new Date(), new Date(),
                    orderId, art.getId(), art.getNumberInCart(), actualPrice, true);
            orderDetailsRef.child(orderDetailId).setValue(orderDetail);
        }
    }

}
