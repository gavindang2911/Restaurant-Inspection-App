package cmpt276.sample.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


        readInspectionData();
        extractDataFromIntent(this.getIntent());
        displayRestaurantInfo();
    }

    private void displayRestaurantInfo() {
        Restaurant
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
                    Log.i("i : ", ""+i);
                    Log.i("data : ", data[i]);
                }

                List<Violation> violations = getViolation(data);
//
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

                Log.d("My Activity", "just created: " + inspection );
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

    private void extractDataFromIntent(Intent intent)
    {
        positionRestaurant = intent.getIntExtra(RESTAURANT_POSITION, -1);
        RestaurantManager restaurantMan = RestaurantManager.getInstance();
        restaurant = restaurantMan.getRestaurant(positionRestaurant);
    }
}
