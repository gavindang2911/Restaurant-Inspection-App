
package cmpt276.sample.project.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private Restaurant restaurant = null;
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


}

