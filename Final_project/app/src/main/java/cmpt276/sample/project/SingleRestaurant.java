package cmpt276.sample.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SingleRestaurant extends AppCompatActivity {

    public static Intent makeIntentForSingleRestaurant(Context context) {
        return new Intent(context, SingleRestaurant.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_restaurant);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
