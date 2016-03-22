package com.example.michael.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ru.mail.R;


public class MainActivity extends AppCompatActivity {
    TextView  textview;
    Button button;
    HandlerThread myThread = new HandlerThread("my");
    Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Intent i = new Intent(this,SecondActivity.class);
//        startActivity(i);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView)findViewById(R.id.text);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String s = textview.getText().toString();
                        Log.e("text", "s=" + s);
//                        textview.setText(s + "\n gzw2 " + Thread.currentThread().getId() + Thread.currentThread().getName());
                    }
                }.start();
                Toast.makeText(MainActivity.this, "in act = " + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
                /*button.animate().translationX(400f).translationY(-400f).alpha(0f).rotation(360f).scaleX(0f).scaleY(0f).setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        button.animate().translationX(0f).translationY(0f).alpha(1f).scaleX(1f).scaleY(1f).rotation(-360f);
                    }
                });*/
                button.animate().alpha(0f).scaleX(10f).scaleY(10f).setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
//                        button.animate().alpha(1rf).scaleX(1f).scaleY(1f);
                        button.setAlpha(1f);
                        button.setScaleX(1f);
                        button.setScaleY(1f);
                    }
                });
            }
        });
        myThread.start();
        myHandler = new Handler(myThread.getLooper());
        Toast.makeText(MainActivity.this,"in act = " + Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        myHandler.post(new Runnable() {
            @Override
            public void run() {
//                textview.setText("gzw " + Thread.currentThread().getId() + Thread.currentThread().getName());
                textview.setText("gzw " + MainActivity.this.getResources().getDisplayMetrics().density);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"onresume",Toast.LENGTH_SHORT).show();
        HashMap<Integer,Integer> hashMap = new HashMap<Integer,Integer>(2);

        for (int i=0; i< 50000; i++) {
            Random random = new Random();
            int result = random.nextInt(2);
            if (hashMap.containsKey(result) ) {
                hashMap.put(result, hashMap.get(result) +1);
            } else {
                hashMap.put(result,1);
            }
        }
        textview.setText(textview.getText() + "\n" + String.valueOf(hashMap.get(0)) + " " + hashMap.get(1));
    }
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"onStart",Toast.LENGTH_SHORT).show();
    }
    //    private native String printJNI();

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//            SharedPreferences sp = this.getApplication().getSharedPreferences("text",this.MODE_MULTI_PROCESS);
//            String st = sp.getString("jieke","badman");
//            textview.setText(st);
            //从系统服务中获得传感器管理器   
            SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            //从传感器管理器中获得全部的传感器列表   
            List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
            //获取某个类型的所有传感器
//            List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

            //显示有多少个传感器   
            textview.setText("经检测该手机有" + allSensors.size() + "个传感器，他们分别是：\n");

            //显示每个传感器的具体信息   
            for (Sensor s : allSensors) {

                String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                        + s.getVendor() + "\n";

                switch (s.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        textview.setText(textview.getText().toString() + s.getType() + " 加速度传感器accelerometer" + tempString);
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        textview.setText(textview.getText().toString() + s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                        break;
                    case Sensor.TYPE_LIGHT:
                        textview.setText(textview.getText().toString() + s.getType() + " 环境光线传感器light" + tempString);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        textview.setText(textview.getText().toString() + s.getType() + " 电磁场传感器magnetic field" + tempString);
                        break;
                    case Sensor.TYPE_ORIENTATION:
                        textview.setText(textview.getText().toString() + s.getType() + " 方向传感器orientation" + tempString);
                        break;
                    case Sensor.TYPE_PRESSURE:
                        textview.setText(textview.getText().toString() + s.getType() + " 压力传感器pressure" + tempString);
                        break;
                    case Sensor.TYPE_PROXIMITY:
                        textview.setText(textview.getText().toString() + s.getType() + " 距离传感器proximity" + tempString);
                        break;
                    case Sensor.TYPE_TEMPERATURE:
                        textview.setText(textview.getText().toString() + s.getType() + " 温度传感器temperature" + tempString);
                        break;
                    default:
                        textview.setText(textview.getText().toString() + s.getType() + " 未知传感器" + tempString);
                        break;
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
