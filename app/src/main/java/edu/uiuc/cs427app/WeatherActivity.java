package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.*;
import android.graphics.Color;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;

import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class WeatherActivity extends BaseActivity {
    private String ENDPOINT = "https://api.open-meteo.com/v1/forecast?latitude=_lat_&longitude=_log_&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m&timezone=auto";

    private TextView tv_city_name, tv_datetime, tv_temp, tv_humidity, tv_weather, tv_wind;

    private ViewGroup loading;

    private RequestQueue requestQueue;
    private Map<Integer, String> weatherMap;
    private boolean f=false;
    private boolean c=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWeatherCodeMap();
        User user = getUser();
        if(user != null) {
            String tempUnit = user.getTemperature_format();
            f = tempUnit.equals("Fahrenheit");
            c = tempUnit.equals("Celsius");
        }


        String cityName = getIntent().getStringExtra("cityName");
        String lat = getIntent().getStringExtra("lat");
        String log = getIntent().getStringExtra("log");
        ENDPOINT = ENDPOINT.replaceAll("_lat_", lat + "").replaceAll("_log_", log + "");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        fetchPosts();

        tv_city_name =  findViewById(R.id.city_name);
        tv_datetime = findViewById(R.id.city_current_datetime);
        tv_temp = findViewById(R.id.city_temperature);
        tv_humidity = findViewById(R.id.city_humidity);
        tv_weather = findViewById(R.id.city_weather);
        tv_wind = findViewById(R.id.city_wind);
        loading = findViewById(R.id.loading);
        tv_city_name.setText(cityName);
    }
    // get user id in database
    private int getUserId(){
        return SharedPrefUtils.getIntData(this, "userid");
    }
    // get user object in database, with signature: id.
    private User getUser() {
        return AppDatabase.getAppDatabase(this).userDao().findById(getUserId());
    }
    /*
    WMO Weather interpretation codes (WW)
     */
    private  void getWeatherCodeMap(){
        weatherMap = new HashMap<>();
        // Populate the map
        weatherMap.put(0, "Clear sky");
        weatherMap.put(1, "Mainly clear");
        weatherMap.put(2, "Partly cloudy");
        weatherMap.put(3, "Overcast");
        weatherMap.put(45, "Fog and depositing rime fog");
        weatherMap.put(48, "Fog and depositing rime fog");
        weatherMap.put(51, "Drizzle: Light intensity");
        weatherMap.put(53, "Drizzle: Moderate intensity");
        weatherMap.put(55, "Drizzle: Dense intensity");
        weatherMap.put(56, "Freezing Drizzle: Light intensity");
        weatherMap.put(57, "Freezing Drizzle: Dense intensity");
        weatherMap.put(61, "Rain: Slight intensity");
        weatherMap.put(63, "Rain: Moderate intensity");
        weatherMap.put(65, "Rain: Heavy intensity");
        weatherMap.put(66, "Freezing Rain: Light intensity");
        weatherMap.put(67, "Freezing Rain: Heavy intensity");
        weatherMap.put(71, "Snow fall: Slight intensity");
        weatherMap.put(73, "Snow fall: Moderate intensity");
        weatherMap.put(75, "Snow fall: Heavy intensity");
        weatherMap.put(77, "Snow grains");
        weatherMap.put(80, "Rain showers: Slight intensity");
        weatherMap.put(81, "Rain showers: Moderate intensity");
        weatherMap.put(82, "Rain showers: Violent intensity");
        weatherMap.put(85, "Snow showers slight");
        weatherMap.put(86, "Snow showers heavy");
        weatherMap.put(95, "Thunderstorm: Slight");
        weatherMap.put(96, "Thunderstorm with slight hail");
        weatherMap.put(99, "Thunderstorm with heavy hail");
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject current = obj.getJSONObject("current");
                Log.i("PostActivity", obj.getJSONObject("current").getString("temperature_2m"));
                tv_datetime.setText(current.getString("time").replace('T',' ')); //replaces all occurrences of 'T' to space
                tv_weather.setText(weatherMap.get(current.getInt("weather_code")));

                // show user customized temperature unit
                if(f){
                    tv_temp.setText(Integer.toString((current.getInt("temperature_2m")* 9/5) + 32)+"°F");
                } else if (c) {
                    tv_temp.setText(current.getString("temperature_2m")+"°C");
                }
                tv_wind.setText("Wind Direction: "+current.getString("wind_direction_10m")+"°"+
                        "\n"+"Wind Speed: "+current.getString("wind_speed_10m")+"km/h");
                tv_humidity.setText(current.getString("relative_humidity_2m")+"%");

            } catch (JSONException e) {
                //some exception handler code.
                Log.e("PostActivity", e.toString());
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity here", error.toString());
            tv_datetime.setText("Data is not available right now. Please try again later!");
            tv_datetime.setTextColor(Color.parseColor("#FF0000"));

        }
    };

}
