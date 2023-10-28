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

public class MainActivity extends BaseActivity {

    ImageView iv_setting, iv_logout;
    TextView greeting;
    Button btn_buttonAddLocation;
    RecyclerView rv_city_list;

    public void onResume() {
        super.onResume();
        //TODO
        rv_city_list.setAdapter(new CustomAdapter(SharedPrefUtils.getIntData(this, "userid")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_setting = findViewById(R.id.setting);
        iv_logout = findViewById(R.id.logout);
        greeting = findViewById(R.id.greeting);
        btn_buttonAddLocation = findViewById(R.id.buttonAddLocation);
        rv_city_list = findViewById(R.id.city_list);
        rv_city_list.setLayoutManager(new LinearLayoutManager(this));

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct from MainActivity to SettingActivity
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout
                //Reset application data and Direct from MainActivity to SettingActivity
                MainActivity.this.setTitle(getString(R.string.app_name) + "-" + SharedPrefUtils.getStringData(MainActivity.this, "username"));
                SharedPrefUtils.saveData(MainActivity.this, "userid", -1);
                SharedPrefUtils.saveData(MainActivity.this, "username", "");
                ThemeHelper.changeTheme("Light");
                MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btn_buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Direct from MainActivity to AddCityActivity
                MainActivity.this.startActivity(new Intent(MainActivity.this, AddCityActivity.class));
            }
        });
    }


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

        public CustomAdapter(int userid) {
            localDataSet = AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().loadCityByUserId(userid);
        }

        @NonNull
        @Override
        public MainActivity.CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_view_citylist_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainActivity.CustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            viewHolder.getTextView().setText(localDataSet.get(position).getCityName().toString());
            viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userid = SharedPrefUtils.getIntData(MainActivity.this, "userid");
                    SavedCity savedCity = AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().isCityExistByUserId(userid, localDataSet.get(position).getId());
                    AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().delete(userid, localDataSet.get(position).getId());
                    localDataSet = AppDatabase.getAppDatabase(MainActivity.this).savedCityDao().loadCityByUserId(userid);
                    CustomAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}