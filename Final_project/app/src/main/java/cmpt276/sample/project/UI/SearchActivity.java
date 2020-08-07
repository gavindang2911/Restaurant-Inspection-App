package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import cmpt276.sample.project.Model.InspectionManager;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.R;

public class SearchActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_SEARCH = 110;

    private static final String FROM_ACTIVITY = "fromActivity";
    private static final String FROM_MAP = "fromMap";
    private static final String HAZARD_LEVEL = "hazard_level";
    private static final String FAVOURITE = "favourite_or_not";
    private static final String LAGER_THAN_NUM= "lager_than";
    private static final String LESS_THAN_NUM = "less_than";
    private static final String RESET = "reset";
    private static final String SEARCH_TEXT = "search_text";

    private static int fromActivity = -1;
    private static int fromMap = -1;

    int greenButton = 0;
    int redButton = 0;
    int orangeButton = 0;
    int favButton = 0;
    int largerNum = -1;
    int lessNum = Integer.MAX_VALUE;
    String searchText = "";
    String hazard_level = "";
    String favourite_or_not = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpSearch();
        setUpRequirement();
        extractDataFromIntent(this.getIntent());
        setUpReset();
    }

    /*
    site for image: https://icons8.com/icons/set/star
     */

    private void setUpRequirement(){
        final Button red = findViewById(R.id.buttonRed);
        final Button orange = findViewById(R.id.buttonOrange);
        final Button green = findViewById(R.id.buttonGreen);
        final ImageButton favourite = findViewById(R.id.imageButtonFav);
        EditText searchView = findViewById(R.id.editTextSearch);
        EditText largerThan = findViewById(R.id.editTextTextLager);
        EditText lessThan = findViewById(R.id.editTextTextLess);


        if (hazard_level.equals("High")) {
            red.setBackgroundResource(R.drawable.red_button);
            redButton = 1;
            greenButton = 0;
            orangeButton = 0;
        } else if (hazard_level.equals("Low")) {
            green.setBackgroundResource(R.drawable.green_button);
            redButton = 0;
            greenButton = 1;
            orangeButton = 0;
        } else if (hazard_level.equals("Moderate")) {
            orange.setBackgroundResource(R.drawable.orange_button);
            redButton = 0;
            greenButton = 0;
            orangeButton = 1;
        }

        if (searchText != ""){
            searchView.setText(searchText);
        }

        if (largerNum != -1) {
            largerThan.setText(largerNum);
        }

        if (lessNum != Integer.MAX_VALUE) {
            lessThan.setText(lessNum);
        }

        if (favourite_or_not == "yes") {
            favourite.setBackgroundResource(R.drawable.star_clicked);
            favButton = 1;
        } else {
            favourite.setBackgroundResource(R.drawable.star_unclick);
            favButton = 0;
            favourite_or_not = "no";
        }
        
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redButton == 0) {
                    red.setBackgroundResource(R.drawable.red_button);
                    orange.setBackgroundResource(R.drawable.orange_outline_button);
                    green.setBackgroundResource(R.drawable.green_outline_button);
                    redButton = 1;
                    greenButton = 0;
                    orangeButton = 0;
                    hazard_level = "High";
                }else{
                    red.setBackgroundResource(R.drawable.red_outline_button);
                }
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orangeButton == 0) {
                    red.setBackgroundResource(R.drawable.red_outline_button);
                    orange.setBackgroundResource(R.drawable.orange_button);
                    green.setBackgroundResource(R.drawable.green_outline_button);
                    redButton = 0;
                    greenButton = 0;
                    orangeButton = 1;
                    hazard_level = "Moderate";
                }else{
                    orange.setBackgroundResource(R.drawable.orange_outline_button);
                }
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (greenButton == 0) {
                    red.setBackgroundResource(R.drawable.red_outline_button);
                    orange.setBackgroundResource(R.drawable.orange_outline_button);
                    green.setBackgroundResource(R.drawable.green_button);
                    redButton = 0;
                    greenButton = 1;
                    orangeButton = 0;
                    hazard_level = "Low";
                }else{
                    green.setBackgroundResource(R.drawable.green_outline_button);
                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favButton == 0){
                    favourite.setBackgroundResource(R.drawable.star_clicked);
                    favButton = 1;
                    favourite_or_not = "yes";
                }else{
                    favourite.setBackgroundResource(R.drawable.star_unclick);
                    favButton = 0;
                    favourite_or_not = "no";
                }
            }
        });


        if (!largerThan.getText().toString().equals("")){
            largerNum = Integer.parseInt(largerThan.getText().toString());
        }

        if (!lessThan.getText().toString().equals("")){
            lessNum = Integer.parseInt(lessThan.getText().toString());
        }

        if (!searchView.getText().toString().equals("")){
            searchText = searchView.getText().toString();
        }
    }

    private void extractDataFromIntent(Intent intent)
    {
        fromActivity = intent.getIntExtra(FROM_ACTIVITY, -1);
        fromMap = intent.getIntExtra(FROM_MAP, -1);

        largerNum = intent.getIntExtra(LAGER_THAN_NUM, -1);
        lessNum = intent.getIntExtra(LESS_THAN_NUM, -1);
        searchText = intent.getStringExtra(SEARCH_TEXT);
        hazard_level = intent.getStringExtra(HAZARD_LEVEL);
        favourite_or_not = intent.getStringExtra(FAVOURITE);
    }

    private void setUpReset(){
        Button button = findViewById(R.id.buttonReset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromActivity == 1) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra(RESET, "yes");
                    intent.putExtra(FAVOURITE, "");
                    intent.putExtra(HAZARD_LEVEL, "");
                    intent.putExtra(LAGER_THAN_NUM, -1);
                    intent.putExtra(LESS_THAN_NUM, Integer.MAX_VALUE);
                    intent.putExtra(SEARCH_TEXT, "");
                    startActivityForResult(intent, 1);
                }else if (fromMap == 1) {
                    Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                    intent.putExtra(RESET, "yes");
                    intent.putExtra(FAVOURITE, "");
                    intent.putExtra(HAZARD_LEVEL, "");
                    intent.putExtra(LAGER_THAN_NUM, -1);
                    intent.putExtra(LESS_THAN_NUM, Integer.MAX_VALUE);
                    intent.putExtra(SEARCH_TEXT, "");
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    private void setUpSearch(){
        Button button = findViewById(R.id.buttonSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromActivity == 1) {
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra(RESET, "no");
                    intent.putExtra(FAVOURITE, favourite_or_not);
                    intent.putExtra(HAZARD_LEVEL, hazard_level);
                    intent.putExtra(LAGER_THAN_NUM, largerNum);
                    intent.putExtra(LESS_THAN_NUM, lessNum);
                    intent.putExtra(SEARCH_TEXT, searchText);
                    startActivityForResult(intent, 1);
                }else if (fromMap == 1) {
                    Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
                    intent.putExtra(RESET, "no");
                    intent.putExtra(FAVOURITE, favourite_or_not);
                    intent.putExtra(HAZARD_LEVEL, hazard_level);
                    intent.putExtra(LAGER_THAN_NUM, largerNum);
                    intent.putExtra(LESS_THAN_NUM, lessNum);
                    intent.putExtra(SEARCH_TEXT, searchText);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }
}