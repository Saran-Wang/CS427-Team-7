package edu.uiuc.cs427app.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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

    @Insert
    void insertAll(User... users);


    @Delete
    void delete(User user);
}
