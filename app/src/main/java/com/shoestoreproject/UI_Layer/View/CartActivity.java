package com.shoestoreproject.UI_Layer.View;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoestoreproject.Data_Layer.Helper.ChangeNumberItemListener;
import com.shoestoreproject.Data_Layer.Helper.ManagementCart;
import com.shoestoreproject.Data_Layer.Model.Order;
import com.shoestoreproject.Data_Layer.ZaloPayConfig.Api.CreateOrder;
import com.shoestoreproject.UI_Layer.Adapter.CartAdapter;
import com.shoestoreproject.databinding.ActivityCartBinding;
import org.json.JSONObject;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ManagementCart managmentCart;
    private double tax = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagementCart(this);

        setVariable();
        initCartList();
        calculateCart();
        redirectToPayment();
    }

    private void initCartList() {
        binding.viewCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.viewCart.setAdapter(new CartAdapter(managmentCart.getListCart(), this, new ChangeNumberItemListener() {
            @Override
            public void onChanged() {
                calculateCart();
            }
        }));
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollView2.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollView2.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10.0;
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void redirectToPayment(){
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    String totalText = binding.totalTxt.getText().toString();

                    String numericValue = totalText.replaceAll("\\$", "");
                    double totalDouble = Double.parseDouble(numericValue);
                    int multipliedValue = (int) Math.ceil(totalDouble * 25000);
                    numericValue = Integer.toString(multipliedValue);

                    JSONObject data = orderApi.createOrder(numericValue);
                    String code = data.getString("return_code");

                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {

                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                managmentCart.clearCart();
                                createOrder();
                                Intent intent1 = new Intent(CartActivity.this, PaymentActivity.class);
                                intent1.putExtra("result", "Thanh toán thành công");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent1 = new Intent(CartActivity.this, PaymentActivity.class);
                                intent1.putExtra("result", "Thanh toán thất bại");
                                startActivity(intent1);
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent1 = new Intent(CartActivity.this, PaymentActivity.class);
                                intent1.putExtra("result", "Lỗi thanh toán");
                                startActivity(intent1);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createOrder() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        String orderId = ordersRef.push().getKey();
        String totalText = binding.totalTxt.getText().toString();
        String numericValue = totalText.replaceAll("\\$", "");
        double totalPrice = Double.parseDouble(numericValue);
        String checkoutInfo = "Some checkout info";
        String accountId = "Some account ID";
        String status = "Pending";

        Order order = new Order(orderId, totalPrice, checkoutInfo, accountId, status);

        ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Order created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Failed to create order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
