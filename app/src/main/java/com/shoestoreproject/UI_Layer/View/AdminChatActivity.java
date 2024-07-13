package com.shoestoreproject.UI_Layer.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.Data_Layer.Model.Message;
import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.Adapter.AdminChatAdapter;
import com.shoestoreproject.UI_Layer.Adapter.MessageAdapter;
import com.shoestoreproject.UI_Layer.ViewModel.AdminChatViewModel;
import com.shoestoreproject.databinding.ActivityAdminBinding;
import com.shoestoreproject.databinding.ActivityAdminChatBinding;

import java.util.List;

public class AdminChatActivity extends AppCompatActivity {
    private ActivityAdminChatBinding binding;
    private String adminEmail;
    private AdminChatViewModel viewModel = new AdminChatViewModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);
        binding = ActivityAdminChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("email")) {
            adminEmail = intent.getStringExtra("email");
        }
        setButtonActions();
        initUser();
    }

    private void setButtonActions() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUser() {
        viewModel._userAccount.observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                if (accounts != null) {
                    // Initialize the RecyclerView only once
                    if (binding.chatRecyclerView.getAdapter() == null) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(AdminChatActivity.this);
                        binding.chatRecyclerView.setLayoutManager(layoutManager);
                        AdminChatAdapter adapter = new AdminChatAdapter(accounts);
                        binding.chatRecyclerView.setAdapter(adapter);
                    } else {
                    }
                }
            }
        });
        viewModel.getUserMessageInfo(adminEmail);
    }

}