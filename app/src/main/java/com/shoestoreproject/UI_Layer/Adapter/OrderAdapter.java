package com.shoestoreproject.UI_Layer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shoestoreproject.Data_Layer.Model.Order;
import com.shoestoreproject.R;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderIdTxt.setText("Order ID: " + order.getOrderId());
        holder.totalPriceTxt.setText("Total Price: $" + order.getTotalPrice());
        holder.checkoutInfoTxt.setText("Checkout Info: " + order.getCheckoutInfo());
        holder.accountIdTxt.setText("Account ID: " + order.getAccountId());
        holder.statusTxt.setText("Status: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTxt, totalPriceTxt, checkoutInfoTxt, accountIdTxt, statusTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTxt = itemView.findViewById(R.id.orderIdTxt);
            totalPriceTxt = itemView.findViewById(R.id.totalPriceTxt);
            checkoutInfoTxt = itemView.findViewById(R.id.checkoutInfoTxt);
            accountIdTxt = itemView.findViewById(R.id.accountIdTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
        }
    }
}


