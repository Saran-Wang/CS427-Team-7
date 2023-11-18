package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import org.json.*;
import android.graphics.Color;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;

import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class WeatherActivity extends BaseActivity {

    // Base URL for the weather API with placeholders for latitude and longitude
    private String ENDPOINT = "https://api.open-meteo.com/v1/forecast?latitude=_lat_&longitude=_log_&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m&timezone=auto";

    // TextViews to display weather information
    private TextView tv_city_name, tv_datetime, tv_temp, tv_humidity, tv_weather, tv_wind;

    // ViewGroups to handle loading, data display, and error scenarios
    private ViewGroup loading, data_panel, error_panel;

    // Button to navigate back in case of an error
    private Button error_back;

    // Volley request queue for API calls
    private RequestQueue requestQueue;

    // Map to store weather codes and their interpretations
    private Map<Integer, String> weatherMap;

    // Temperature format preferences
    private boolean fahrenheit = false;
    private boolean celsius = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout resource
        setContentView(R.layout.activity_weather);

        // Initialize the weather code map
        initWeatherCodeMap();

        // Retrieve user's temperature format preference
        User user = getUser();
        if (user != null) {
            String tempUnit = user.getTemperature_format();

            // Check if the temperature format is Fahrenheit
            fahrenheit = tempUnit.equals("Fahrenheit");

            // Check if the temperature format is Celsius
            celsius = tempUnit.equals("Celsius");
        }

        // Initialize layout elements
        tv_city_name = findViewById(R.id.city_name);
        tv_datetime = findViewById(R.id.city_current_datetime);
        tv_temp = findViewById(R.id.city_temperature);
        tv_humidity = findViewById(R.id.city_humidity);
        tv_weather = findViewById(R.id.city_weather);
        tv_wind = findViewById(R.id.city_wind);
        loading = findViewById(R.id.loading);
        data_panel = findViewById(R.id.data_panel);
        error_panel = findViewById(R.id.error_panel);
        error_back = findViewById(R.id.error_back);

        // Get city name from the intent and set it in the city name TextView
        String cityName = getIntent().getStringExtra("cityName");
        tv_city_name.setText(cityName);

        // Set up API format by replacing latitude and longitude in the endpoint
        String lat = getIntent().getStringExtra("lat");
        String log = getIntent().getStringExtra("log");
        ENDPOINT = ENDPOINT.replaceAll("_lat_", lat + "").replaceAll("_log_", log + "");

        // Show loading bar
        loading.setVisibility(View.VISIBLE);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Perform initial API request to fetch data
        fetchData();
    }

    // Helper method to get user id in the database
    private int getUserId(){
        return SharedPrefUtils.getIntData(this, "userid");
    }

    // Helper method to get user object in the database
    private User getUser() {
        return AppDatabase.getAppDatabase(this).userDao().findById(getUserId());
    }

    /*
    WMO Weather interpretation codes (WW)
     */
    private  void initWeatherCodeMap(){
        weatherMap = new HashMap<>();
        // Populate the map with weather codes and their interpretations
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

    // Open-meteo - HTTP call via request
    private void fetchData() {
        // Create a string request for API call
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onDataLoaded, onDataError);
        // Add the request to the Volley queue
        requestQueue.add(request);
    }

    // Open-meteo - API retrieval success callback
    // This callback updates the UI with weather data
    private final Response.Listener<String> onDataLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                // Convert the response string to a JSONObject
                JSONObject obj = new JSONObject(response);

                // Extract the "current" JSON object from the response
                JSONObject current = obj.getJSONObject("current");

                // Set the date and time in the appropriate TextView
                tv_datetime.setText(current.getString("time").replace('T',' '));

                // Set the weather description in the corresponding TextView using the weather code
                tv_weather.setText(weatherMap.get(current.getInt("weather_code")));

                // Show user-customized temperature unit
                if (fahrenheit) {
                    // Convert temperature to Fahrenheit and set it in the temperature TextView
                    tv_temp.setText(Integer.toString((current.getInt("temperature_2m") * 9/5) + 32) + "°F");
                } else if (celsius) {
                    // Set temperature in Celsius in the temperature TextView
                    tv_temp.setText(current.getString("temperature_2m") + "°C");
                }

                // Set wind information in the wind TextView
                tv_wind.setText("Direction: " + degreeToTextureDescription(Double.parseDouble(current.getString("wind_direction_10m"))) +
                        " " + (current.getString("wind_direction_10m")) + "°" +
                        "\n" + "Speed: " + current.getString("wind_speed_10m") + "km/h");

                // Set humidity information in the humidity TextView
                tv_humidity.setText(current.getString("relative_humidity_2m") + "%");

            } catch (JSONException e) {
                // Handle JSON parsing exceptions
                Log.e("PostActivity", e.toString());

                // Display an alert dialog with the exception message
                AlertHelper.displayDialog(WeatherActivity.this, e.toString());
            }

            // Hide the loading view as data has been loaded
            loading.setVisibility(View.GONE);
        }
    };

    // Open-meteo - API retrieval failure callback
    // This callback notifies the user that the API is not currently available
    private final Response.ErrorListener onDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Log the error message for debugging purposes
            Log.e("PostActivity here", error.toString());

            // Hide the data panel and show the error panel
            data_panel.setVisibility(View.GONE);
            error_panel.setVisibility(View.VISIBLE);

            // Set a click listener on the error back button to handle the back press
            error_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the back press when the error back button is clicked
                    WeatherActivity.this.onBackPressed();
                }
            });
        }
    };

    // Convert wind direction angle to textual description
    public String degreeToTextureDescription(double degree) {
        // Reference: https://stackoverflow.com/questions/36475255/i-have-wind-direction-data-coming-from-openweathermap-api-and-the-data-is-repre

        if (degree > 337.5) {
            return "Northerly";
        }
        if (degree > 292.5) {
            return "North Westerly";
        }
        if (degree > 247.5) {
            return "Westerly";
        }
        if (degree > 202.5) {
            return "South Westerly";
        }
        if (degree > 157.5) {
            return "Southerly";
        }
        if (degree > 122.5) {
            return "South Easterly";
        }
        if (degree > 67.5) {
            return "Easterly";
        }
        if (degree > 22.5) {
            return "North Easterly";
        }

        return "Northerly";
    }
}
