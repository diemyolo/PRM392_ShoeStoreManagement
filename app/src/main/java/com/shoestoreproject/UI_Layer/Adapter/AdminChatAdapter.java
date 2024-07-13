package com.shoestoreproject.UI_Layer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.Data_Layer.Model.Brand;
import com.shoestoreproject.R;

import java.util.List;

public class AdminChatAdapter extends RecyclerView.Adapter<AdminChatAdapter.ChatViewHolder> {

    private List<Account> items;
    private Context context;

    private String userEmail;

    public AdminChatAdapter(List<Account> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_admin_support, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Account currentItem = items.get(position);
        holder.usernameTextView.setText(currentItem.getUsername());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }
}