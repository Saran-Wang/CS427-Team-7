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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(isUsernameExistInDB(username)){
                    User loginUser = validateCredential(username, password);

                    if(loginUser != null) {
                        ThemeHelper.changeTheme(loginUser.getTheme());
                        SharedPrefUtils.saveData(LoginActivity.this, "userid", loginUser.getId());
                        SharedPrefUtils.saveData(LoginActivity.this, "username", loginUser.getUsername());

                        LoginActivity.this.startActivity(new Intent(LoginActivity.this,MainActivity.class));
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

        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
            }
        });
    }


    public boolean isUsernameExistInDB(String username) {
        return AppDatabase.getAppDatabase(this).userDao().findByName(username) != null;
    }
    public User validateCredential(String username, String password) {
        return AppDatabase.getAppDatabase(this).userDao().findByNameAndPassword(username, password) ;
    }
}
