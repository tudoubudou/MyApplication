package com.example.michael.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Michael on 15/7/16.
 */
public class MyReceiver extends BroadcastReceiver {

    final static String ACTION = "com.ali.mytest";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION)) {
            Toast.makeText(context,"text!",Toast.LENGTH_SHORT).show();
        }
    }
}
