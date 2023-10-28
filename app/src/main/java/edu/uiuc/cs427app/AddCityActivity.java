package edu.uiuc.cs427app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uiuc.cs427app.Database.AppDatabase;
import edu.uiuc.cs427app.Database.Entity.City;
import edu.uiuc.cs427app.Database.Entity.SavedCity;
import edu.uiuc.cs427app.Helper.AlertHelper;
import edu.uiuc.cs427app.Helper.SharedPrefUtils;

public class AddCityActivity extends BaseActivity {
    EditText et_user_input;
    RecyclerView rv_city_list;
    Button btn_add;

    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        et_user_input = findViewById(R.id.user_input);
        rv_city_list = findViewById(R.id.city_list);
        btn_add = findViewById(R.id.add);

        rv_city_list.setLayoutManager(new LinearLayoutManager(AddCityActivity.this));
        customAdapter = new CustomAdapter();
        rv_city_list.setAdapter(customAdapter);

        //for filtering the city list by et_user_input changes
        et_user_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customAdapter.setData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //adding foreign relationship to SavedCity (User id and City Id)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City city = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().findByName(et_user_input.getText().toString());

                if(city != null) {
                    SavedCity isExistCity = AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().isCityExistByUserId(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"), city.getId());

                    if(isExistCity!= null) {
                        // if the city did exist before in the list, we prompt a msg box and clear the input text field and return
                        AlertHelper.displayDialog(AddCityActivity.this, city.getCityName() + " is added already!");
                        et_user_input.getText().clear();
                        return;
                    }


                    // else we add the new city to the list according to user id and city id
                    SavedCity savedCity = new SavedCity(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"), city.getId());
                    AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().insertAll(savedCity);

                    // prompt a msg box to say we added the city
                    AlertHelper.displayDialog(AddCityActivity.this,   "Added " + city.getCityName() + " to the list!");

                    // Changed the behavior here, after the city is added, clear out the input,
                    // So user can continue add more cities
                    et_user_input.getText().clear();
                    //AddCityActivity.this.onBackPressed();
                } else {
                    AlertHelper.displayDialog(AddCityActivity.this, "No city found!");
                }
            }
        });
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        public List<City> localDataSet = new ArrayList<>();

        public  class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.textView);
            }

            public TextView getTextView() {
                return textView;
            }
        }
        public CustomAdapter() {
            localDataSet = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().getAll();
        }

        public void setData(String filterText) {
            localDataSet = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().filterByName(filterText);
            this.notifyDataSetChanged();
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
            ((ViewGroup)viewHolder.getTextView().getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //pressing the list item will update textfield accordingly
                    et_user_input.setText(localDataSet.get(position).getCityName().toString());
                    et_user_input.setSelection(et_user_input.length() - 1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }

}
