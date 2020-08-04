package cmpt276.sample.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cmpt276.sample.project.Model.DateUtils;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.R;

public class NewInspectionActivity extends AppCompatActivity {
    private List<Restaurant> restaurantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inspection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String restaurantListAsString = getIntent().getStringExtra("list_newRestaurant_as_string");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Restaurant>>(){}.getType();
        restaurantsList = gson.fromJson(restaurantListAsString, type);

        newInspectionListView();
    }

    private void newInspectionListView(){
        ArrayAdapter<Restaurant> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.newInspectionListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Restaurant>{

        public MyListAdapter(){
            super(NewInspectionActivity.this,R.layout.new_inspection_item, restaurantsList);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.new_inspection_item,parent,false);
            }

            Restaurant currentRestaurant = restaurantsList.get(position);

            //set Name
            TextView nameText = (TextView) itemView.findViewById(R.id.textView_New_Inspection_ResName);
            nameText.setText(currentRestaurant.getName());



            //set icon of hazard level and date of inspection
            ImageView imageIcon = (ImageView) itemView.findViewById(R.id.imageView_New_Inspection_hazard_icon);
            TextView hazardLevelText = (TextView) itemView.findViewById(R.id.text_view_hazard_New_Inspection);
            TextView lastDateOfInspection = (TextView) itemView.findViewById(R.id.textView_New_Inspection_Date);


            if(currentRestaurant.getInspections().size()!=0) {

                hazardLevelText.setText(currentRestaurant.getInspections().get(0).getHazardRating());
                lastDateOfInspection.setText(DateUtils.DAY_MONTH_YEAR.getDateString(currentRestaurant.getInspections().get(0).getInspectionDate()));


                if (currentRestaurant.getInspections().get(0).getHazardRating().equals("Low")) {
                    imageIcon.setImageResource(R.drawable.green_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#459E48"));
                } else if (currentRestaurant.getInspections().get(0).getHazardRating().equals("Moderate")) {
                    imageIcon.setImageResource(R.drawable.orange_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#FF6722"));
                } else {
                    imageIcon.setImageResource(R.drawable.red_circle);
                    hazardLevelText.setTextColor(Color.parseColor("#C6170B"));
                }
            }
            return itemView;
        }
    }
}