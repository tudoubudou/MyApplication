package com.example.michael.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.michael.myapplication.utils.ToastUtils;


/**
 * Created by Michael on 15/7/16.
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = MyReceiver.class.getSimpleName();
    final static String ACTION = "com.ali.mytest";
    final static String ACTION_INCOMINGCALL = "android.intent.action.PHONE_STATE";

    private static boolean incomingFlag = false;

    private static boolean talking = false;


    private Handler handler = new Handler();

    private static String incoming_number = null;
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION)) {
            Toast.makeText(context,"get intent in Receiver!",Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(Intent.ACTION_MAIN);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setClass(context, IncomingCallActivity.class);
            context.startActivity(intent1);
        } else if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:"+phoneNumber);
            ToastUtils.showShortToast(context,  "call OUT:"+ phoneNumber);
        } else if (action.equals(ACTION_INCOMINGCALL)){
            Log.e(TAG,"action=" + action);
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;// 标识当前是来电
                    incoming_number = intent.getStringExtra("incoming_number");
                    Log.e(TAG, "call in ringing :" + incoming_number);
                    ToastUtils.showShortToast(context, "call in ringing :" + incoming_number);
                    if (!talking) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent();
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.setClass(context, IncomingCallActivity.class);
                                context.startActivity(intent1);
                            }
                        }, 500);
                    }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (incomingFlag) {
                        talking = true;
                        Log.e(TAG, "call in offhook :" + incoming_number);
                        ToastUtils.showShortToast(context, "call in offhook :" + incoming_number);
                    }
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "call in idle :" + incoming_number);
                    ToastUtils.showShortToast(context, "call in idle :" + incoming_number);
                    incomingFlag = false;
                    talking = false;
                    break;
            }
        }
    }

}
