package edu.uiuc.cs427app;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.AlertHelper;

public class CreateAccountActivity extends BaseActivity {
    EditText et_username, et_password, et_confirm_password;
    Spinner sp_theme_selector;
    RadioGroup rg_temperature_standard;

    Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        et_confirm_password = findViewById(R.id.confirm_password);
        sp_theme_selector = findViewById(R.id.theme_spinner);
        btn_create = findViewById(R.id.create);
        rg_temperature_standard = findViewById(R.id.temperature_standard);

        //init sp_theme_selector
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.theme_color, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_theme_selector.setAdapter(adapter);


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String confirm_password = et_confirm_password.getText().toString();
                String theme = sp_theme_selector.getSelectedItemPosition() == 0 ? "Light" : "Dark";
                String temperature_standard = ((RadioButton)findViewById(rg_temperature_standard.getCheckedRadioButtonId())).getText().toString();

                int validate_result = validateRegistrationRules(username, password, confirm_password, theme);

                //validate_result == 0 means rules are validated
                if(validate_result == 0) {

                    //check username duplication
                    if(!isUsernameExistInDB(username)){

                        //insert into database
                        if(insertUserIntoDB(username, password, theme, temperature_standard)){

                            //insert success and redirect to MainActivity
                            CreateAccountActivity.this.startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));

                        } else {
                            //insert database error
                            AlertHelper.displayDialog(CreateAccountActivity.this, "Unable to insert in database");
                        }
                    } else {
                        //Username already exists in database
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Username already exists");
                    }
                } else {
                    //show prompt
                    if(validate_result == 1) {
                        //1 - username is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Username should have at least 6 characters");
                    } else if(validate_result == 2) {
                        //2 - password is not fulfilling the requirement
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password should have at least 6 characters");
                    } else if(validate_result == 3) {
                        //3 - confirm_password does not match with password
                        AlertHelper.displayDialog(CreateAccountActivity.this, "Password doesn't match with the confirmation");
                    }
                }
            }
        });
    }

    public int validateRegistrationRules(String username, String password, String confirm_password, String theme){
        if (username.length() < 6) {
            // 1 - username is not fulfilling the requirement
            return 1;
        }
        if (password.length() < 6) {
            // 2 - password is not fulfilling the requirement
            return 2;
        }
        if (!password.equals(confirm_password)) {
            // 3 - confirm_password does not match with password
            return 3;
        }
        // 0 - rules are validated
        return 0;
    }


    public boolean isUsernameExistInDB(String username) {
        // If a user with the same username exists, user will not be null
        // Return false if the username doesn't exit
        // Return true if the username already exists in the database
        return AppDatabase.getAppDatabase(this).userDao().findByName(username) != null;
//        User user = AppDatabase.getAppDatabase(this).userDao().findByName(username);
//        if (user != null) {
//            return true;
//        };
//        return false;
    }


    public boolean insertUserIntoDB(String username, String password, String theme, String temperature_standard){
        try {
            AppDatabase.getAppDatabase(this).userDao().insertAll(new  User(username, password, theme, temperature_standard));
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
