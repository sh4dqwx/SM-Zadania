package com.example.zad7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationActivity extends AppCompatActivity {
    private final static int REQUEST_LOCATION_PERMISSION = 1;
    private final static String TAG = "LocationActivity";
    private Location lastLocation;
    private TextView locationText;
    private TextView addressText;
    private FusedLocationProviderClient fusedLocationClient;
    private Button downloadAddress;
    private Button downloadLocation;

    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if(location != null) {
                    lastLocation = location;
                    locationText.setText(getString(
                            R.string.location_text,
                            lastLocation.getLatitude(),
                            lastLocation.getLongitude(),
                            lastLocation.getTime()
                    ));
                } else {
                    locationText.setText(R.string.no_location);
                }
            });
        }
    }

    private String locationGeocoding(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );
        } catch(IOException exception) {
            resultMessage = context.getString(R.string.geocoder_not_available);
            Log.e(TAG, resultMessage, exception);
        }

        if(addresses == null || addresses.isEmpty()) {
            if(resultMessage.isEmpty()) {
                resultMessage = context.getString(R.string.no_address_found);
                Log.e(TAG, resultMessage);
            }
        } else {
            Address address = addresses.get(0);
            List<String> addressParts = new ArrayList<>();

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressParts.add(address.getAddressLine(i));
            }
            resultMessage = TextUtils.join("\n", addressParts);
        }

        return resultMessage;
    }

    private void executeGeocoding() {
        if(lastLocation != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> returnedAddress = executor.submit(() -> locationGeocoding(getApplicationContext(), lastLocation));
            try {
                String result = returnedAddress.get();
                addressText.setText(getString(R.string.address_text, result, System.currentTimeMillis()));
            } catch(ExecutionException | InterruptedException exception) {
                Log.e(TAG, exception.getMessage(), exception);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationText = findViewById(R.id.location_text);
        addressText = findViewById(R.id.address_text);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        downloadAddress = findViewById(R.id.download_address);
        downloadAddress.setOnClickListener(v -> executeGeocoding());

        downloadLocation = findViewById(R.id.download_location);
        downloadLocation.setOnClickListener(v -> getLocation());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
}