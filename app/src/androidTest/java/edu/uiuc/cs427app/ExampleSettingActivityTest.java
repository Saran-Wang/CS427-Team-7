package edu.uiuc.cs427app;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;

import static org.hamcrest.CoreMatchers.isA;

import android.view.View;
import android.widget.Checkable;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleSettingActivityTest {
    @Rule
    public ActivityScenarioRule<SettingActivity> activityRule = new ActivityScenarioRule<>(SettingActivity.class);
    @Test
    public void testDarkModeSwitch() {
        // Find the Switch by its ID
        Espresso.onView(ViewMatchers.withId(R.id.setting_mode))
                // Perform a check on the Switch
                .perform(setChecked(true));

        Espresso.onView(ViewMatchers.withId(R.id.edit))
                // Perform a click on DONE
                .perform(click());

        // You can add assertions or further actions based on the state of the Switch
        // For example, to check if the Switch is checked:
        // Espresso.onView(ViewMatchers.withId(R.id.setting_mode))
        //        .check(matches(isNotChecked())).perform(setChecked(true)).check(matches(isChecked()));
        //Espresso.onView(ViewMatchers.withId(R.id.setting_mode))
        //       .check(ViewAssertions.matches(ViewMatchers.isChecked()));
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



    @Test
    public void testTemperatureUnit() {
        // Find the RadioButtons by their IDs
        Espresso.onView(ViewMatchers.withId(R.id.fahrenheit))
                // Perform a click on the first RadioButton
                .perform(click());

        // You can add assertions or further actions based on the state of the RadioButtons
        // For example, to check if the first RadioButton is checked:
        Espresso.onView(ViewMatchers.withId(R.id.fahrenheit))
                .check(matches(isChecked()));

        // Similarly, you can perform actions on other RadioButtons in the RadioGroup
        Espresso.onView(ViewMatchers.withId(R.id.celsius))
                .perform(click());

        // Check the state of the second RadioButton
        Espresso.onView(ViewMatchers.withId(R.id.celsius))
                .check(matches(isChecked()));
    }

}
