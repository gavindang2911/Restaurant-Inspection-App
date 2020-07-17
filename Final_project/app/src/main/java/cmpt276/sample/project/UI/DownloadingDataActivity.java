package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.sample.project.R;

public class DownloadingDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_data);
    }

    public static Intent makeIntentForDownloadingData(Context context){
        Intent intent = new Intent(context, DownloadingDataActivity.class);
        return intent;
    }
}