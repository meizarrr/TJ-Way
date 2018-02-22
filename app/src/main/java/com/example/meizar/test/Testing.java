package com.example.meizar.test;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by meizar on 09/02/18.
 */

public class Testing extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
