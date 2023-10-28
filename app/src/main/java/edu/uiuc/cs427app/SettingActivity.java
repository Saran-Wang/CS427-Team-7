package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.LoginHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class SettingActivity extends BaseActivity {
    RadioGroup rg_temperature_standard;
    RadioButton fahrenheit, celsius;

    Switch sw_theme_selector;
    Button btn_edit;


    /*
    * show user customized UI preference after user clicks on the Setting button on home screen
    * allow user to re-select UI feature preference (temperature unit, dark mode)
    * adjust the UI theme, with onClick feature
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sw_theme_selector = findViewById(R.id.Mode);
        rg_temperature_standard = findViewById(R.id.temperature_standard);
        fahrenheit = findViewById(R.id.fahrenheit);
        celsius = findViewById(R.id.celsius);
        btn_edit = findViewById(R.id.edit);

        User user = getUser();
        // display user customized UI feature
        if(user != null) {

            //init rg_temperature_standard, sp_theme_selector by user data
            boolean theme_dark = user.getTheme().equals("Dark");
            String tempUnit = user.getTemperature_format();
            boolean f = tempUnit.equals("Fahrenheit");
            boolean c = tempUnit.equals("Celsius");

            sw_theme_selector.setChecked(theme_dark);

            if(f){
                rg_temperature_standard.check(fahrenheit.getId());
            } else if (c) {
                rg_temperature_standard.check(celsius.getId());
            }

        }

        sw_theme_selector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // switch is ON
                // show light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // switch is OFF
                // show dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        /*
        * once user clicks on the "DONE" button,
        * 1) the database will be updated
        * 2) UI theme feature will be reflect user preference
        */
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String theme = sw_theme_selector.isChecked()? "Dark" : "Light";
                String temperature_standard = ((RadioButton)findViewById(rg_temperature_standard.getCheckedRadioButtonId())).getText().toString();

                //UPDATE User's temperature unit and theme in database
                updateTempUnit(temperature_standard);
                updateTheme(theme);

                // Change UI to user selection
                LoginHelper.configureApplicationSetting(SettingActivity.this, getUser());
                // implement after pressing the button
                SettingActivity.this.onBackPressed();
            }
        });
    }

    // get user id in database
    public int getUserId(){
        return SharedPrefUtils.getIntData(this, "userid");
    }

    // get user object in database, with signature: id.
    public User getUser() {
        return AppDatabase.getAppDatabase(this).userDao().findById(getUserId());
    }

    // update user customized dark theme in database
    public void updateTheme(String theme){
        AppDatabase.getAppDatabase(this).userDao().updateThemeByName(getUserId(), theme);
    }

    // update user customized temperature unit in database
    public void updateTempUnit(String tempUnit){
        AppDatabase.getAppDatabase(this).userDao().updateTempUnitByName(getUserId(), tempUnit);
    }

}
