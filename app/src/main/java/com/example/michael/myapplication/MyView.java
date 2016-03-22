package com.example.michael.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;
import java.util.jar.Attributes;

/**
 * Created by Michael on 16/3/22.
 */
public class MyView extends View {
    Bitmap bufferBitmap;
    Canvas bufferCanvas;
    Point screenSize;
    Random rand = new Random();
    SensorManager sm;
    int step = 0;
    float[] gravity = new float[3];
    float[] linear_acceleration = new float[3];
    double[] average = new double[3];
    double[] history = new double[3];
    double[] markPointHistory = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int max = 0;
    boolean drawSwitch = true;

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            float[] values = event.values;
            if(sensorType == Sensor.TYPE_ACCELEROMETER){
                final float alpha = 0.8f;

                // 用低通滤波器分离出重力加速度
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                // 用高通滤波器剔除重力干扰
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];


//                double sh = Math.sqrt(Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2));
                double sh = Math.sqrt(Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2) + Math.pow(linear_acceleration[2], 2));
                average[2] = average[1];
                average[1] = average[0];
                average[0] = sh;
                double result = (average[0] + average[1] + average[2] ) /3;
                android.util.Log.d("gzw", "sh=" + screenSize.y /2 + (int)result);
                history[2] = history[1];
                history[1] = history[0];
                history[0] = result;
                step += 10;
                if (step > screenSize.x) {
                    step = 0;
                    MyView.this.clear();
                }
                if (history[1] > history[2]) {
                    if (history[0] - history[1] <  0) {
                        recordMarkPoint((int)result);
                        drawOnBufferWithRed(step,screenSize.y /2 + (int)result * 5);
                    } else {
                        drawOnBuffer(step,screenSize.y /2 + (int)result * 5);
                    }
                } else {
                    if (history[0] - history[1] > 0) {
                        recordMarkPoint((int)result);
                        drawOnBufferWithRed(step,screenSize.y /2 + (int)result * 5);
                    } else {
                        drawOnBuffer(step,screenSize.y /2 + (int)result * 5);
                    }
                }
                caculate();
                if (drawSwitch) {
                    max = 0;
                    MyView.this.invalidate();
                }
                android.util.Log.d("gzw", "sh=" + screenSize.y / 2 + (int) result * 5 + " x=" + step);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private void recordMarkPoint(int result) {
        for (int i = markPointHistory.length - 1; i >= 0;i--) {
            if (i == 0) {
                markPointHistory[0] = result;
            } else {
                markPointHistory[i] = markPointHistory[i-1];
            }
        }
    }
    private void caculate() {
        double average = 0;
        for (int i = markPointHistory.length - 1; i >= 0; i--) {
            average += markPointHistory[i];
        }
        average = average / markPointHistory.length;
        double varience = 0;
        for (int i = markPointHistory.length - 1; i >= 0; i--) {
            varience += Math.pow(markPointHistory[i] - average,2);
        }
        varience = Math.sqrt(varience / markPointHistory.length);
        int breakPoint = 0;
        for (int i = markPointHistory.length - 1; i >= 0; i--) {
            if (varience > 5 && Math.abs(markPointHistory[i] - average) > varience) {
                breakPoint ++;
            }
        }

        max = breakPoint > max ? breakPoint: max;
        android.util.Log.e("gzw", "va=" + varience + " avg=" + average + " max=" + max);
    }
    public MyView(Context context) {
        super(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize = new Point(metrics.widthPixels - 20,
                metrics.heightPixels - 20);

        bufferBitmap = Bitmap.createBitmap(this.screenSize.x,this.screenSize.y, Bitmap.Config.ARGB_8888);//创建内存位图
        bufferCanvas = new Canvas(bufferBitmap);//创建绘图画布
    }


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize = new Point(metrics.widthPixels - 20,
                metrics.heightPixels - 20);

        bufferBitmap = Bitmap.createBitmap(this.screenSize.x,this.screenSize.y, Bitmap.Config.ARGB_8888);//创建内存位图
        bufferCanvas = new Canvas(bufferBitmap);//创建绘图画布
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        bufferCanvas.drawColor(Color.WHITE);
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bufferBitmap, 0, 0, new Paint());//在双缓冲中绘图，将自定义缓冲绘制到屏幕上
    }
    public void setDraw(boolean draw) {
        drawSwitch = draw;
    }

    public boolean isDrawSwitch() {
        return drawSwitch;
    }

    public void drawOnBuffer(int x,int y) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        bufferCanvas.drawCircle(x, y, 5, paint);

    }
    public void drawOnBufferWithRed(int x,int y) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        bufferCanvas.drawCircle(x, y, 5, paint);

    }
    public void clear() {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        bufferCanvas.drawColor(Color.WHITE);
    }
}
