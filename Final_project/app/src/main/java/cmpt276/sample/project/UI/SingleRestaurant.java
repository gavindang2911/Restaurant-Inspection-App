
package cmpt276.sample.project.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/*
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import com.google.android.material.appbar.AppBarLayout;


 */

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import cmpt276.sample.project.Adapter.InspectionAdapter;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.R;


/**
 * SingleRestaurant class is the second screen of the app, which displays the detail
 * of the specific restaurant when user click on from first screen. It also shows
 * the list of inspections of the restaurant
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */
public class SingleRestaurant extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_CALCULATE = 103;
    private static final String RESTAURANT = "Restaurant";

    private String restaurantIDString;
    private int positionRestaurant;
    private Restaurant restaurant;
    private RestaurantManager restaurantMan;



    public static Intent makeIntentForSingleRestaurant(Context context, String restaurantID) {
        Intent intent = new Intent(context, SingleRestaurant.class);
        intent.putExtra(RESTAURANT, restaurantID);
        return intent;
    }

    //https://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar#:~:text
    // =There%20are%20two%20simple%20steps,setDisplayHomeAsUpEnabled(true)%3B
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extractDataFromIntent(this.getIntent());
        displayRestaurantInfo();

        displayRecyclerViewInspection();

        setUpFavouriteBtn();
        setUpGPS();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMap() {
        Intent i = new Intent();
        i.putExtra("restaurantID", restaurant.getTrackingNumber());
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    // https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
    private void displayRecyclerViewInspection() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Single_Restaurant_inspection);
        InspectionAdapter adapter = new InspectionAdapter(positionRestaurant);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new InspectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positionInspection) {
                Intent intent = SingleInspection.makeIntentForSingleInspection(
                        SingleRestaurant.this, positionInspection, positionRestaurant
                );
                startActivityForResult(intent, ACTIVITY_RESULT_CALCULATE);
            }
        });

    }


    private void displayRestaurantInfo() {
        TextView textRestaurantName = (TextView) findViewById(R.id.textView_Single_Restaurant_Name);
        TextView textRestaurantAddress = (TextView) findViewById(R.id.textView_Single_Restaurant_Address);
        TextView textRestaurantLatitude = (TextView) findViewById(R.id.textView_Single_Restaurant_latitude);
        TextView textRestaurantLongitude = (TextView) findViewById(R.id.textView_Single_Restaurant_longitude);

        textRestaurantName.setText(restaurant.getName());
        textRestaurantAddress.setText(restaurant.getAddress());
        textRestaurantLatitude.setText("" + restaurant.getLatitude());
        textRestaurantLongitude.setText("" + restaurant.getLongitude());

    }


    private void extractDataFromIntent(Intent intent)
    {
        restaurantIDString = intent.getStringExtra(RESTAURANT);
        restaurantMan = RestaurantManager.getInstance();
        int count = 0;
        for (Restaurant temp : restaurantMan) {
            if (temp.getTrackingNumber().equals(restaurantIDString)) {
                restaurant = temp;
                positionRestaurant = count;
            }
            count++;
        }
    }

    private void setUpGPS(){
        TextView text1 = (TextView) findViewById(R.id.textView_Single_Restaurant_longitude);
        TextView text2 = (TextView) findViewById(R.id.textView_Single_Restaurant_latitude);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });
    }



    private void setUpFavouriteBtn() {
        final Button btn_favourite = findViewById(R.id.btn_favourite);
        if (restaurant.isFavourite()) {
            btn_favourite.setBackgroundResource(R.drawable.star_on);
        } else {
            btn_favourite.setBackgroundResource(R.drawable.star_off);
        }
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.isFavourite()) {
                    restaurant.setFavourite(false);
                    btn_favourite.setBackgroundResource(R.drawable.star_off);
                    removeFromFavourites(restaurant);
                } else {
                    restaurant.setFavourite(true);
                    btn_favourite.setBackgroundResource(R.drawable.star_on);
                    addToFavourites(restaurant);
                }
            }
        });
    }

    private void addToFavourites(Restaurant restaurant) {
        SharedPreferences pref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        Set<String> favourites_list = new HashSet<>(pref.getStringSet("favourites", new HashSet<String>()));
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        favourites_list.add(json);
        editor.putStringSet("favourites", favourites_list).apply();
    }

    private void removeFromFavourites(Restaurant restaurant) {
        SharedPreferences pref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        Set<String> favourites_list = new HashSet<>(pref.getStringSet("favourites", new HashSet<String>()));
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        favourites_list.remove(json);
        editor.putStringSet("favourites", favourites_list).apply();
    }

}

