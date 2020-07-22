
package cmpt276.sample.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


import cmpt276.sample.project.Model.DataManager;
import cmpt276.sample.project.Model.DateUtils;
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
    private static final int ACTIVITY_RESULT_UPDATE = 103;
    private static final int ACTIVITY_RESULT_MAP = 105;

    private RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private List<Restaurant> restaurantList = new ArrayList<>();
    private InspectionManager inspectionManager = InspectionManager.getInstance();
    private DataManager dataManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataManager = DataManager.init(this);
        dataManager = DataManager.getInstance();

        /**
         * To start the app again uncomment this function, REMEMBER TO COMMENT WHEN USE THE APP.
         * CANNOT USE BOTH AT THE SAME TIME
         */
//        clearStorage();

        /**
         * Comment everything below to start the app again
         * CANNOT USE BOTH AT THE SAME TIME
         */
        // --------------------------------------------------------------------------------------------------------
        checkForUpdate();
        try {
            restaurantManager.reset();
            restaurantManager = RestaurantManager.getInstance();
            readRestaurantDataFromServer();
            readInspectionDataFromServer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "update_restaurants.csv");

        if (!file.exists()) {
            readRestaurantData();
            readInspectionData();
        }
        sortRestaurants();
        restaurantListView();
        setUpMap();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // --------------------------------------------------------------------------------------------------------
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
//                Intent intent = SingleRestaurant.makeIntentForSingleRestaurant(MainActivity.this, position);
//                startActivity(intent);

                    String message = restaurantManager.getRestaurantList().get(position).getTrackingNumber();

                    Intent intent = SingleRestaurant.makeIntentForSingleRestaurant(MainActivity.this, message);
                    startActivity(intent);
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

            //set the view
            /*
            Site fr image
            https://commons.wikimedia.org/wiki/File:7-eleven_logo.svg
            https://fontmeme.com/mcdonalds-font/
            https://1000logos.net/starbucks-logo/
            https://www.facebook.com/Freshslice/
            https://www.facebook.com/BlenzCoffee/
            https://logos.fandom.com/wiki/Safeway
            https://www.glassdoor.ca/Benefits/Save-On-Foods-Canada-Benefits-EI_IE316196.0,13_IL.14,20_IN3.htm
            https://expandedramblings.com/index.php/tim-hortons-statistics-facts/
            https://www.pngitem.com/middle/ibibJio_burger-king-logo-in-helvetica-round-brand-logo/
            https://www.amazon.co.uk/KFC-Logo-Bumper-Sticker-12/dp/B00GAZY70S
            https://www.tripadvisor.ca/LocationPhotoDirectLink-g312583-d8495003-i156134269-Domino_s_Pizza_Hatfield-Pretoria_Gauteng.html
            https://getvectorlogo.com/boston-pizza-vector-logo-svg/
            https://www.vippng.com/ps/restaurant-icon/

             */

            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_image);
            if(currentRestaurant.getName().contains("Boston Pizza")){
                imageView.setImageResource(R.drawable.boston_pizza);
            }
            else if(currentRestaurant.getName().contains("7-Eleven")){
                imageView.setImageResource(R.drawable.seven_eleven);
            }
            else if(currentRestaurant.getName().contains("Tim Hortons")) {
                imageView.setImageResource(R.drawable.tim_hortons);
            }
            else if(currentRestaurant.getName().contains("Safeway")){
                imageView.setImageResource(R.drawable.safeway);
            }
            else if(currentRestaurant.getName().contains("Domino's Pizza")) {
                imageView.setImageResource(R.drawable.dominos_pizza);
            }
            else if(currentRestaurant.getName().contains("McDonald's")) {
                imageView.setImageResource(R.drawable.mcdonalds);
            }
            else if(currentRestaurant.getName().contains("KFC")) {
                imageView.setImageResource(R.drawable.kfc);
            }
            else if(currentRestaurant.getName().contains("Starbucks")) {
                imageView.setImageResource(R.drawable.starbucks);
            }
            else if(currentRestaurant.getName().contains("Burger King")) {
                imageView.setImageResource(R.drawable.burger_king);
            }
            else if(currentRestaurant.getName().contains("Blenz Coffee")) {
                imageView.setImageResource(R.drawable.blenz_coffee);
            }
            else if(currentRestaurant.getName().contains("Save On Foods")) {
                imageView.setImageResource(R.drawable.save_on_foods);
            }
            else if(currentRestaurant.getName().contains("Freshslice Pizza")) {
                imageView.setImageResource(R.drawable.fresh_slice_pizza);
            }
            else {
                imageView.setImageResource(R.drawable.restaurant);
            }



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
                long date = DateUtils.dayFromCurrent(currentRestaurant.getInspections().get(0).getInspectionDate());
                if(date<=30){
                    lastDateOfInspection.setText("latest inspection: "+String.format(Locale.ENGLISH,"%d days ago",date));
                }
                else if(date<365){
                    lastDateOfInspection.setText("latest inspection: "+ DateUtils.DAY_MONTH.getDateString(currentRestaurant.getInspections().get(0).getInspectionDate()));
                }
                else{
                    lastDateOfInspection.setText("latest inspection: "+ DateUtils.DAY_MONTH_YEAR.getDateString(currentRestaurant.getInspections().get(0).getInspectionDate()));
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
                startActivityForResult((new Intent(MainActivity.this, MapsActivity.class)), ACTIVITY_RESULT_MAP);
            }
        });
    }

    /**
     * _____________________________________________________________________________________________________________________________________________________________
     */
    private void clearStorage() {
        SharedPreferences settings = this.getSharedPreferences("AppPrefs", 0);
        settings.edit().clear().commit();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update_inspections.csv");
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update_restaurants.csv");

        boolean d = file.delete();
        boolean f = file2.delete();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkForUpdate() {
        if(dataManager.check20hour()) {
            if(dataManager.checkIfUpdateNeeded()) {
                Intent i = UpdateDataActivity.makeIntentForUpdateData(MainActivity.this);
                startActivityForResult(i, ACTIVITY_RESULT_UPDATE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ACTIVITY_RESULT_UPDATE) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if (resultCode == RESULT_CANCELED && requestCode == ACTIVITY_RESULT_UPDATE) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.downloadFailed),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (resultCode == RESULT_OK && requestCode == ACTIVITY_RESULT_MAP) {

            return;
        }
    }

    // Bring to the main (map activity when enter the app
    private boolean readRestaurantDataFromServer() throws FileNotFoundException {
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(path, "update_restaurants.csv");
        InputStream is = new FileInputStream(file);


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        int i = 1;
        String image = "image";
        String line = "";
        try {
            //Step over headers
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                //Split by ','
                line = line.replaceAll(", ", " ");
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

                restaurantManager.add(restaurant);
                restaurantList.add(restaurant);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean readInspectionDataFromServer() throws FileNotFoundException {
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(path, "update_inspections.csv");
        InputStream is = new FileInputStream(file);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        InspectionManager inspectionManager = InspectionManager.getInstance();


        String csvLine = "";

        try {
            reader.readLine();
            while ((csvLine = reader.readLine()) != null)
            {
                String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                String[] tokens = csvLine.split(regex);
                String hazardRating = "";
                if(tokens.length!=0) {
                    List<Violation> result = new ArrayList<>();
                    if (tokens.length == 5) {
                        Inspection inspection = new Inspection(
                                tokens[0],
                                Integer.parseInt(tokens[1]),
                                tokens[2].replaceAll("[^a-zA-Z0-9 &]", ""),
                                Integer.parseInt(tokens[3]),
                                Integer.parseInt(tokens[4]),
                                hazardRating,
                                result
                        );
                        inspectionManager.add(inspection);
                        restaurantManager = RestaurantManager.getInstance();
                        for (Restaurant res : restaurantManager) {
                            if (res.getTrackingNumber().equals(inspection.getTrackingNumber())) {
                                res.addInspection(inspection);
                            }
                        }
                    }
                    else {
                        if (tokens.length == 7) {
                            hazardRating = tokens[6].replaceAll("[^a-zA-Z0-9 &]","");;
                        }
                        Inspection inspection = new Inspection(
                                tokens[0],
                                Integer.parseInt(tokens[1]),
                                tokens[2].replaceAll("[^a-zA-Z0-9 &]", ""),
                                Integer.parseInt(tokens[3]),
                                Integer.parseInt(tokens[4]),
                                hazardRating,
                                result
                        );

                        if (tokens[5].length() > 0) {
                            /**
                             * CHeck if where there is more than 1 violations or not
                             */
                            if (!tokens[5].contains("|")) {
                                String[] violationStringArray = tokens[5].split(",");

                                Violation violation = readViolation(violationStringArray);

                                inspection.addViolation(violation);
                            } else {
                                String[] allViolations = tokens[5].split("[|]");

                                for (int i = 0; i < allViolations.length; i++) {
                                    String[] violationStringArray = allViolations[i].split(",");
                                    Violation violation = readViolation(violationStringArray);
                                    inspection.addViolation(violation);
                                }
                            }
                        }
                        inspectionManager.add(inspection);
                        restaurantManager = RestaurantManager.getInstance();
                        for (Restaurant res : restaurantManager) {
                            if (res.getTrackingNumber().equals(inspection.getTrackingNumber())) {
                                res.addInspection(inspection);
                            }
                        }
                        Log.i("AAAAAAA", "this is writeeeeee" + inspection);

                    }
                }
            }
            return true;
        }
        catch (IOException e) {
            Log.wtf("InspectionManager", "Error reading file on line " + csvLine, e);
            e.printStackTrace();
            return false;
        }
    }


    private Violation readViolation(String[] violationStringArray) {

        int violationNum = Integer.parseInt(violationStringArray[0].replaceAll("[^0-9]", ""));
        String criticalOrNon = violationStringArray[1];
        String description = violationStringArray[2];
        String isRepeat = violationStringArray[3];

        return new Violation(violationNum , criticalOrNon, description, isRepeat);
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

}

