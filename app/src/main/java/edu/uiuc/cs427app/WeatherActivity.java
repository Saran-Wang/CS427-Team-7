package edu.uiuc.cs427app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;
import edu.uiuc.cs427app.Network.Entity.Forecast;

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

        tv_city_name.setText(cityName);
    }

    public void onResume(){
        super.onResume();
        loading.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }
    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("PostActivity", response);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Forecast forecast = (gson.fromJson(response, Forecast.class));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            df.setTimeZone(TimeZone.getTimeZone(forecast.getTimezone()));

            final String timeString = df.format(new Date());

            tv_datetime.setText(timeString);
            if(getUser().getTemperature_format().equals("Celsius")) {
                tv_temp.setText(forecast.getCurrent().getTemperature_2m() + " C");
            } else {
                double fahrenheit = ((forecast.getCurrent().getTemperature_2m()*9)/5)+32;
                tv_temp.setText(fahrenheit + " F");
            }
            tv_humidity.setText(forecast.getCurrent().getRelative_humidity_2m() + " %");
            tv_weather.setText(forecast.getCurrent().getWeatherCode() + "");
            tv_wind.setText("Direction : " + forecast.getCurrent().getWindDirection() + ", Speed : " + forecast.getCurrent().getWind_speed_10m() + " km/h");
            loading.setVisibility(View.GONE);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            loading.setVisibility(View.GONE);
            AlertHelper.displayDialog(WeatherActivity.this, "Network Error! Unable to get weather data!");
        }
    };


    // get user id in database
    public int getUserId(){
        return SharedPrefUtils.getIntData(this, "userid");
    }

    // get user object in database, with signature: id.
    public User getUser() {
        return AppDatabase.getAppDatabase(this).userDao().findById(getUserId());
    }
}
