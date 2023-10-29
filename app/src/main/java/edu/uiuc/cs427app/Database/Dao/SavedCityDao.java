package edu.uiuc.cs427app.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.SavedCity;
import edu.uiuc.cs427app.Database.Entity.User;


@Dao
public interface SavedCityDao {
    @Query("SELECT * FROM saved_city")
    List<SavedCity> getAll();

    @Query("SELECT * FROM saved_city WHERE id IN (:savedCityIds)")
    List<SavedCity> loadAllByIds(int[] savedCityIds);

    @Query("SELECT * FROM saved_city INNER JOIN city on saved_city.city_id = city.id WHERE user_id = :userId")
    List<City> loadCityByUserId(int userId);

    @Query("SELECT * FROM saved_city INNER JOIN city on saved_city.city_id = :city_id WHERE user_id = :userId LIMIT 1")
    SavedCity isCityExistByUserId(int userId, int city_id);

    @Insert
    void insertAll(SavedCity... savedCities);

    @Delete
    void delete(SavedCity savedCity);

    @Query("DELETE FROM saved_city WHERE city_id = :city_id AND user_id = :userId")
    void delete(int userId, int city_id);

}
