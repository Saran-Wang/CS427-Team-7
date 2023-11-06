package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherActivity extends BaseActivity {
    private String ENDPOINT = "https://api.open-meteo.com/v1/forecast?latitude=_lat_&longitude=_log_&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m&timezone=auto";

    private TextView tv_city_name, tv_datetime, tv_temp, tv_humidity, tv_weather, tv_wind;

    private ViewGroup loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String cityName = getIntent().getStringExtra("city_name");
        String lat = getIntent().getStringExtra("lat");
        String log = getIntent().getStringExtra("log");
        ENDPOINT = ENDPOINT.replaceAll("_lat_", lat + "").replaceAll("_log_", log + "");

        tv_city_name = findViewById(R.id.city_name);
        tv_datetime = findViewById(R.id.city_current_datetime);
        tv_temp = findViewById(R.id.city_temperature);
        tv_humidity = findViewById(R.id.city_humidity);
        tv_weather = findViewById(R.id.city_weather);
        tv_wind = findViewById(R.id.city_wind);
        loading = findViewById(R.id.loading);

    }

}
