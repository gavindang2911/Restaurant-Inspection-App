package cmpt276.sample.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import com.google.android.material.appbar.AppBarLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.InspectionManager;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.Model.Violation;

public class SingleRestaurant extends AppCompatActivity {
    private static final String RESTAURANT_POSITION = "Position";
    private int positionRestaurant;
    InspectionManager inspectionManager = InspectionManager.getInstance();
    private Restaurant restaurant = null;
    private static RestaurantManager restaurantMan = RestaurantManager.getInstance();

    public static Intent makeIntentForSingleRestaurant(Context context, int restaurantPosition) {
        Intent intent = new Intent(context, SingleRestaurant.class);
        intent.putExtra(RESTAURANT_POSITION, restaurantPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        Toolbar toolbar = findViewById(R.id.toolbar_single_restaurant);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        extractDataFromIntent(this.getIntent());
        displayRestaurantInfo();

        displayRecyclerViewInspection();
    }

    // https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
    private void displayRecyclerViewInspection() {
        RecyclerView  recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Single_Restaurant_inspection);
        InspectionAdapter adapter = new InspectionAdapter(positionRestaurant);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener(new InspectionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int inspectionPosition) {
//                Intent intent = SingleInspection.makeLaunchIntent(
//                        InspectionActivity.this, restaurantPosition, inspectionPosition
//                );
//                startActivity(intent);
//            }
//        });

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
        positionRestaurant = intent.getIntExtra(RESTAURANT_POSITION, -1);

        restaurantMan = RestaurantManager.getInstance();

        restaurant = restaurantMan.getRestaurant(positionRestaurant);
    }
}
