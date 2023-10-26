package edu.uiuc.cs427app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.City;

public class AddCityActivity extends BaseActivity {
    EditText et_user_input;
    RecyclerView rv_city_list;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        et_user_input = findViewById(R.id.user_input);
        rv_city_list = findViewById(R.id.city_list);
        btn_add = findViewById(R.id.delete);

        rv_city_list.setLayoutManager(new LinearLayoutManager(AddCityActivity.this));
        rv_city_list.setAdapter(new CustomAdapter());

        //for filtering the city list by et_user_input changes
        et_user_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //adding foreign relationship to SavedCity (User id and City Id)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                //City city = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().findByName("Tokyo");
                //SavedCity savedCity = new SavedCity(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"), city.getId());
                //AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().insertAll(savedCity);

                //List<City> cities = AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().loadCityByUserId(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"));
                //AlertHelper.displayDialog(AddCityActivity.this, cities.size() + " " +cities.get(0).getCityName());
            }
        });
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        List<City> localDataSet = new ArrayList<>();

        public  class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.textView4);
            }

            public TextView getTextView() {
                return textView;
            }
        }
        public CustomAdapter() {
            localDataSet = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().getAll();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AddCityActivity.this).inflate(R.layout.recycler_view_addcity_row, null, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            viewHolder.getTextView().setText(localDataSet.get(position).getCityName().toString());
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }

}
