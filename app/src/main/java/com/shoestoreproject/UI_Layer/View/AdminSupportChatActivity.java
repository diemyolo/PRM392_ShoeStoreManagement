package com.shoestoreproject.UI_Layer.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shoestoreproject.Data_Layer.Model.Message;
import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.Adapter.MessageAdapter;
import com.shoestoreproject.UI_Layer.ViewModel.AdminSupportChatViewModel;
import com.shoestoreproject.UI_Layer.ViewModel.MainViewModel;
import com.shoestoreproject.databinding.ActivityAdminSupportChatBinding;
import com.shoestoreproject.databinding.ActivityHomeBinding;

import java.util.List;

public class AdminSupportChatActivity extends AppCompatActivity {
    public ActivityAdminSupportChatBinding binding;
    private AdminSupportChatViewModel viewModel = new AdminSupportChatViewModel();
    public String accountId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSupportChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("accountId")) {
            accountId = intent.getStringExtra("accountId");
        }
        setButtonActions();
        initMessage();
        sendBtnOnClickListener();
    }
    private void setButtonActions() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initMessage() {
        viewModel._messageList.observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(AdminSupportChatActivity.this);
                layoutManager.setStackFromEnd(true);
                binding.viewChat.setLayoutManager(layoutManager);
                MessageAdapter adapter = new MessageAdapter(messages, "admin1@example.com");
                binding.viewChat.setAdapter(adapter);
                binding.viewChat.scrollToPosition(messages.size());
                binding.progressBarChat.setVisibility(View.GONE);
            }
        });
        viewModel.getMessage("admin1@example.com", accountId);
    }
    private void sendBtnOnClickListener(){
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.sendMessage.getText().toString().isEmpty() || binding.sendMessage.getText().toString().length() <= 0){
                    Toast.makeText(AdminSupportChatActivity.this, "Please input text", Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.addMessage(accountId, binding.sendMessage.getText().toString());

                    binding.sendMessage.setText("");
                }
            }
        });
    }
}