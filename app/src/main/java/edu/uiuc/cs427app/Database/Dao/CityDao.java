package edu.uiuc.cs427app.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.uiuc.cs427app.Database.Entity.City;

@Dao
public interface CityDao {

        @Query("SELECT * FROM city")
        List<City> getAll();

        @Query("SELECT * FROM city WHERE cityName = :cityName " + " LIMIT 1")
        City findByName(String cityName);

        @Insert
        void insertAll(City... cities);

}
