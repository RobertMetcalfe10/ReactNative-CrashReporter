package com.rn_crash_reporter;

import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;

public class FirebaseReporter extends ReactContextBaseJavaModule {

    public FirebaseReporter(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "FirebaseReporter";
    }

    @ReactMethod
    public void reportErrorFirebase(String error, Callback successCallback, Callback errorCallback) {
        try {
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            String time = Instant.now().toString().split("\\.")[0];
            databaseReference.child("Errors").child(time).push().setValue(error);
            successCallback.invoke("Error pushed to Firebase!");
        } catch (Exception e) {
            errorCallback.invoke(e.getMessage());
        }
    }
}
