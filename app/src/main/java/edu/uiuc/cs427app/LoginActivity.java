package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.Helper.AlertHelper;

public class LoginActivity extends AppCompatActivity {
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
                    if(validateCredential(username, password)) {
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
        //TODO
        return true;
    }


    public boolean validateCredential(String username, String password) {
        //TODO
        return true;
    }
}
