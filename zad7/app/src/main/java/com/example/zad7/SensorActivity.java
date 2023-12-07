package com.example.zad7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private static final String SHOW_SENSOR_COUNT_VISIBLE = "show_sensor_count_visible";
    private RecyclerView sensorRecyclerView;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private SensorAdapter adapter;
    private boolean showSensorCountVisible;

    private void updateSubtitle() {
        String subtitle = getString(R.string.sensor_count, sensorList.size());
        if(!showSensorCountVisible) subtitle = null;
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            showSensorCountVisible = savedInstanceState.getBoolean(SHOW_SENSOR_COUNT_VISIBLE);

        setContentView(R.layout.activity_sensor);

        sensorRecyclerView = findViewById(R.id.sensor_recycler_view);
        sensorRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if(adapter == null) {
            adapter = new SensorAdapter(sensorList);
            sensorRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.show_sensor_count) {
            showSensorCountVisible = !showSensorCountVisible;
            invalidateOptionsMenu();
            updateSubtitle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_SENSOR_COUNT_VISIBLE, showSensorCountVisible);
    }
}