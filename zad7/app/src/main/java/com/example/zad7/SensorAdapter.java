package com.example.zad7;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {
    public final static String SENSOR_TYPE = "sensor_type";
    private final List<Sensor> sensorList;

    public SensorAdapter(List<Sensor> sensorList) { this.sensorList = sensorList; }
    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SensorViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = sensorList.get(position);
        holder.bind(sensor);
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Sensor sensor;
        private final TextView sensorName;
        private final ImageView sensorIcon;

        public SensorViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnLongClickListener(this);

            sensorName = itemView.findViewById(R.id.sensor_value);
            sensorIcon = itemView.findViewById(R.id.sensor_icon);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorName.setText(sensor.getName());

            if(sensor.getType() == Sensor.TYPE_ACCELEROMETER || sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD || sensor.getType() == Sensor.TYPE_LIGHT) {
                itemView.setOnClickListener(this);
                sensorIcon.setImageResource(R.drawable.ic_sensor_detailed);
            } else {
                sensorIcon.setImageResource(R.drawable.ic_sensor);
            }
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(context, LocationActivity.class);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, SensorDetailsActivity.class);
                intent.putExtra(SENSOR_TYPE, sensor.getType());
                context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Context context = v.getContext();
            new AlertDialog.Builder(context)
                    .setTitle(R.string.sensor_details)
                    .setItems(new String[]{"" +
                            context.getString(R.string.sensor_name, sensor.getName()),
                            context.getString(R.string.sensor_vendor, sensor.getVendor()),
                            context.getString(R.string.sensor_max_range, sensor.getMaximumRange())
                    }, (dialog, which) -> {})
                    .show();
            return true;
        }
    }
}
