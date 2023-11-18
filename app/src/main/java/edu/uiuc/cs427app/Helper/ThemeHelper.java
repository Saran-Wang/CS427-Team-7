package edu.uiuc.cs427app.Helper;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    //responsible for application level changing theme
    public static void changeTheme(String theme){
        if(theme.equals("Light")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if(theme.equals("Dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}

