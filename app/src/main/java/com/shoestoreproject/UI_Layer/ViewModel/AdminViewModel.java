package com.shoestoreproject.UI_Layer.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoestoreproject.Data_Layer.Model.Account;

public class AdminViewModel extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public MutableLiveData<Account> _account = new MutableLiveData<>();
    public LiveData<Account> account = _account;
    public void getAccountInfo(String email) {
        DatabaseReference accountRef = firebaseDatabase.getReference("accounts");
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
}
