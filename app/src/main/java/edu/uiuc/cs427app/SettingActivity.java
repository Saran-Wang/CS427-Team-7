package edu.uiuc.cs427app;

import android.os.Bundle;
import android.util.Log;
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
    // Spinner sp_theme_selector;
    Switch sw_theme_selector;
    Button btn_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // sp_theme_selector = findViewById(R.id.theme_spinner);
        sw_theme_selector = findViewById(R.id.Mode);
        rg_temperature_standard = findViewById(R.id.temperature_standard);
        fahrenheit = findViewById(R.id.fahrenheit);
        celsius = findViewById(R.id.celsius);
        btn_edit = findViewById(R.id.edit);

        User user = getUser();
        if(user != null) {
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
            } else {
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String theme = sw_theme_selector.isChecked()? "Dark" : "Light";
                String temperature_standard = ((RadioButton)findViewById(rg_temperature_standard.getCheckedRadioButtonId())).getText().toString();

                //TODO
                //UPDATE User's temperature unit and theme in database
                updateTempUnit(temperature_standard);
                updateTheme(theme);

                // Change UI to user selection
                LoginHelper.configureApplicationSetting(SettingActivity.this, getUser());
                SettingActivity.this.onBackPressed();
            }
        });
    }

    public int getUserId(){
        return SharedPrefUtils.getIntData(this, "userid");
    }
    public User getUser() {
        return AppDatabase.getAppDatabase(this).userDao().findById(getUserId());
    }

    public void updateTheme(String theme){
        AppDatabase.getAppDatabase(this).userDao().updateThemeByName(getUserId(), theme);
    }

    public void updateTempUnit(String tempUnit){
        AppDatabase.getAppDatabase(this).userDao().updateTempUnitByName(getUserId(), tempUnit);
    }

}
