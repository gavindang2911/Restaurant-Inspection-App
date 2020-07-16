
package cmpt276.sample.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


import cmpt276.sample.project.MapsActivity;
import cmpt276.sample.project.Model.Date;
import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.InspectionManager;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.Model.Violation;
import cmpt276.sample.project.R;
/**
 * MainActivity class is the first screen of the app, which displays the list
 * of all restaurants to the screen after reading data from CSV files
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */
public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_CALCULATE = 103;

    private RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private List<Restaurant> restaurantList = new ArrayList<>();
    private InspectionManager inspectionManager = InspectionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readRestaurantData();
        sortRestaurants();
        readInspectionData();
        restaurantListView();
        setUpMap();
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
                restaurant.setTrackingNumber(tokens[0].replace("\"", ""));
                restaurant.setName(tokens[1].replace("\"", ""));
                restaurant.setAddress(tokens[2].replace("\"", ""));
                restaurant.setCity(tokens[3].replace("\"", ""));
                restaurant.setType(tokens[4].replace("\"", ""));
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = SingleRestaurant.makeIntentForSingleRestaurant(MainActivity.this, position);
                startActivityForResult(intent, ACTIVITY_RESULT_CALCULATE);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Restaurant>{

        public MyListAdapter(){
            super(MainActivity.this,R.layout.item_view,restaurantList);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
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
            imageView.setImageResource(currentRestaurant.getIcon());


            //set Name
            TextView nameText = (TextView) itemView.findViewById(R.id.restaurantName);
            nameText.setText(currentRestaurant.getName());

            //set Address
            TextView addressText = (TextView) itemView.findViewById(R.id.restaurantAddress);
            addressText.setText(currentRestaurant.getAddress());



            //set icon of hazard level and date of inspection
            ImageView imageIcon = (ImageView) itemView.findViewById(R.id.hazardLevelIcon);
            TextView hazardLevelText = (TextView) itemView.findViewById(R.id.hazardLevelTextView);
            TextView lastDateOfInspection = (TextView) itemView.findViewById(R.id.lastDateInspectionTextView);
            TextView numberOfIssues = (TextView) itemView.findViewById(R.id.numberOfIssuesTextView);
            if(currentRestaurant.getInspections().size()!=0) {
                //set number of issues
                int numberOfIssuesFound = currentRestaurant.getInspections().get(0).getNumOfCritical() + currentRestaurant.getInspections().get(0).getNumOfNonCritical();
                numberOfIssues.setText(numberOfIssuesFound+" issues found");

                hazardLevelText.setText(currentRestaurant.getInspections().get(0).getHazardRating());
                long date = Date.dayFromCurrent(currentRestaurant.getInspections().get(0).getInspectionDate());
                if(date<=30){
                    lastDateOfInspection.setText("latest inspection: "+String.format(Locale.ENGLISH,"%d days ago",date));
                }
                else if(date<365){
                    lastDateOfInspection.setText("latest inspection: "+Date.DAY_MONTH.getDateString(currentRestaurant.getInspections().get(0).getInspectionDate()));
                }
                else{
                    lastDateOfInspection.setText("latest inspection: "+Date.DAY_MONTH_YEAR.getDateString(currentRestaurant.getInspections().get(0).getInspectionDate()));
                }
                if (currentRestaurant.getInspections().get(0).getHazardRating().equals("Low")) {
                    imageIcon.setImageResource(R.drawable.green_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#459E48"));
                } else if (currentRestaurant.getInspections().get(0).getHazardRating().equals("Moderate")) {
                    imageIcon.setImageResource(R.drawable.orange_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#FF6722"));
                } else {
                    imageIcon.setImageResource(R.drawable.red_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#C6170B"));
                }
            }
            else {
                hazardLevelText.setText("Unknown");
                imageIcon.setImageResource(R.drawable.gray_circle);
                lastDateOfInspection.setText("No Inspections");
                numberOfIssues.setText("0 issues found");
            }



            return itemView;
        }
    }

    public void sortRestaurants(){
        List<Restaurant> restaurantList = restaurantManager.getRestaurantList();
        Collections.sort(restaurantList,new Comparator(){
            public int compare(Object restaurantOne, Object restaurantTwo){
                return ((Restaurant)restaurantOne).getName().compareTo(((Restaurant)restaurantTwo).getName());
            }
        });
        restaurantManager.setRestaurantList(restaurantList);
    }


    private void readInspectionData() {
        InputStream inputStream = getResources().openRawResource(R.raw.inspectionreports_itr1);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );
        String csvLine = "";
        try {
            reader.readLine();
            while ((csvLine = reader.readLine()) != null)
            {
                String[] data = csvLine.split("[,|]");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("^\"|\"$", "");
                }

                List<Violation> violations = getViolation(data);

                Inspection inspection = new Inspection(
                        data[0],
                        Integer.parseInt(data[1]),
                        data[2],
                        Integer.parseInt(data[3]),
                        Integer.parseInt(data[4]),
                        data[5],
                        violations
                );

                inspectionManager.add(inspection);

                restaurantManager = RestaurantManager.getInstance();
                for (Restaurant res : restaurantManager) {
                    if (res.getTrackingNumber().equals(inspection.getTrackingNumber())) {
                        res.addInspection(inspection);
                    }
                }
            }
        }
        catch (IOException e) {
            Log.wtf("InspectionManager", "Error reading file on line " + csvLine, e);
            e.printStackTrace();
        }
    }

    private List<Violation> getViolation(String[] data) {
        List<Violation> result = new ArrayList<>();
        final int START = 6;
        final int COMPONENT_PER_VIOLATION = 4;

        for (int i = START; i < data.length; i += COMPONENT_PER_VIOLATION) {
            int violationNum = 0;
            try {
                violationNum = Integer.parseInt(data[i]);
            } catch (Exception e) {
                Log.wtf("InspectionManager",
                        "Error when converting string " + data[i] + " to int");
                e.getStackTrace();
            }

            result.add(new Violation(
                    violationNum,
                    data[i + 1],
                    data[i + 2],
                    data[i + 3]
            ));
        }

        return result;
    }

    private void setUpMap(){
        Button button = (Button) findViewById(R.id.goToMapButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }

}

