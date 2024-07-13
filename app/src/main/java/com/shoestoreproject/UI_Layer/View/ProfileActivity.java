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
import com.shoestoreproject.UI_Layer.ViewModel.MainViewModel;
import com.shoestoreproject.UI_Layer.ViewModel.ProfileViewModel;
import com.shoestoreproject.databinding.ActivityHomeBinding;
import com.shoestoreproject.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel = new ProfileViewModel();
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
        setButtonActions();
    }

    public void buttonLogoutOnClickListener(){
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }
    private void setButtonActions() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}