package com.shoestoreproject.Data_Layer.Application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ShoeStoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
