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

/*
Activity for users to add cities to their list of saved cities. 
It provides city suggestions as the user types in the city name and 
handles the addition of selected cities to the user's list. 
The CustomAdapter class is responsible for displaying city suggestions in a RecyclerView.
*/
public class AddCityActivity extends BaseActivity {
    EditText et_user_input;
    RecyclerView rv_city_list;
    Button btn_add;

    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        // Initialize UI elements
        et_user_input = findViewById(R.id.user_input);
        rv_city_list = findViewById(R.id.city_list);
        btn_add = findViewById(R.id.add);
        
        // Set up the RecyclerView for displaying city suggestions
        rv_city_list.setLayoutManager(new LinearLayoutManager(AddCityActivity.this));
        customAdapter = new CustomAdapter();
        rv_city_list.setAdapter(customAdapter);

        // Listen for changes in the user input (EditText) to dynamically filter city suggestions
        et_user_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Update the displayed city suggestions based on the user's input
                customAdapter.setData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Handle adding a selected city to the user's list (User id and City Id)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the city object based on user input
                City city = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().findByName(et_user_input.getText().toString());

                if(city != null) {
                    // Check if the city is already in the user's saved cities list
                    SavedCity isExistCity = AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().isCityExistByUserId(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"), city.getId());

                    if(isExistCity!= null) {
                        //If the city did exist before in the list, we prompt a message box and clear the input text field and return
                        AlertHelper.displayDialog(AddCityActivity.this, city.getCityName() + " is added already!");
                        et_user_input.getText().clear();
                        return;
                    }

                    // else we add the new city to the user's saved cities list
                    SavedCity savedCity = new SavedCity(SharedPrefUtils.getIntData(AddCityActivity.this, "userid"), city.getId());
                    AppDatabase.getAppDatabase(AddCityActivity.this).savedCityDao().insertAll(savedCity);

                    // Prompt a message box to confirm the addition
                    AlertHelper.displayDialog(AddCityActivity.this,   "Added " + city.getCityName() + " to the list!");

                    // Clear the input field for adding more cities
                    et_user_input.getText().clear();
                    //AddCityActivity.this.onBackPressed();
                } else {
                    // Show a message if no matching city is found
                    AlertHelper.displayDialog(AddCityActivity.this, "No city found!");
                }
            }
        });
    }
    
    // CustomAdapter class for displaying city suggestions in a RecyclerView
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

        // Initialize the adapter with all available cities
        public CustomAdapter() {
            localDataSet = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().getAll();
        }
        
        // Set the data in the adapter to filter city suggestions based on user input
        public void setData(String filterText) {
            localDataSet = AppDatabase.getAppDatabase(AddCityActivity.this).cityDao().filterByName(filterText);
            this.notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the view for each city suggestion
            View view = LayoutInflater.from(AddCityActivity.this).inflate(R.layout.recycler_view_addcity_row, null, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
            // Bind data to each view holder (city suggestion)
            viewHolder.getTextView().setText(localDataSet.get(position).getCityName().toString());
            // Listen for item clicks to update the user input
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
