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

import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.ViewModel.AdminViewModel;
import com.shoestoreproject.UI_Layer.ViewModel.ProfileViewModel;
import com.shoestoreproject.databinding.ActivityAdminBinding;
import com.shoestoreproject.databinding.ActivityProfileBinding;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private String userEmail;
    private AdminViewModel viewModel = new AdminViewModel();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("email")) {
            userEmail = intent.getStringExtra("email");
            viewModel.account.observe(this, new Observer<Account>() {
                @Override
                public void onChanged(Account account) {
                    if (account != null) {
                        binding.usernameText.setText(account.getUsername());
                    }
                }
            });
            viewModel.getAccountInfo(userEmail);
        }
        buttonLogoutOnClickListener();
    }

    public void buttonLogoutOnClickListener(){
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });
    }
}