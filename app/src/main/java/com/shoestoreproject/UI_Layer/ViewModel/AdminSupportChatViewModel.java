package com.shoestoreproject.UI_Layer.ViewModel;

import android.util.Log;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AdminSupportChatViewModel extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public MutableLiveData<Account> _account = new MutableLiveData<>();
    public LiveData<Account> account = _account;

    public MutableLiveData<List<Message>> _messageList = new MutableLiveData<>();
    public LiveData<List<Message>> messageList = _messageList;

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
    public void getMessage(String userEmail, String chatterId) {
        DatabaseReference messageRef = firebaseDatabase.getReference("messages");
        DatabaseReference accountRef = firebaseDatabase.getReference("accounts");

        accountRef.orderByChild("email").equalTo(userEmail).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    Account account = snapshot.getValue(Account.class);

                    if (account != null) {
                        Query receiverQuery = messageRef.orderByChild("receiverId").equalTo(account.getAccountId());
                        Query senderQuery = messageRef.orderByChild("senderId").equalTo(account.getAccountId());

                        List<Message> combinedMessages = new ArrayList<>();
                        List<Message> receiverMessages = new ArrayList<>();
                        List<Message> senderMessages = new ArrayList<>();

                        ValueEventListener receiverListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                receiverMessages.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Message message = snapshot.getValue(Message.class);
                                    if (message != null && message.getSenderId().equals(chatterId)) {
                                        receiverMessages.add(message);
                                    }
                                }
                                combineAndSetMessages(receiverMessages, senderMessages, combinedMessages);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        };

                        ValueEventListener senderListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                senderMessages.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Message message = snapshot.getValue(Message.class);
                                    if (message != null && message.getReceiverId().equals(chatterId)) {
                                        senderMessages.add(message);
                                    }
                                }
                                combineAndSetMessages(receiverMessages, senderMessages, combinedMessages);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        };

                        receiverQuery.addValueEventListener(receiverListener);
                        senderQuery.addValueEventListener(senderListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void combineAndSetMessages(List<Message> receiverMessages, List<Message> senderMessages, List<Message> combinedMessages) {
        combinedMessages.clear();
        combinedMessages.addAll(receiverMessages);
        combinedMessages.addAll(senderMessages);

        List<Message> sortedMessages = sortAndHandleMessages(combinedMessages);
        _messageList.setValue(sortedMessages);
    }

    private List<Message> sortAndHandleMessages(List<Message> messages) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                try {
                    Date date1 = dateFormat.parse(m1.getTimeStamp());
                    Date date2 = dateFormat.parse(m2.getTimeStamp());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        for (Message message : messages) {
            System.out.println(message.getTimeStamp() + ": " + message.getMessage());
        }
        return messages;
    }

    public String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public void addMessage(String userid, String message){
        DatabaseReference messageRef = firebaseDatabase.getReference("messages");
        DatabaseReference accountRef = firebaseDatabase.getReference("accounts");
        accountRef.orderByChild("role").equalTo("admin").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot adminSnapshot) {
                if (adminSnapshot.exists()) {
                    DataSnapshot adminData = adminSnapshot.getChildren().iterator().next();
                    Account adminAccount = adminData.getValue(Account.class);

                    if (adminAccount != null) {
                        String adminId = adminAccount.getAccountId();
                        String messageId = UUID.randomUUID().toString();
                        Message newMessage = new Message(messageId, adminId, userid, message, getCurrentDateTime());

                        String messageNumber = messageRef.push().getKey();
                        if (messageNumber != null) {
                            messageRef.child(messageNumber).setValue(newMessage).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("AddMessage", "Message added successfully");
                                } else {
                                    Log.e("AddMessage", "Failed to add message: " + task.getException().getMessage());
                                }
                            });
                        } else {
                            Log.e("AddMessage", "Failed to generate message key");
                        }
                    } else {
                        Log.e("AddMessage", "Admin account not found");
                    }
                } else {
                    Log.e("AddMessage", "No admin accounts found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AddMessage", "Error retrieving admin account: " + databaseError.getMessage());
            }
        });
    }
}
