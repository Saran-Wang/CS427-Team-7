package edu.uiuc.cs427app.Database;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.uiuc.cs427app.AddCityActivity;
import edu.uiuc.cs427app.Database.Dao.CityDao;
import edu.uiuc.cs427app.Database.Dao.SavedCityDao;
import edu.uiuc.cs427app.Database.Dao.UserDao;
import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.SavedCity;
import edu.uiuc.cs427app.Database.Entity.User;

@Database(entities = {User.class, City.class, SavedCity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    private static boolean loadOnce = true;

    public abstract UserDao userDao();
    public abstract CityDao cityDao();
    public abstract SavedCityDao savedCityDao();

    //initialize database instance if needed and preload a city list from csv
    public static AppDatabase getAppDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, "Weather.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        if(loadOnce) {
            loadOnce = false;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    int size = sInstance.cityDao().getAll().size();
                    if (size == 0) {
                        try {
                            AssetManager manager = ((Activity) context).getAssets();
                            //https://simplemaps.com/data/world-cities
                            InputStream in = manager.open("worldcities.csv");

                            ArrayList<City> cooked = parse(in);
                            for (City c : cooked) {
                                sInstance.cityDao().insertAll(c);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }


        return sInstance;
    }

    //parse input stream of csv data into arraylist of city
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