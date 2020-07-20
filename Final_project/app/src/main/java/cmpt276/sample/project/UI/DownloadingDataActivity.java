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

}