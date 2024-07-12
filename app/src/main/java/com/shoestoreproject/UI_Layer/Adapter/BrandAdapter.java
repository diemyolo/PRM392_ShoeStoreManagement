package com.shoestoreproject.UI_Layer.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shoestoreproject.R;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.shoestoreproject.Data_Layer.Model.Brand;
import com.shoestoreproject.databinding.ViewholderBrandBinding;


import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private List<Brand> items;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    private Context context;

    public BrandAdapter(List<Brand> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderBrandBinding binding = ViewholderBrandBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewholderBrandBinding binding;

        public ViewHolder(ViewholderBrandBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Brand item) {
            binding.title.setText(item.getTitle());
            Glide.with(binding.getRoot().getContext())
                    .load(item.getPicUrl())
                    .apply(new RequestOptions().centerCrop())
                    .into(binding.pic);

            // Determine if the current item is selected
            boolean isSelected = getAdapterPosition() == selectedPosition;
            binding.title.setTextColor(
                    ContextCompat.getColor(context, R.color.white));
            // Update views based on selection state
            updateSelection(isSelected);

            // Handle item click
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear previous selection
                    lastSelectedPosition = selectedPosition;
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(lastSelectedPosition);
                    notifyItemChanged(selectedPosition);

                    // Update current selection state
                    updateSelection(true);  // true because it's clicked
                }
            });
        }

        private void updateSelection(boolean isSelected) {
            if (isSelected) {
                binding.pic.setBackgroundColor(Color.TRANSPARENT);
                binding.mainLayout.setBackgroundResource(R.drawable.black_button_bg);
                ImageViewCompat.setImageTintList(
                        binding.pic,
                        ColorStateList.valueOf(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white))
                );
                binding.title.setVisibility(View.VISIBLE);
            } else {
                binding.pic.setBackgroundResource(R.drawable.grey_bg);
                binding.mainLayout.setBackgroundResource(0);
                ImageViewCompat.setImageTintList(
                        binding.pic,
                        ColorStateList.valueOf(ContextCompat.getColor(binding.getRoot().getContext(), R.color.black))
                );
                binding.title.setVisibility(View.GONE);
            }
        }

    }
}

