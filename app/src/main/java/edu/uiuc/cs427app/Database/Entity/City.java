package edu.uiuc.cs427app.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "city")
public class City {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name= "cityName")

    private String cityName;
    @ColumnInfo(name= "lat")
    private String lat;
    @ColumnInfo(name= "log")
    private String log;

    public City(String cityName, String lat, String log) {
        this.cityName = cityName;
        this.lat = lat;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
