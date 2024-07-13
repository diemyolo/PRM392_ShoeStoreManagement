package com.shoestoreproject.UI_Layer.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.Data_Layer.Model.Brand;
import com.shoestoreproject.Data_Layer.Model.Message;
import com.shoestoreproject.Data_Layer.Model.Shoe;
import com.shoestoreproject.Data_Layer.Model.Slider;
import com.shoestoreproject.R;
import com.shoestoreproject.UI_Layer.Adapter.BrandAdapter;
import com.shoestoreproject.UI_Layer.Adapter.MessageAdapter;
import com.shoestoreproject.UI_Layer.Adapter.PopularAdapter;
import com.shoestoreproject.UI_Layer.Adapter.SliderAdapter;
import com.shoestoreproject.UI_Layer.ViewModel.MainViewModel;
import com.shoestoreproject.databinding.ActivityHomeBinding;

import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private MainViewModel viewModel = new MainViewModel();
    private ActivityHomeBinding binding;
    private String userEmail;
    private float dX, dY;
    private int lastAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            userEmail = intent.getStringExtra("email");
            viewModel.account.observe(this, new Observer<Account>() {
                @Override
                public void onChanged(Account account) {
                    if (account != null) {
                        binding.userNameTextView.setText(account.getUsername());
                    }
                }
            });
            viewModel.getAccountInfo(userEmail);
        }


        initBanner();
        initOrder();
        initBrand();
        initPopular();
        initCartMenu();
        initMapMenu();
        chatBubbleOnClickListener();
        darkOverlayOnClickListener();
        initMessage();
        sendBtnOnClickListener();
        profileBtnOnClickListener();
    }

    private void toggleChatBox() {
        binding.popUpChatContainer.setVisibility(View.VISIBLE);
        binding.darkOverlay.setVisibility(View.VISIBLE);

        Animation floatUp = AnimationUtils.loadAnimation(this, R.anim.float_up);
        binding.popUpChatContainer.startAnimation(floatUp);
    }

    private void chatBubbleOnClickListener(){
        binding.chatBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleChatBox();
            }
        });
    }

    private void darkOverlayOnClickListener() {
        binding.darkOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation floatDown = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.float_down);
                floatDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        binding.popUpChatContainer.setVisibility(View.GONE);
                        binding.darkOverlay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                binding.popUpChatContainer.startAnimation(floatDown);
            }
        });
    }

    private void initCartMenu() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
    }

    private void initOrder() {
        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
            }
        });
    }

    private void initMapMenu() {
        binding.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        viewModel.banners.observe(this, images -> {
            binding.progressBarBanner.setVisibility(View.GONE);
            banners(images);
        });
        viewModel.loadBanners();
    }


    private void banners(List<Slider> images) {
        binding.viewPageSlider.setAdapter(new SliderAdapter(images, binding.viewPageSlider));
        binding.viewPageSlider.setClipToPadding(false);
        binding.viewPageSlider.setClipChildren(false);
        binding.viewPageSlider.setOffscreenPageLimit(3);
        binding.viewPageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPageSlider.setPageTransformer(compositePageTransformer);

        if (images.size() > 1) {
            binding.dotindicator.setVisibility(View.VISIBLE);
            binding.dotindicator.attachTo(binding.viewPageSlider);
        }
    }
    private void initBrand() {
        binding.progressBarBrand.setVisibility(View.VISIBLE);
        viewModel.brands.observe(this, new Observer<List<Brand>>() {
            @Override
            public void onChanged(List<Brand> brandModels) {
                binding.viewBrand.setLayoutManager(new LinearLayoutManager(
                        HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                binding.viewBrand.setAdapter(new BrandAdapter(brandModels));
                binding.progressBarBrand.setVisibility(View.GONE);
            }
        });

        viewModel.loadBrand();
    }
    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        viewModel.populars.observe(this, new Observer<List<Shoe>>() {
            @Override
            public void onChanged(List<Shoe> shoes) {
                binding.viewPopular.setLayoutManager(new GridLayoutManager(
                        HomeActivity.this, 2));
                binding.viewPopular.setAdapter(new PopularAdapter(shoes));
                binding.progressBarPopular.setVisibility(View.GONE);
            }
        });

        viewModel.loadPopular();
    }

    private void initMessage() {
        viewModel._messageList.observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                layoutManager.setStackFromEnd(true);
                binding.viewChat.setLayoutManager(layoutManager);
                MessageAdapter adapter = new MessageAdapter(messages, userEmail);
                binding.viewChat.setAdapter(adapter);
                binding.viewChat.scrollToPosition(messages.size());
                binding.progressBarChat.setVisibility(View.GONE);
            }
        });
        viewModel.getMessage(userEmail);
    }

    private void sendBtnOnClickListener(){
            binding.sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(binding.sendMessage.getText().toString().isEmpty() || binding.sendMessage.getText().toString().length() <= 0){
                        Toast.makeText(HomeActivity.this, "Please input text", Toast.LENGTH_SHORT).show();
                    }else{
                        viewModel.addMessage(userEmail, binding.sendMessage.getText().toString());

                        binding.sendMessage.setText("");
                    }
                }
            });
    }
    private void profileBtnOnClickListener(){
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }
        });
    }
}

