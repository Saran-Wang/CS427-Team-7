package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import edu.uiuc.cs427app.Helper.SharedPrefUtils;

/*
This class is meant to be a base activity for other activities within the application.
It provides common functionality that should be executed in the onResume() method of derived activities.
*/
public class BaseActivity extends AppCompatActivity {
    public void onResume(){
        super.onResume(); // Call the onResume method of the parent class

        // Get the username from the application's shared preferences.
        String username = SharedPrefUtils.getStringData(this,"username");

        // Check if a username is available in shared preferences.
        if(username != null && username.length() > 0){
            // If a username exists and is not empty, set the activity's title to include the username.
            setTitle(getString(R.string.app_name) + "-" + username);
        } else {
            // If no username is found or it's empty, set the activity's title to the default application name.
            setTitle(getString(R.string.app_name));
        }
    }

}
