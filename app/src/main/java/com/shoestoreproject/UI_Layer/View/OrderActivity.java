package com.shoestoreproject.UI_Layer.View;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.shoestoreproject.Data_Layer.Model.Order;
import com.shoestoreproject.UI_Layer.Adapter.OrderAdapter;
import com.shoestoreproject.UI_Layer.ViewModel.MainViewModel;
import com.shoestoreproject.databinding.ActivityOrderBinding;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private ActivityOrderBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupRecyclerView();

        viewModel.orders.observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null) {
                    // Update RecyclerView adapter with new list of orders
                    OrderAdapter adapter = new OrderAdapter(orders);
                    binding.recyclerViewOrders.setAdapter(adapter);
                }
            }
        });

        viewModel.loadOrders();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewOrders.setLayoutManager(layoutManager);
        // Optionally, you can set item decorations, animations, etc. here
    }
}

