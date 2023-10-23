package edu.uiuc.cs427app.Helper;

import android.content.Context;

import edu.uiuc.cs427app.CreateAccountActivity;
import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.User;

public class LoginHelper {
    public static void configureApplicationSetting(Context context, String username, String password) {
        User loginUser =  AppDatabase.getAppDatabase(context).userDao().findByNameAndPassword(username, password) ;
        ThemeHelper.changeTheme(loginUser.getTheme());
        SharedPrefUtils.saveData(context, "userid", loginUser.getId());
        SharedPrefUtils.saveData(context, "username", loginUser.getUsername());
    }


    public static void configureApplicationSetting(Context context, User loginUser) {
        LoginHelper.configureApplicationSetting(context, loginUser.getUsername(), loginUser.getPassword());
    }
}
