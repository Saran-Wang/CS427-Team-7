package edu.uiuc.cs427app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class SettingActivity extends AppCompatActivity {
    RadioGroup rg_temperature_standard;
    Spinner sp_theme_selector;
    Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rg_temperature_standard = findViewById(R.id.temperature_standard);
        sp_theme_selector = findViewById(R.id.theme_spinner);
        btn_edit = findViewById(R.id.edit);

        //init sp_theme_selector
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_color, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_theme_selector.setAdapter(adapter);

        User user = getUserById(SharedPrefUtils.getIntData(this, "userid"));
        if(user != null) {
            //TODO
            //init rg_temperature_standard, sp_theme_selector by user data
        }
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //UPDATE User in database
                SettingActivity.this.onBackPressed();
            }
        });
    }

    public User getUserById(int id) {
        //TODO
        return null;
    }


}
