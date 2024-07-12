package com.shoestoreproject.Data_Layer.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import java.util.UUID;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shoestoreproject.Data_Layer.DataSource.FirebaseDataSource;
import com.shoestoreproject.Data_Layer.Model.Account;

public class AccountRepository {
    private FirebaseDataSource firebaseDataSource;
    public AccountRepository() {
        firebaseDataSource = new FirebaseDataSource();
    }

    public MutableLiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResult = new MutableLiveData<>();
        DatabaseReference accountRef = firebaseDataSource.getFirebaseDatabase().getReference("accounts");
        accountRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapShot : dataSnapshot.getChildren()){
                        Account account = snapShot.getValue(Account.class);
                        if (account != null && account.getPassword().equals(password)) {
                            loginResult.setValue("Login successfully");
                            return;
                        }
                    }
                    loginResult.setValue("Incorrect email or password!");
                } else {
                    loginResult.setValue("Error fetching data!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loginResult.setValue("Database error: " + error.getMessage());
            }
        });
        return loginResult;
    }

    public MutableLiveData<String> register(String email, String password, String userName) {
        MutableLiveData<String> registerResult = new MutableLiveData<>();
        DatabaseReference accountRef = firebaseDataSource.getFirebaseDatabase().getReference("accounts");

        accountRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    registerResult.setValue("Email already registered");
                } else {
                    String accountNumber = accountRef.push().getKey();
                    String userId = UUID.randomUUID().toString();
                    Account newAccount = new Account( userId, null, userName, password, email, "active", "user");
                    accountRef.child(accountNumber).setValue(newAccount).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            registerResult.setValue("Registration successfully");
                        } else {
                            registerResult.setValue("Registration failed: " + task.getException().getMessage());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                registerResult.setValue("Database error: " + error.getMessage());
            }
        });
        return registerResult;
    }
    public MutableLiveData<Account> getAccountInfo(String email) {
        MutableLiveData<Account> accountLiveData = new MutableLiveData<>();
        DatabaseReference accountRef = firebaseDataSource.getFirebaseDatabase().getReference("accounts");

        accountRef.orderByChild("email").equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Account account = snapshot.getValue(Account.class);
                        if (account != null) {
                            accountLiveData.setValue(account);
                            return;
                        }
                    }
                } else {
                    accountLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                accountLiveData.setValue(null);
            }
        });
        return accountLiveData;
    }
}
