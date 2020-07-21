package cmpt276.sample.project.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_data);

        DataManager.init(this);
        dataManager = DataManager.getInstance();

        setPopUpSize();
        setCancelButton();
        runThread();
    }

    private void runThread() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        Intent i = new Intent();
                        setDownloadingData();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (dataManager.isCancelled() == false) {
                            setResult(DownloadingDataActivity.RESULT_OK, i);
                            finish();
                        }
                    }
                });
            }
        });
    }


    private void setCancelButton() {
        Button btn = findViewById(R.id.button_cancel_download);
        SharedPreferences pref = this.getSharedPreferences("AppPrefs", 0);
        final SharedPreferences.Editor editor = pref.edit();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dataManager.setCancel(true);

                editor.putString("last_updated", "2020-07-01 00:00:00");
                dataManager.setUpdate(false);

                Intent intent = new Intent();
                setResult(DownloadingDataActivity.RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void setPopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int popUpWidth = dm.widthPixels;
        int popUpHeight = dm.heightPixels;
        getWindow().setLayout((int)(popUpWidth * .8), (int)(popUpHeight * .5));
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