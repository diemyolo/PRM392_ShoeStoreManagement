package com.shoestoreproject.UI_Layer.View;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shoestoreproject.Data_Layer.Helper.ManagementCart;
import com.shoestoreproject.Data_Layer.Model.Shoe;
import com.shoestoreproject.Data_Layer.Model.Slider;
import com.shoestoreproject.UI_Layer.Adapter.ColorAdapter;
import com.shoestoreproject.UI_Layer.Adapter.SizeAdapter;
import com.shoestoreproject.UI_Layer.Adapter.SliderAdapter;
import com.shoestoreproject.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private Shoe item;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagementCart(this);

        getBundle();
        banners();
        initLists();
    }

    private void initLists() {
        ArrayList<String> sizeList = new ArrayList<>();
        for (String size : item.getSize()) {
            sizeList.add(size);
        }

        binding.sizeList.setAdapter(new SizeAdapter(sizeList));
        binding.sizeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<String> colorList = new ArrayList<>();
        for (String imageUrl : item.getPicUrl()) {
            colorList.add(imageUrl);
        }

        binding.colorList.setAdapter(new ColorAdapter(colorList));
        binding.colorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void banners() {
        ArrayList<Slider> sliderItems = new ArrayList<>();
        for (String imageUrl : item.getPicUrl()) {
            sliderItems.add(new Slider(imageUrl));
        }

        binding.slider.setAdapter(new SliderAdapter(sliderItems, binding.slider));
        binding.slider.setClipToPadding(false);
        binding.slider.setClipChildren(false);
        binding.slider.setOffscreenPageLimit(3);
        binding.slider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        if (sliderItems.size() > 1) {
            binding.dotindicator.setVisibility(View.VISIBLE);
            binding.dotindicator.attachTo(binding.slider);
        }
    }

    private void getBundle() {
        item = getIntent().getParcelableExtra("object");

        binding.titleTxt.setText(item.getTitle());
        binding.descriptionTxt.setText(item.getDescription());
        binding.priceTxt.setText("$" + item.getPrice());
        binding.ratingTxt.setText(item.getRating() + " Rating");

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setNumberInCart(numberOrder);
                managementCart.insertFood(item);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, CartActivity.class));
            }
        });
    }
}
