package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;


import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.City;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap mMap;
    Button zoomInButton;
    Button zoomOutButton;
    TextView map_city;
    TextView map_longitude;
    TextView map_latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the city name from Intent callback
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("cityName");
        City c = AppDatabase.getAppDatabase(MapActivity.this).cityDao().findByName(cityName);

        // Initialize activity_map.xml element
        map_city = findViewById(R.id.map_city);
        map_longitude = findViewById(R.id.map_longitude);
        map_latitude = findViewById(R.id.map_latitude);
        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);

        // Set text display of city name, longitude and latitude
        map_city.setText(cityName);
        map_latitude.setText("Latitude: " + c.getLat());
        map_longitude.setText("Longitude: " + c.getLog());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Set onClickListener zoom in and zoom out
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                }
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.zoomOut());
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Get city name
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("cityName");
        City c = AppDatabase.getAppDatabase(MapActivity.this).cityDao().findByName(cityName);

        // New Latlng object with latitude and longitude
        LatLng sydney = new LatLng(Double.parseDouble(c.getLat()), Double.parseDouble(c.getLog()));

        // Add a marker in city and move the camera
        // Set zoom in parameter at 15.0f
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(cityName)
                .snippet(String.format("%s with latitude %s and longitude %s", cityName, c.getLat(), c.getLog())));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }

}



