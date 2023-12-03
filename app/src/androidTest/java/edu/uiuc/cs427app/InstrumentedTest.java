package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;


import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Checkable;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.User;

import android.view.View;
import androidx.test.espresso.UiController;

import static androidx.test.espresso.action.ViewActions.click;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void A_user_signup() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("Weather.db");

        onView(withId(R.id.create)).perform(click());

        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.create)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7-hmyu2")));

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        assertThat (appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!"), is(not(nullValue())));

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void B_user_login() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7-hmyu2")));
    }
    @Test
    public void B_test_setting_DarkModeSwitch() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        // Navigate to the setting page
        onView(withId(R.id.setting)).perform(click());

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        String prevTheme = appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!").getTheme();
        // Find the Switch by its ID
        onView(withId(R.id.setting_mode))
                // Perform a check on the Switch
                .perform(setChecked(true));
        onView(withId(R.id.edit))
                // Perform a click on DONE
                .perform(click());
        // Check whether the new theme (after switch) is different from the previous theme stored in database
        assertThat (appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!").getTheme(), is(prevTheme.equals("Dark")? "Light": "Dark"));


        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void B_test_setting_TemperatureUnit() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        // Navigate to the setting page
        onView(withId(R.id.setting)).perform(click());


        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        // Find the RadioButtons by their IDs
        onView(withId(R.id.fahrenheit))
                // Perform a click on the first RadioButton
                .perform(click());
        onView(withId(R.id.edit))
                // Perform a click on DONE
                .perform(click());
        // Navigate to the setting page
        onView(withId(R.id.setting)).perform(click());
        assertThat (appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!").getTemperature_format(), is( "Fahrenheit"));

        // Similarly, you can perform actions on other RadioButtons in the RadioGroup
        onView(withId(R.id.celsius))
                .perform(click());
        onView(withId(R.id.edit))
                // Perform a click on DONE
                .perform(click());
        // Check whether the new temperature unit (after switch) is different from the previous one stored in database
        assertThat (appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!").getTemperature_format(), is("Celsius"));


        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void C_adding_a_city() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.buttonAddLocation)).perform(click());

        onView(withId(R.id.user_input)).perform(typeText("Chicago"), closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());

        Espresso.pressBack();
        Espresso.pressBack();

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        User user = appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!");
        City city = appDatabase.cityDao().findByName("Chicago");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat (appDatabase.savedCityDao().isCityExistByUserId(user.getId(), city.getId()), is(not(nullValue())));

        onView(ViewMatchers.withId(R.id.city_list))
                .perform((ViewAction) RecyclerViewActions.scrollTo(
                        hasDescendant(withText("Chicago"))
                ));
    }

    @Test
    public void D_removing_an_existing_city(){
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.city_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnViewChild(R.id.delete)));

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        User user = appDatabase.userDao().findByNameAndPassword("hmyu2", "847B2m8c!");
        City city = appDatabase.cityDao().findByName("Chicago");

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat (appDatabase.savedCityDao().isCityExistByUserId(user.getId(), city.getId()), is((nullValue())));
    }
    private String ENDPOINT = "https://api.open-meteo.com/v1/forecast?latitude=_lat_&longitude=_log_&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m&timezone=auto";

    @Test
    public void F_test_weather_feature() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.city_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnViewChild(R.id.weather_button)));

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
            City city = appDatabase.cityDao().findByName("Tokyo");
            String lat = city.getLat();
            String log = city.getLog();

            ENDPOINT = ENDPOINT.replaceAll("_lat_", lat + "").replaceAll("_log_", log + "");


            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(appContext);

            StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onDataLoaded, onDataError);
            requestQueue.add(request);

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            onView(withId(R.id.city_current_datetime)).check(matches((withText(datetime))));
            onView(withId(R.id.city_temperature)).check(matches((withText(temp))));
            onView(withId(R.id.city_humidity)).check(matches((withText(humidity))));
            onView(withId(R.id.city_weather)).check(matches((withText(weather))));

        } catch (Exception e) {
        }

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void F1_test_weather_feature() {

        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());


        onView(withId(R.id.buttonAddLocation)).perform(click());
        onView(withId(R.id.user_input)).perform(typeText("Tokyo"), closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());

        Espresso.pressBack();

        onView(withId(R.id.user_input)).perform(typeText("Hong Kong"), closeSoftKeyboard());
        onView(withId(R.id.add)).perform(click());

        Espresso.pressBack();
        Espresso.pressBack();
        onView(withId(R.id.city_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickOnViewChild(R.id.weather_button)));

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
            City city = appDatabase.cityDao().findByName("Hong Kong");
            String lat = city.getLat();
            String log = city.getLog();

            ENDPOINT = ENDPOINT.replaceAll("_lat_", lat + "").replaceAll("_log_", log + "");


            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(appContext);

            StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onDataLoaded, onDataError);
            requestQueue.add(request);

            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            onView(withId(R.id.city_current_datetime)).check(matches((withText(datetime))));
            onView(withId(R.id.city_temperature)).check(matches((withText(temp))));
            onView(withId(R.id.city_humidity)).check(matches((withText(humidity))));
            onView(withId(R.id.city_weather)).check(matches((withText(weather))));

        } catch (Exception e) {
        }


        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String datetime, weather, wind, humidity, temp;

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
                Log.i("current", "current2 " + current);

                // Set the date and time in the appropriate TextView
                datetime = (current.getString("time").replace('T',' '));

                initWeatherCodeMap();
                // Set the weather description in the corresponding TextView using the weather code
                weather = (weatherMap.get(current.getInt("weather_code")));

                // Show user-customized temperature unit
                temp = (Integer.toString((current.getInt("temperature_2m") * 9/5) + 32) + "°F");

                // Set wind information in the wind TextView
                wind = ("Direction: " + degreeToTextureDescription(Double.parseDouble(current.getString("wind_direction_10m"))) +
                        " " + (current.getString("wind_direction_10m")) + "°" +
                        "\n" + "Speed: " + current.getString("wind_speed_10m") + "km/h");

                // Set humidity information in the humidity TextView
                humidity = (current.getString("relative_humidity_2m") + "%");

            } catch (JSONException e) {
                // Handle JSON parsing exceptions
                Log.e("error", e.toString());
            }
        }
    };

    // Open-meteo - API retrieval failure callback
    // This callback notifies the user that the API is not currently available
    private final Response.ErrorListener onDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error here", error.toString());
        }
    };

    @Test
    public void G_test_location_feature() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.city_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickOnViewChild(R.id.map_button)));

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        City city = appDatabase.cityDao().findByName("Tokyo");

        try {
            onView(withId(R.id.map_city)).check(matches((withText(containsString("" + city.getCityName())))));
            onView(withId(R.id.map_latitude)).check(matches((withText(containsString("" + city.getLat())))));
            onView(withId(R.id.map_longitude)).check(matches((withText(containsString("" + city.getLog())))));
        } catch (Exception e) {
        }
    }

    @Test
    public void G1_test_location_feature() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.city_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickOnViewChild(R.id.map_button)));

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase appDatabase = AppDatabase.getAppDatabase(appContext);
        City city = appDatabase.cityDao().findByName("Hong Kong");

        try {
            onView(withId(R.id.map_latitude)).check(matches((withText(containsString("" + city.getLat())))));
            onView(withId(R.id.map_longitude)).check(matches((withText(containsString("" + city.getLog())))));
        } catch (Exception e) {
        }
    }


    @Test
    public void E_user_logout() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.logout)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7")));
    }

    public ViewAction clickOnViewChild(final int viewId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                click().perform(uiController, view.findViewById(viewId));
            }
        };
    }

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }

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

    private Map<Integer, String> weatherMap;
    private void initWeatherCodeMap(){
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
//https://stackoverflow.com/questions/29924564/using-espresso-to-unit-test-google-maps
}