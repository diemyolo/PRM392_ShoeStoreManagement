package com.shoestoreproject.UI_Layer.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shoestoreproject.Data_Layer.Model.Account;
import com.shoestoreproject.Data_Layer.Model.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminChatViewModel extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference accountRef = firebaseDatabase.getReference("accounts");
    private DatabaseReference messageRef = firebaseDatabase.getReference("messages");

    public MutableLiveData<Account> _account = new MutableLiveData<>();
    public LiveData<Account> account = _account;

    public MutableLiveData<List<Account>> _userAccount = new MutableLiveData<>();
    public LiveData<List<Account>> userAccount = _userAccount;

    public void getAccountInfo(String email) {
        accountRef.orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        if (account != null) {
                            _account.setValue(account);
                            return;
                        }
                    }
                } else {
                    _account.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                _account.setValue(null);
            }
        });
    }

    public void getUserMessageInfo(String email) {
        accountRef.orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        if (account != null) {
                            String adminId = account.getAccountId();
                            fetchMessagesByReceiverId(adminId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    private void fetchMessagesByReceiverId(String receiverId) {
        messageRef.orderByChild("receiverId").equalTo(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> senderIds = new HashSet<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        senderIds.add(message.getSenderId());
                    }
                }
                fetchUserAccountsBySenderIds(senderIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    private void fetchUserAccountsBySenderIds(Set<String> senderIds) {
        List<Account> userAccounts = new ArrayList<>();
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("accounts");
        Query query = accountRef.orderByChild("accountId");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Account account = snapshot.getValue(Account.class);
                    if (account != null && senderIds.contains(account.getAccountId())) {
                        userAccounts.add(account);
                    }
                }
                _userAccount.setValue(userAccounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        };

        // Execute the query once for all senderIds
        query.addListenerForSingleValueEvent(valueEventListener);
    }

}