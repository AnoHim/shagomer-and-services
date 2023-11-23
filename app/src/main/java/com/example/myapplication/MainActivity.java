package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private boolean active = true;
    private SensorManager sensorManager;
    private int count = 0;
    private TextView text;
    private long lastUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView2);
        text.setText(String.valueOf(count));
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdate = System.currentTimeMillis();
    }

    public void click(View v) {
        Intent i=new Intent(this, MediaService.class);
        if (v.getId()==R.id.start) {
            startService(i);
        }
        else {
            stopService(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void OnStopped(View view) {
        active=!active;
        if(!active){
            Button button=findViewById(R.id.button);
            button.setText("Возобновить");
        }else{
            Button button=findViewById(R.id.button);
            button.setText("Пауза");
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (active && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelerationSquareRoot = (x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            long actualTime = System.currentTimeMillis();

            if (accelerationSquareRoot >= 2) {
                if (actualTime - lastUpdate < 200) {
                    return;
                }
                lastUpdate = actualTime;

                count++;
                text.setText(String.valueOf(count));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}