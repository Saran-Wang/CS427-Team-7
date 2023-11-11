package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.uiuc.cs427app", appContext.getPackageName());
    }


    public static final String STRING_TO_BE_TYPED = "Espresso";
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void signup_sameActivity() {

        onView(withId(R.id.create)).perform(click());

        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.create)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7-hmyu2")));
    }
    @Test
    public void login_sameActivity() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7-hmyu2")));
    }

}