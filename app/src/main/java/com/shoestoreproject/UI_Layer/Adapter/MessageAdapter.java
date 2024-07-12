package com.shoestoreproject.UI_Layer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.Data_Layer.Model.Message;
import com.shoestoreproject.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final int VIEW_TYPE_RECEIVER = 1;
    private static final int VIEW_TYPE_SENDER = 2;
    private static final int VIEW_TYPE_DEFAULT_RECEIVER = 3;

    private List<Message> items;
    private String userEmail;
    private String id;
    private Context context;

    public MessageAdapter(List<Message> items, String userEmail) {
        this.items = items;
        this.userEmail = userEmail;
        getUserId();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_DEFAULT_RECEIVER;
        } else {
            Message message = items.get(position - 1);
            getUserId();
            if (message.getSenderId().equals(id)) {
                return VIEW_TYPE_SENDER;
            } else {
                return VIEW_TYPE_RECEIVER;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_SENDER) {
            view = inflater.inflate(R.layout.viewholder_chat_sender, parent, false);
            return new ViewHolderUser(view);
        } else if(viewType == VIEW_TYPE_RECEIVER){
            view = inflater.inflate(R.layout.viewholder_chat_receiver, parent, false);
            return new ViewHolderAdmin(view);
        } else{
            view = inflater.inflate(R.layout.viewholder_chat_receiver, parent, false);
            return new ViewHolderAdmin(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) items.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SENDER:
                ((ViewHolderUser) holder).bind(message);
                break;
            case VIEW_TYPE_RECEIVER:
                ((ViewHolderAdmin) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolderAdmin extends RecyclerView.ViewHolder {
        TextView textMessage;

        public ViewHolderAdmin(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
        void bind(Message message) {
            textMessage.setText(message.getMessage());
        }
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {
        TextView textMessage;

        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
        void bind(Message message) {
            textMessage.setText(message.getMessage());
        }
    }

    public void getUserId() {
        DatabaseReference accountRef = firebaseDatabase.getReference("accounts");
        accountRef.orderByChild("email").equalTo(userEmail).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        if (account != null) {
                            id = account.getAccountId();
                            notifyDataSetChanged();
                            return;
                        }
                    }
                } else {
                    id = null;
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                id = null;
                notifyDataSetChanged();
            }
        });
    }
}
