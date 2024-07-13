package com.shoestoreproject.Data_Layer.Application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ShoeStoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase and set persistence enabled before any other usage
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
