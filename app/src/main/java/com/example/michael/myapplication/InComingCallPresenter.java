package com.example.michael.myapplication;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * Created by Michael on 16/4/20.
 */
public class InComingCallPresenter {
    private static final String TAG = InComingCallPresenter.class.getSimpleName();
    private WeakReference<Context> context;

    InComingCallPresenter(Context ctx) {
        context = new WeakReference<Context>(ctx);
    }

    private Context getContext() {
        return context.get();
    }

    public void killCall() {
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            methodGetITelephony.setAccessible(true);

            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) {
            Log.d(TAG, "PhoneStateReceiver **" + ex.toString());
        }
    }

    public void answerCall() {
        try {
            Runtime.getRuntime().exec("input keyevent " +
                    Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

        } catch (IOException e) {
            answerRingingCallWithIntent();
        }
    }

    public void answerRingingCallWithIntent() {
        try {
            Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent1.putExtra("state", 1);
            localIntent1.putExtra("microphone", 1);
            localIntent1.putExtra("name", "Headset");
            getContext().sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");

            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent1);
            getContext().sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");

            Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent2);
            getContext().sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");

            Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent4.putExtra("state", 0);
            localIntent4.putExtra("microphone", 1);
            localIntent4.putExtra("name", "Headset");
            getContext().sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

}
