package cmpt276.sample.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;


public class MainActivity extends AppCompatActivity {

    private RestaurantManager restaurantManager = RestaurantManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public  void readRestaurantData(){
        InputStream is = getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        int i=0;
        String line = "";
        try {
            while (!((line = reader.readLine())!=null)) {
                //Split by ','
                String[] tokens = line.split(",");

                //Read the data
                Restaurant restaurant = new Restaurant();
                restaurant.setTrackingNumber(tokens[0]);
                restaurant.setName(tokens[1]);
                restaurant.setAddress(tokens[2]);
                restaurant.setType(tokens[3]);
                restaurant.setLatitude(Double.parseDouble(tokens[4]));
                restaurant.setLongitude(Double.parseDouble(tokens[5]));
                restaurantManager.add(restaurant);
            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }

}
