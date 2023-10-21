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

    public User (String username, String password, String theme, String temperature_format) {
        this.username = username;
        this.password = password;
        this.theme = theme;
        this.temperature_format = temperature_format;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTemperature_format() {
        return temperature_format;
    }

    public void setTemperature_format(String temperature_format) {
        this.temperature_format = temperature_format;
    }
}