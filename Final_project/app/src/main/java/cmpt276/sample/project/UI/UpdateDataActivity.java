package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import cmpt276.sample.project.R;

public class UpdateDataActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_CALCULATE = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

//        Button btnCancel = (Button) findViewById(R.id.button_cancel_popup);
        
        setPopUpSize();
        setUpUpdateButton();
        setUpCancelButton();

    }

    private void setPopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));
    }

    private void setUpCancelButton() {
    }

    private void setUpUpdateButton() {
        Button btnUpdate = (Button) findViewById(R.id.button_update_popup);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DownloadingDataActivity.makeIntentForDownloadingData(UpdateDataActivity.this);
                startActivityForResult(intent, ACTIVITY_RESULT_CALCULATE);
            }
        });

    }
}