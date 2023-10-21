package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class BaseActivity extends AppCompatActivity {
    public void onResume(){
        super.onResume();
        String username = SharedPrefUtils.getStringData(this,"username");

        if(username != null && username.length() > 0){
            setTitle(getString(R.string.app_name) + "-" + username);
        } else {
            setTitle(getString(R.string.app_name));
        }
    }

}
