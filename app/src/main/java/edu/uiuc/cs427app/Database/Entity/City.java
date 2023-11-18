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

    //constructor of City
    //with parameter - city, latitude and longitude
    public City(String cityName, String lat, String log) {
        this.cityName = cityName;
        this.lat = lat;
        this.log = log;
    }

    //id getter
    public int getId() {
        return id;
    }

    //id setter
    public void setId(int id) {
        this.id = id;
    }

    //cityname getter
    public String getCityName() {
        return cityName;
    }

    //cityname setter
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    //latitude getter
    public String getLat() {
        return lat;
    }

    //latitude setter
    public void setLat(String lat) {
        this.lat = lat;
    }

    //longitude getter
    public String getLog() {
        return log;
    }

    //longitude setter
    public void setLog(String log) {
        this.log = log;
    }
}
