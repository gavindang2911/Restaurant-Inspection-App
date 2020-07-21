package cmpt276.sample.project.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import cmpt276.sample.project.Model.DataManager;
import cmpt276.sample.project.Model.DateUtils;
import cmpt276.sample.project.R;

public class UpdateDataActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_DOWNLOADING = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);


        setPopUpSize();
        setUpUpdateButton();
        setUpCancelButton();
    }

    private void setPopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int popUpWidth = dm.widthPixels;
        int popUpHeight = dm.heightPixels;
        getWindow().setLayout((int)(popUpWidth * .8), (int)(popUpHeight * .6));
    }

    private void setUpCancelButton() {
        Button btnCancel = (Button) findViewById(R.id.button_cancel_popup);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpUpdateButton() {
        Button btnUpdate = (Button) findViewById(R.id.button_update_popup);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DownloadingDataActivity.makeIntentForDownloadingData(UpdateDataActivity.this);
                startActivityForResult(intent, ACTIVITY_RESULT_DOWNLOADING);
            }
        });
    }
    // Gets called when the Lens Details activity we started, finishes.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(UpdateDataActivity.RESULT_OK, intent);
            finish();
        }
        // Handle Cancel Button
        if (resultCode == RESULT_CANCELED) {
            Intent intent = new Intent();
            setResult(UpdateDataActivity.RESULT_CANCELED, intent);
            finish();
        }

    }

    public static Intent makeIntentForUpdateData(Context context){
        Intent intent = new Intent(context, UpdateDataActivity.class);
        return intent;
    }
}