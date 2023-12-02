package edu.uiuc.cs427app;

import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleLocationTest {
    Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapActivity.class).putExtra("cityName", "New York");
    @Rule
    public ActivityScenarioRule<MapActivity> activityRule = new ActivityScenarioRule<>(intent);

    @Test
    public void testZoomInAndOut_Chicago() {
        // Create a Intent to initiate City Name
        // https://developer.android.com/training/testing/espresso/intents
        // Intent resultData = new Intent();
        // Launch the activity with the explicit intent
        String cityName = "Chicago";
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapActivity.class).putExtra("cityName", cityName);

        activityRule.getScenario().onActivity(activity -> {
            activity.startActivity(intent);
        });

        // Check the Intent is passed successfully
        Espresso.onView(ViewMatchers.withId(R.id.map_city)).check(matches(ViewMatchers.withText(cityName)));

        // Wait for the map to load (You might need to customize this wait time)
        Espresso.onView(ViewMatchers.withId(R.id.map));


        // Zoom in
        // Replace R.id.zoomInButton with the actual ID of your zoom in button
        Espresso.onView(ViewMatchers.withId(R.id.zoomInButton))
                .perform(ViewActions.click());

        // Wait for the map to finish zooming
        Espresso.onView(ViewMatchers.withId(R.id.map));

        // Zoom out
        // Replace R.id.zoomOutButton with the actual ID of your zoom out button
        Espresso.onView(ViewMatchers.withId(R.id.zoomOutButton))
                .perform(ViewActions.click());

        // Wait for the map to finish zooming
        Espresso.onView(ViewMatchers.withId(R.id.map));

        // Additional assertions or verifications can be added here if needed
    }

    @Test
    public void testZoomInAndOut_NewYork() {
        // Launch the activity with the explicit intent
        // https://developer.android.com/training/testing/espresso/intents
        String cityName = "New York";

        // Check the Intent is passed successfully
        Espresso.onView(ViewMatchers.withId(R.id.map_city)).check(matches(ViewMatchers.withText(cityName)));

        // Wait for the map to load (You might need to customize this wait time)
        Espresso.onView(ViewMatchers.withId(R.id.map));


        // Zoom in
        // Replace R.id.zoomInButton with the actual ID of your zoom in button
        Espresso.onView(ViewMatchers.withId(R.id.zoomInButton))
                .perform(ViewActions.click());

        // Wait for the map to finish zooming
        Espresso.onView(ViewMatchers.withId(R.id.map));

        // Zoom out
        // Replace R.id.zoomOutButton with the actual ID of your zoom out button
        Espresso.onView(ViewMatchers.withId(R.id.zoomOutButton))
                .perform(ViewActions.click());

        // Wait for the map to finish zooming
        Espresso.onView(ViewMatchers.withId(R.id.map));

        // Additional assertions or verifications can be added here if needed
    }
}
