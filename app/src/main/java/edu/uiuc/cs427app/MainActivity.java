package edu.uiuc.cs427app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.SavedCity;
import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;
import edu.uiuc.cs427app.Helper.ThemeHelper;
import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
Activity for users to view their saved cities, log out, add new cities, and delete existing cities. 
*/
public class MainActivity extends BaseActivity {

    ImageView iv_setting, iv_logout;
    TextView greeting;
    Button btn_buttonAddLocation;
    RecyclerView rv_city_list;
    
    // Setting the adapter in onResume to update data after adding a city successfully in AddCityActivity
    public void onResume() {
        super.onResume();
        rv_city_list.setAdapter(new CustomAdapter(SharedPrefUtils.getIntData(this, "userid")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize UI elements
        iv_setting = findViewById(R.id.setting);
        iv_logout = findViewById(R.id.logout);
        greeting = findViewById(R.id.greeting);
        btn_buttonAddLocation = findViewById(R.id.buttonAddLocation);
        rv_city_list = findViewById(R.id.city_list);
        rv_city_list.setLayoutManager(new LinearLayoutManager(this));
        
        // Callback for the "Setting" button
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct from MainActivity to SettingActivity
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        // Callback for the "Logout" button
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout
                //Reset application data and Direct from MainActivity to SettingActivity
                MainActivity.this.setTitle(getString(R.string.app_name) + "-" + SharedPrefUtils.getStringData(MainActivity.this, "username"));
                SharedPrefUtils.saveData(MainActivity.this, "userid", -1);
                SharedPrefUtils.saveData(MainActivity.this, "username", "");
                ThemeHelper.changeTheme("Light");
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        
        // Callback for the "Add Location" button
        btn_buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct from MainActivity to AddCityActivity
                MainActivity.this.startActivity(new Intent(MainActivity.this, AddCityActivity.class));
            }
        });
    }
    
    // CustomAdapter class for handling the RecyclerView
    public class CustomAdapter extends RecyclerView.Adapter<MainActivity.CustomAdapter.ViewHolder> {
        List<City> localDataSet = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView city_name;
            private Button btn_delete;

            public ViewHolder(View view) {
                super(view);
                city_name = (TextView) view.findViewById(R.id.city_name);
                btn_delete = view.findViewById(R.id.delete);
            }

            public TextView getTextView() {
                return city_name;
            }
        }
        
        // Constructor for the CustomAdapter
        public CustomAdapter(int userid) {
            localDataSet = AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().loadCityByUserId(userid);
        }

        @NonNull
        @Override
        public MainActivity.CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_view_citylist_row, parent, false);
            return new ViewHolder(view);
        }
        
        /* DeleteCity method
        this method is used to delete a SavedCity via userid and cityid and then load the updated dataset once delete executes successfully
        */
        @Override
        public void onBindViewHolder(MainActivity.CustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            viewHolder.getTextView().setText(localDataSet.get(position).getCityName().toString());
            //This line of code will set the text of the TextView in the ViewHolder to the city name at the given position in the dataset
            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener()
                    //This line of code will create a button, when clicked the button will execute the code inside the method onClick
            {
                @Override
                public void onClick(View view) {
                    int userid = SharedPrefUtils.getIntData(MainActivity.this, "userid");
                    // this line of code retrieves the user id
                    AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().delete(userid, localDataSet.get(position).getId());
                    // This line of code accesses the database and then deletes the saved city using user id and city id
                    localDataSet = AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().loadCityByUserId(userid);
                    // This line of code updates the localDataSet with the latest city data for the user
                    CustomAdapter.this.notifyDataSetChanged();
                    // This line of code will notify the adapter that the data has changed and to refresh the RecyclerView
                }
            });
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}
