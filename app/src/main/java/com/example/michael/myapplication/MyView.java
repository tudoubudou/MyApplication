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
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            float[] values = event.values;
            if(sensorType == Sensor.TYPE_ACCELEROMETER){
                double sh = Math.sqrt(Math.pow(values[0], 2) + Math.pow(values[1], 2) + Math.pow(values[2], 2));
                android.util.Log.d("gzw", "sh=" + screenSize.y /2 + (int)sh);
                step += 10;
                if (step > screenSize.x) {
                    step = 0;
                    MyView.this.clear();
                }
                drawOnBuffer(step,screenSize.y /2 + (int)sh * 10);
                MyView.this.invalidate();
                android.util.Log.d("gzw", "sh=" + screenSize.y / 2 + (int) sh * 10 + " x=" + step);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
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
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bufferBitmap, 0, 0, new Paint());//在双缓冲中绘图，将自定义缓冲绘制到屏幕上
    }

    public void drawOnBuffer(int x,int y) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        bufferCanvas.drawCircle(x, y, 5, paint);

//        for(int n=0;n<500;n++){                     //随机在绘图画布上绘制500个圆
//            int r=rand.nextInt(256);
//            int g=rand.nextInt(256);
//            int b=rand.nextInt(256);
//            paint.setColor(Color.rgb(r, g, b));
//            int x=rand.nextInt(bufferCanvas.getWidth());
//            int y=rand.nextInt(bufferCanvas.getHeight());
//            int radius=rand.nextInt(100)+20;
//            bufferCanvas.drawCircle(x, y, radius, paint);
//        }
    }
    public void clear() {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        bufferCanvas.drawColor(Color.WHITE);
    }
}
