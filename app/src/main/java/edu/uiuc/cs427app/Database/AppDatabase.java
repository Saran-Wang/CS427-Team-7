package edu.uiuc.cs427app.Database;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.uiuc.cs427app.Database.Dao.CityDao;
import edu.uiuc.cs427app.Database.Dao.UserDao;
import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.User;

@Database(entities = {User.class, City.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public abstract UserDao userDao();
    public abstract CityDao cityDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, "Weather.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        //TODO
        if(sInstance.userDao().getAll() == null || sInstance.userDao().getAll().size() == 0){
            User user = new User("admin", "password", "Dark", "Celsius");
            sInstance.userDao().insertAll(user);
        }

        if(sInstance.cityDao().getAll() == null || sInstance.cityDao().getAll().size() == 0){
            try {
                AssetManager manager = ((Activity)context).getAssets();
                InputStream in = manager.open("worldcities.csv");
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<City> cooked = parse(in);
                            for(City c : cooked) {
                                sInstance.cityDao().insertAll(c);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("city", "city" + sInstance.cityDao().getAll().size());


        return sInstance;
    }


    private static ArrayList<City> parse(InputStream in) throws IOException {
        ArrayList<City> results = new ArrayList<City>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int line_count = 0;
        String nextLine = null;
        while ((nextLine = reader.readLine()) != null) {
            String[] tokens = nextLine.split(",");
            City current = new City(tokens[0].replace("\"", ""),tokens[2].replace("\"", ""),tokens[3].replace("\"", ""));
            if(line_count != 0) {
                results.add(current);
            }
            line_count++;
        }

        in.close();
        return results;
    }
}