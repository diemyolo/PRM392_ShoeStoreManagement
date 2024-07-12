package com.shoestoreproject.UI_Layer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shoestoreproject.R;
import com.shoestoreproject.databinding.ViewholderSizeBinding;
import com.shoestoreproject.databinding.ViewholderSizeBinding;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    private List<String> items;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    private Context context;

    public SizeAdapter(List<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewholderSizeBinding binding;

        public ViewHolder(ViewholderSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderSizeBinding binding = ViewholderSizeBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.sizeTxt.setText(items.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;

                lastSelectedPosition = selectedPosition;
                selectedPosition = adapterPosition;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                if (selectedPosition == adapterPosition) {
                    holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected);
                    holder.binding.sizeTxt.setTextColor(ContextCompat.getColor(context, R.color.darkGrey));
                } else {
                    holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg);
                    holder.binding.sizeTxt.setTextColor(ContextCompat.getColor(context, R.color.black));

                }
            }
        });

        if (selectedPosition == position) {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected);
        } else {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
