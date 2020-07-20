package cmpt276.sample.project.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import cmpt276.sample.project.Model.Violation;
import cmpt276.sample.project.R;

public class DownloadingDataActivity extends AppCompatActivity {
    private DataManager dataManager;
    Thread download;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_data);

        DataManager.init(this);
        dataManager = DataManager.getInstance();


        setPopUpSize();
        setCancelButton();
        setProcessing();
    }

    private void setProcessing() {
        download = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Intent i = new Intent();
                setDownloadingData();
                while (dataManager.isUpdated() == false) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (dataManager.isCancelled() == false) {
                    setResult(DownloadingDataActivity.RESULT_OK, i);
                    finish();
                }
                setResult(RESULT_CANCELED, i);
                finish();
            }
        });
        download.start();
    }


    private void setCancelButton() {
        Button btn = findViewById(R.id.button_cancel_download);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download.interrupt();
                dataManager.setCancel(true);

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void setPopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int popUpWidth = dm.widthPixels;
        int popUpHeight = dm.heightPixels;

        getWindow().setLayout((int)(popUpWidth * .8), (int)(popUpHeight * .6));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDownloadingData() {
        dataManager.readRestaurantURL();
        dataManager.readInspectionsURL();
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
        }
        catch (IOException e) {
            Log.wtf("InspectionManager", "Error reading file on line " + csvLine, e);
            e.printStackTrace();
        }
    }


    private Violation readViolation(String[] violationStringArray) {

        int violationNum = Integer.parseInt(violationStringArray[0].replaceAll("[^0-9]", ""));
        String criticalOrNon = violationStringArray[1];
        String description = violationStringArray[2];
        String isRepeat = violationStringArray[3];

        return new Violation(violationNum , criticalOrNon, description, isRepeat);
    }

}