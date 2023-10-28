package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;
import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.LoginHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;
import edu.uiuc.cs427app.Helper.ThemeHelper;

public class LoginActivity extends BaseActivity {
    EditText et_username, et_password;
    TextView tv_create;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        tv_create = findViewById(R.id.create);
        btn_login = findViewById(R.id.submit);


        /*
        The onClickListener of Button, btn_login
        will confirm the user input field (e.g. username and password)
        whether it has a validated username and/or password or not.

        If validated credential was filled in, the user will be directed to the MainActivity.
        If not, a prompt message will be shown.
         */
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(isUsernameExistInDB(username)){
                    User loginUser = validateCredential(username, password);

                    if(loginUser != null) {
                        LoginHelper.configureApplicationSetting(LoginActivity.this, loginUser);
                        ThemeHelper.changeTheme("Light");
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        //Wrong Password
                        AlertHelper.displayDialog(LoginActivity.this, "Wrong Password");
                    }
                } else {
                    //User does not exists
                    AlertHelper.displayDialog(LoginActivity.this, "User does not exists");
                }
            }
        });


        /*
        The onClickListener of textview, tv_create
        will redirect the user to the CreateAccountActivity
         */
        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
            }
        });
    }

    /*
    The isUsernameExistInDB method is used to confirm
    whether the parameter exists in the database or not.
     */
    public boolean isUsernameExistInDB(String username) {
        return AppDatabase.getAppDatabase(this).userDao().findByName(username) != null;
    }

    /*
    The validateCredential method is used to confirm
    whether the parameter username and password exists in the database or not.
     */
    public User validateCredential(String username, String password) {
        return AppDatabase.getAppDatabase(this).userDao().findByNameAndPassword(username, password) ;
    }
}
