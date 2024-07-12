package com.shoestoreproject.UI_Layer.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shoestoreproject.R;
import com.shoestoreproject.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        binding.textViewNotify.setText(intent.getStringExtra("result"));
    }
}