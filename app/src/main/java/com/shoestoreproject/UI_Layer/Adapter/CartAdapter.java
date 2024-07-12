package com.shoestoreproject.UI_Layer.Adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shoestoreproject.Data_Layer.Helper.ChangeNumberItemListener;
import com.shoestoreproject.Data_Layer.Helper.ManagementCart;
import com.shoestoreproject.Data_Layer.Model.Shoe;
import com.shoestoreproject.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<Shoe> listItemSelected;
    private Context context;
    private ChangeNumberItemListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Shoe> listItemSelected, Context context, ChangeNumberItemListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewholderCartBinding binding;
        private ManagementCart managementCart;

        public ViewHolder(ViewholderCartBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.managementCart = new ManagementCart(context);
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Shoe item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(item.getNumberInCart() * item.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().centerCrop())
                .into(holder.binding.pic);

        holder.binding.plusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.managementCart.plusItem(listItemSelected, position, new ChangeNumberItemListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });

        holder.binding.minusCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.managementCart.minusItem(listItemSelected, position, new ChangeNumberItemListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }
}
