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

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import edu.uiuc.cs427app.Database.AppDatabase;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.uiuc.cs427app", appContext.getPackageName());
    }


    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void A_signup_sameActivity() {
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
    public void B_login_sameActivity() {
        onView(withId(R.id.username)).perform(typeText("hmyu2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("847B2m8c!"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Team 7-hmyu2")));
    }

}