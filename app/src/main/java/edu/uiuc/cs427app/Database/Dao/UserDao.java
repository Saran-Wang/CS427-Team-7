package edu.uiuc.cs427app.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uiuc.cs427app.Database.Entity.User;


@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE id = :id " + " LIMIT 1")
    User findById(int id);

    @Query("SELECT * FROM user WHERE username LIKE :username " + " LIMIT 1")
    User findByName(String username);

    @Query("SELECT * FROM user WHERE username LIKE :username AND " + "password LIKE :password LIMIT 1")
    User findByNameAndPassword(String username, String password);

    /* Updating theme mode and temperature unit */
    @Query("UPDATE user SET theme =:theme WHERE id =:id")
    void updateThemeByName(int id, String theme);

    @Query("UPDATE user SET temperature_format =:temperature_standard WHERE id =:id")
    void updateTempUnitByName(int id, String temperature_standard);

    /* Get theme mode and temperature unit */
    @Query("SELECT theme FROM user WHERE id =:id")
    String getThemeById(int id);

    @Query("SELECT temperature_format FROM user WHERE id =:id")
    String getTempUnitById(int id);


    @Update
    void updateUsers(User... users);


    @Insert
    void insertAll(User... users);


    @Delete
    void delete(User user);
}
