package edu.uiuc.cs427app.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.uiuc.cs427app.Database.Entity.SavedCity;
import edu.uiuc.cs427app.Database.Entity.User;


@Dao
public interface SavedCityDao {
    @Query("SELECT * FROM saved_city")
    List<SavedCity> getAll();

    @Query("SELECT * FROM saved_city WHERE id IN (:savedCityIds)")
    List<SavedCity> loadAllByIds(int[] savedCityIds);

    //@Query("SELECT * FROM user WHERE username LIKE :username " + " LIMIT 1")
    //User findByName(String username);

    //@Query("SELECT * FROM user WHERE username LIKE :username AND " + "password LIKE :password LIMIT 1")
    //User findByNameAndPassword(String username, String password);

    @Insert
    void insertAll(SavedCity... savedCities);

    @Delete
    void delete(SavedCity savedCity);
}
