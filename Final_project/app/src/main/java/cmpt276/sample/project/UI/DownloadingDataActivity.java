package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import cmpt276.sample.project.Model.DataManager;
import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.InspectionManager;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.Model.UpdateManager;
import cmpt276.sample.project.Model.Violation;
import cmpt276.sample.project.R;

public class DownloadingDataActivity extends AppCompatActivity {
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_data);

        DataManager.init(this);
        dataManager = DataManager.getInstance();

        setDownloadingData();
        try {
            Log.i("enter", "abc");
            printRes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDownloadingData() {
        dataManager.readRestaurantURL();
    }

    public static Intent makeIntentForDownloadingData(Context context){
        Intent intent = new Intent(context, DownloadingDataActivity.class);
        return intent;
    }


    // Bring to the main (map activity when enter the app
    private void printRes() throws FileNotFoundException {
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(path, "update_restaurants.csv");
        InputStream is = new FileInputStream(file);


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

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
//                Log.i("AAAAAAA", "this is write" + restaurant);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInspectionDataFromUpdate() throws FileNotFoundException {
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        File file = new File(path, "update_inspections.csv");
        InputStream is = new FileInputStream(file);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        InspectionManager inspectionManager = InspectionManager.getInstance();
        RestaurantManager restaurantManager;

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

}