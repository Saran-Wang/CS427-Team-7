package edu.uiuc.cs427app.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_city")
public class SavedCity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name= "user_id")
    private int user_id;
    @ColumnInfo(name= "city_id")
    private int city_id;

    public SavedCity (int user_id, int city_id) {
        this.user_id = user_id;
        this.city_id = city_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
}
