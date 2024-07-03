package com.example.hw1.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.hw1.Interface.MoveCallback;

public class MoveDetector {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private int moveCountX = 0;
    private long timeStamp = 0L;
    private MoveCallback moveCallback;



    public MoveDetector(Context context,MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        intEventListener();
    }

    public int getMoveCountX() {
        return moveCountX;
    }

    private void intEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                calculateMove(x);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateMove(float x) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - timeStamp > 500) {
            timeStamp = System.currentTimeMillis();
            if (x > 1.5 || x < -1.5) {
                if (x > 0) {
                    if(moveCallback!=null){
                        moveCallback.moveX(false);
                    }
                } else {
                    if(moveCallback!=null){
                        moveCallback.moveX(true);
                    }
                }


            }

        }

    }


    public void startListening() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void stopListening() {
        sensorManager.unregisterListener(sensorEventListener,sensor);
    }


}
