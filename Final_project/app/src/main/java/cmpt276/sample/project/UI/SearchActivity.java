package cmpt276.sample.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import cmpt276.sample.project.R;

public class SearchActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT_SEARCH = 110;
    int greenButton = 0;
    int redButton = 0;
    int orangeButton = 0;
    int favButton = 0;
    int largerNum = -1;
    int lessNum = Integer.MAX_VALUE;
    String searchText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpSearch();
        setUpRequirement();
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
                }else{
                    favourite.setBackgroundResource(R.drawable.star_unclick);
                    favButton = 0;
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
    private void setUpSearch(){
        Button button = findViewById(R.id.buttonSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}