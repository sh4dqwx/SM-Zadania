package com.example.zad7;

import static com.example.zad7.SensorAdapter.SENSOR_TYPE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorName;
    private TextView sensorValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorName = findViewById(R.id.sensor_details_name);
        sensorValue = findViewById(R.id.sensor_value);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(getIntent().getIntExtra(SENSOR_TYPE, 0));

        if(sensor == null) {
            sensorValue.setText(R.string.missing_sensor);
        } else {
            sensorName.setText(sensor.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        if(sensorType == sensor.getType()) {
            sensorValue.setText(getString(R.string.sensor_value, currentValue));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("onAccuracyChanged called");
    }
}