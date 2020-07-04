package cmpt276.sample.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.InspectionManager;

public class SingleRestaurant extends AppCompatActivity {
    InspectionManager inspectionManager = InspectionManager.getInstance();
    String[] data;

    public static Intent makeIntentForSingleRestaurant(Context context) {
        return new Intent(context, SingleRestaurant.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        readInspectionData();
    }

    private void readInspectionData() {
        InputStream inputStream = getResources().openRawResource(R.raw.inspectionreports_itr1);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null)
            {
                data = csvLine.split(",");
                try{
                    Inspection inspection = new Inspection();
                    inspection.setTrackingNumber(data[0]);
                    inspection.setInspectionDate(Integer.parseInt(data[1]));
                    inspection.setInspectionType(data[2]);
                    inspection.setNumOfCritical(Integer.parseInt(data[3]));
                    inspection.setNumOfNonCritical(Integer.parseInt(data[4]));
                    inspection.setHazardRating(data[5]);
                    if (data[6].length() > 0) {
                        inspection.setViolLump(data[6]);
                    }
                    else {
                        inspection.setViolLump("");
                    }

                    inspectionManager.add(inspection);

                    Log.d("My Activity", "just created: " + inspection );
                }catch (Exception e){
                    Log.e("Problem",e.toString());
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
    }
}
