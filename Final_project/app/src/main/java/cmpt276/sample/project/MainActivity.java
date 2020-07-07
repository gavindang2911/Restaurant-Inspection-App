package cmpt276.sample.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;

public class MainActivity extends AppCompatActivity {

    private RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private List<Restaurant> restaurantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        readRestaurantData();
        restaurantListView();


    }

    public  void readRestaurantData(){
        InputStream is = getResources().openRawResource(R.raw.restaurants_itr1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        int i=1;
        String image = "image";
        String line = "";
        try {
            //Step over headers
            reader.readLine();

            while ((line = reader.readLine())!=null) {
                //Split by ','
                String[] tokens = line.split(",");

                //Read the data
                Restaurant restaurant = new Restaurant();
                restaurant.setTrackingNumber(tokens[0]);
                restaurant.setName(tokens[1]);
                restaurant.setAddress(tokens[2]);
                restaurant.setCity(tokens[3]);
                restaurant.setType(tokens[4]);
                restaurant.setLatitude(Double.parseDouble(tokens[5]));
                restaurant.setLongitude(Double.parseDouble(tokens[6]));

                String iconName = image + Integer.toString(i++);
                restaurant.setIconName(iconName);


                int id = getResources().getIdentifier(iconName,"drawable",getPackageName());
                restaurant.setIcon(id);
                restaurantManager.add(restaurant);
                restaurantList.add(restaurant);

            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
    }

    private void restaurantListView(){
        ArrayAdapter<Restaurant> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.restaurantListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Restaurant>{

        public MyListAdapter(){
            super(MainActivity.this,R.layout.item_view,restaurantList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view,parent,false);
            }

            Restaurant currentRestaurant = restaurantManager.getRestaurant(position);

            //fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_image);

            int width = imageView.getWidth();
            imageView.setMinimumWidth(width);
            imageView.setMaxWidth(width);

            int height = imageView.getHeight();
            imageView.setMaxHeight(height);
            imageView.setMinimumHeight(height);

            imageView.setImageResource(currentRestaurant.getIcon());

            //set Name
            TextView nameText = (TextView) itemView.findViewById(R.id.restaurantName);
            nameText.setText(currentRestaurant.getName());

            //set Address
            TextView addressText = (TextView) itemView.findViewById(R.id.restaurantAddress);
            addressText.setText(currentRestaurant.getAddress());


            //set .....

            return itemView;
        }
    }



}
