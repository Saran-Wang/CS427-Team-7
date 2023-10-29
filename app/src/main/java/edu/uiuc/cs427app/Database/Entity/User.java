package edu.uiuc.cs427app.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name= "username")
    private String username;
    @ColumnInfo(name= "password")
    private String password;
    @ColumnInfo(name= "theme")
    private String theme;
    @ColumnInfo(name= "temperature_format")
    private String temperature_format;

    //constructor of user object
    //with parameter - password, theme and temperature format
    public User (String username, String password, String theme, String temperature_format) {
        this.username = username;
        this.password = password;
        this.theme = theme;
        this.temperature_format = temperature_format;
    }


    //id getter
    public int getId() {
        return id;
    }

    //id setter
    public void setId(int id) {
        this.id = id;
    }

    //username getter
    public String getUsername() {
        return username;
    }

    //username setter
    public void setUsername(String username) {
        this.username = username;
    }

    //password getter
    public String getPassword() {
        return password;
    }

    //password setter
    public void setPassword(String password) {
        this.password = password;
    }

    //theme getter
    public String getTheme() {
        return theme;
    }

    //theme setter
    public void setTheme(String theme) {
        this.theme = theme;
    }

    //temperature_format getter
    public String getTemperature_format() {
        return temperature_format;
    }

    //temperature_format setter
    public void setTemperature_format(String temperature_format) {
        this.temperature_format = temperature_format;
    }
}