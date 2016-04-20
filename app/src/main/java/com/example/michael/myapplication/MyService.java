package com.example.michael.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.example.michael.myapplication.utils.Utils;

/**
 * Created by Michael on 15/7/16.
 */
public class MyService extends Service {

    final static String ACTION = "com.ali.myservice";
    Handler myHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.test();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if (action != null && action.equals(ACTION)) {
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences sp = this.getApplication().getSharedPreferences("text", this.MODE_MULTI_PROCESS);
            String st = sp.getString("jieke", "badman");
//            Toast.makeText(this, "start service!" + st, Toast.LENGTH_SHORT).show();
           /* AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
//            Intent newIntent = new Intent("com.ali.myservice");
            Intent newIntent = new Intent(this,MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, 10000, pIntent);
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, pIntent);*/
            myHandler = new Handler(Looper.myLooper());
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyService.this, "in ser = " + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
