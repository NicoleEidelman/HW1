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
    private long timeStampY = 0L;
    private long timeStampX = 0L;
    private MoveCallback moveCallback;



    public MoveDetector(Context context,MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//The MoveDetector class registers a sensor listener for the accelerometer.
        this.moveCallback = moveCallback;
        intEventListener();
    }



    private void intEventListener() {
        this.sensorEventListener = new SensorEventListener() { //When the sensor values change, the onSensorChanged method is called.
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateMove(x, y);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateMove(float x,float y) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - timeStampX > 500) {
            timeStampX = System.currentTimeMillis();
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
            if (currentTime - timeStampY > 1000) {
                timeStampY = System.currentTimeMillis();
                if (y > 6.0 || y < -6.0) {
                    if (moveCallback != null) {
                        moveCallback.adjustSpeed(y > 0);
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
