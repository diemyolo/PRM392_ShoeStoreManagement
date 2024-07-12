package com.shoestoreproject.UI_Layer.Adapter;
import com.shoestoreproject.Data_Layer.Model.Slider;
import com.shoestoreproject.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<Slider> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<Slider> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems = sliderItems;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item_container, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position), context);
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        public void setImage(Slider sliderItem, Context context) {
            RequestOptions requestOptions = new RequestOptions().centerInside();
            Glide.with(context)
                    .load(sliderItem.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}

