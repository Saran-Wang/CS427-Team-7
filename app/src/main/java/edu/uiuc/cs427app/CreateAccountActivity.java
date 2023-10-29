package edu.uiuc.cs427app;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Database;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.LoginHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

/*
Activity for new users to create a user account.
After the user provides their username and password,
the app performs user credentials validation. 
This includes checking if the username is already in use and 
verifying that the provided username and password meet the app's requirements.
 */
public class CreateAccountActivity extends BaseActivity {
    // Define UI elements
    EditText et_username, et_password, et_confirm_password;

    Switch sw_theme_selector;
    RadioGroup rg_temperature_standard;

    Button btn_create;

    boolean isAppear = false;
    //onResume will be called once the page is shown
    public void onResume(){
        super.onResume();
        isAppear = true;
    }
    //onPause will be called once the page is disappeared
    public void onPause(){
        super.onPause();
        isAppear = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        // Initialize UI elements by finding their corresponding views in the layout
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        et_confirm_password = findViewById(R.id.confirm_password);
        sw_theme_selector = findViewById(R.id.Mode);
        btn_create = findViewById(R.id.create);
        rg_temperature_standard = findViewById(R.id.temperature_standard);

        // Check the initial state of the theme selector switch
        //sw_theme_selector.isChecked();

        // Handle theme selector switch changes
        sw_theme_selector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //avoid to be triggered when it is not necessary
            if(isAppear) {
                if (isChecked) {
                    // Switch is ON
                    // show dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Switch is OFF
                    // show light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        // Handle create button click
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from the UI elements
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_confirm_password.getText().toString();
                // Determine the selected theme based on the switch state
                String theme = sw_theme_selector.isChecked() ? "Dark" : "Light";
                Log.i("theme", "theme " + theme);

                // Determine the selected temperature standard based on the selected radio button
                String temperature_standard = ((RadioButton)findViewById(rg_temperature_standard.getCheckedRadioButtonId())).getText().toString();
                // Validate user input according to registration rules
                int validate_result = validateRegistrationRules(username, password, confirm_password, theme);

                //validate_result == 0 means rules are validated
                if(validate_result == 0) {
                    // Rules are validated, proceed with registration

                    // Check if the username already exists in the database
                    if(!isUsernameExistInDB(username)){

                        // Insert the user's information into the database
                        if(insertUserIntoDB(username, password, theme, temperature_standard)){

                            // Registration is successful, configure application settings and navigate to the main activity
                            LoginHelper.configureApplicationSetting(CreateAccountActivity.this, username, password);
                            CreateAccountActivity.this.startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));

                        } else {
                            // Database insertion error
                            AlertHelper.displayDialog(CreateAccountActivity.this, "Unable to insert in database");
                        }
                    } else {
                        // Username already exists in the database
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Username already exists");
                    }
                } else {
                    // Show prompts based on the validation result
                    if(validate_result == 1) {
                        //1 - username is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Username should have at least 6 characters");
                    } else if(validate_result == 2) {
                        //2 - password is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password should have at least 6 characters");
                    } else if(validate_result == 3) {
                        //3 - password is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password should contain at least one uppercase letter");
                    } else if(validate_result == 4) {
                        //4 - password is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password should contain at least one special character");
                    } else if(validate_result == 5) {
                        //5 - confirm_password does not match with password
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password doesn't match with the confirmation");
                    }
                }
            }
        });
    }

    // Function to validate user registration rules
    public int validateRegistrationRules(String username, String password, String confirm_password, String theme){
        if (username.length() < 3) {
            // 1 - username is not fulfilling the length requirement (We have debate on this requirement)
            // for most of the netids should have more than 3 characters
            return 1;
        }
        if (password.length() < 6) {
            // 2 - password is not fulfilling the length requirement
            return 2;
        }

        if (!password.matches(".*[A-Z].*")) {
            // 3 - password should contain at least one uppercase letter
            return 3;
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            // 4 - password should contain at least one special character
            return 4;
        }

        if (!password.equals(confirm_password)) {
            // 5 - confirm_password does not match with password
            return 5;
        }
        // 0 - rules are validated
        return 0;
    }

    // Function to check if the username already exists in the database
    public boolean isUsernameExistInDB(String username) {
        // If a user with the same username exists, user will not be null
        // Return false if the username doesn't exit
        // Return true if the username already exists in the database
        return AppDatabase.getAppDatabase(this).userDao().findByName(username) != null;
    }

    // Function to insert the user's information into the database
    public boolean insertUserIntoDB(String username, String password, String theme, String temperature_standard){
        try {
            AppDatabase.getAppDatabase(this).userDao().insertAll(new  User(username, password, theme, temperature_standard));
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //Clear the dark theme if the user set it before
    //it would override the "back button" of the bottom three buttons
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    //Clear the dark theme if the user set it before
    //it would override the "back button" of the menu bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
