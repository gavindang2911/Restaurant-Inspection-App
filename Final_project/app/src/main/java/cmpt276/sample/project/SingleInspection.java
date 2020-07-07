package cmpt276.sample.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmpt276.sample.project.Model.Inspection;
import cmpt276.sample.project.Model.InspectionManager;
import cmpt276.sample.project.Model.Violation;

public class SingleInspection extends AppCompatActivity {
    private List<Inspection> myInspection = new ArrayList<Inspection>();
    private List<Violation> myViolation = new ArrayList<Violation>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_inspection);
        populateInspectionList();
        populateListView();
        registerClickCallback();
        //setText();
        //populateInspection();
    }

    private void populateInspectionList() {
        myViolation.add(new Violation(102,"Critical", "description", "repeat"));
        myViolation.add(new Violation(212,"Critical", "descriptionaaa", "repeat"));
        myViolation.add(new Violation(302,"NotCritical", "descriptionooo", "NotRepeat"));
        myViolation.add(new Violation(305,"NotCritical", "descriptionooo", "NotRepeat"));
        myViolation.add(new Violation(404,"NotCritical", "descriptionooo", "NotRepeat"));

        myInspection.add(new Inspection("SDFO", 20191002, "Routine",
                0, 0, "Low", myViolation));
    }

    /*
    private InspectionManager populateInspection(){
        InspectionManager myInspection = new InspectionManager();
        myInspection.add(new Inspection("SDFO", 20191002, "Routine",
                0, 0, "Low", myViolation));
        return myInspection;
    }



    private void setText(){
        TextView date = (TextView)findViewById(R.id.textViewDescription);
        date.setText();
    }
*/

    private void populateListView() {
        ArrayAdapter<Violation> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.ListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.ListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Violation clickedViolation = myViolation.get(position);
                String message = clickedViolation.getDescription() + ", " +
                        clickedViolation.getRepeat();
                Toast.makeText(SingleInspection.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Violation> {

        public MyListAdapter() {
            super(SingleInspection.this, R.layout.single_violation, myViolation);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView ==null){
                itemView = getLayoutInflater().inflate(R.layout.single_violation, parent, false);
            }
            Violation currentViolation = myViolation.get(position);

            int number = currentViolation.getViolationNum();

            TextView severity = (TextView)itemView.findViewById(R.id.textViewSeverity);
            severity.setText(currentViolation.getCriticalOrNon());

            ImageView imageViewSeverity = (ImageView)itemView.findViewById(R.id.imageViewSeverity);

            // set severity color and image
            if(currentViolation.getCriticalOrNon().equals("Critical")){
                //set text to green color
                severity.setTextColor(Color.parseColor("#459E48"));
                imageViewSeverity.setImageResource(R.drawable.green_circle);
            }else{
                //set text to red color
                severity.setTextColor(Color.parseColor("#C6170B"));
                imageViewSeverity.setImageResource(R.drawable.red_circle);
            }

            /*
            site for image
            https://icons8.com/icon/set/food/color
            https://icons8.com/icons/set/equipment
            https://icons8.com/icons/set/utensils
            https://icons8.com/icons/set/restaurant-facility
            https://icons8.com/icons/set/law
            https://icons8.com/icons/set/water
            https://icons8.com/icons/set/pest
            https://icons8.com/icons/set/chemical-cleanser

             */
            // Find image id
            ImageView imageViewNature1 = (ImageView)itemView.findViewById(R.id.imageViewNature1);
            ImageView imageViewNature2 = (ImageView)itemView.findViewById(R.id.imageViewNature2);
            ImageView imageViewNature3 = (ImageView)itemView.findViewById(R.id.imageViewNature3);
            //Set icon to permit
            if(number == 101 || number == 102 || number == 103 || number == 104 || number == 311){

                imageViewNature1.setImageResource(R.drawable.permit );

            }
            // Set icon to food
            else if(number == 201 || number == 202 || number == 203 || number == 204 || number == 205
                    || number == 206 || number == 208 || number == 209 || number == 210 || number == 211
                    || number == 306 || number == 312){

                imageViewNature1.setImageResource(R.drawable.food);

            }
            // Set icon to employee
            else if(number == 212 || number == 314 || number == 402 || number == 403 || number == 404
                    || number == 501 || number == 502){

                imageViewNature1.setImageResource(R.drawable.employee);
            }
            // Set icon to Equipment & utensils & food
            else if(number == 301 || number == 302 || number == 307 || number == 308){
                imageViewNature1.setImageResource(R.drawable.equipment);
                imageViewNature2.setImageResource(R.drawable.utensils);
                imageViewNature3.setImageResource(R.drawable.food);
            }
            // Set icon to Equipment & facilities & hot and cold water
            else if(number == 303){
                imageViewNature1.setImageResource(R.drawable.equipment);
                imageViewNature2.setImageResource(R.drawable.facility);
                imageViewNature3.setImageResource(R.drawable.water);
            }
            // Set icon to pest
            else if(number == 304 || number == 305 || number == 313){
                imageViewNature1.setImageResource(R.drawable.pest);
            }
            // Set icon to Chemicals cleansers
            else if(number == 309){
                imageViewNature1.setImageResource(R.drawable.chemical);
            }
            // Set icon to utensil
            else if(number == 310){
                imageViewNature1.setImageResource(R.drawable.utensils);
            }
            // Set icon to equipment
            else if(number == 315 || number == 401){
                imageViewNature1.setImageResource(R.drawable.equipment);
            }

            // Set string of description
            TextView description = (TextView)itemView.findViewById(R.id.textViewDescription);
            if (number == 101){
                description.setText(getResources().getString(R.string.violation101));
            }else if(number == 102){
                description.setText(getResources().getString(R.string.violation102));
            }else if(number == 103){
                description.setText(getResources().getString(R.string.violation103));
            }else if(number == 104){
                description.setText(getResources().getString(R.string.violation104));
            }else if(number == 201){
                description.setText(getResources().getString(R.string.violation201));
            }else if(number == 202){
                description.setText(getResources().getString(R.string.violation202));
            }else if(number == 203){
                description.setText(getResources().getString(R.string.violation203));
            }else if(number == 204){
                description.setText(getResources().getString(R.string.violation204));
            }else if(number == 205){
                description.setText(getResources().getString(R.string.violation205));
            }else if(number == 206){
                description.setText(getResources().getString(R.string.violation206));
            }else if(number == 208){
                description.setText(getResources().getString(R.string.violation208));
            }else if(number == 209){
                description.setText(getResources().getString(R.string.violation209));
            }else if(number == 210){
                description.setText(getResources().getString(R.string.violation210));
            }else if(number == 211){
                description.setText(getResources().getString(R.string.violation211));
            }else if(number == 212){
                description.setText(getResources().getString(R.string.violation212));
            }else if(number == 301){
                description.setText(getResources().getString(R.string.violation301));
            }else if(number == 302){
                description.setText(getResources().getString(R.string.violation302));
            }else if(number == 303) {
                description.setText(getResources().getString(R.string.violation303));
            }else if(number == 304) {
                description.setText(getResources().getString(R.string.violation304));
            }else if(number == 305) {
                description.setText(getResources().getString(R.string.violation305));
            }else if(number == 306) {
                description.setText(getResources().getString(R.string.violation306));
            }else if(number == 307) {
                description.setText(getResources().getString(R.string.violation307));
            }else if(number == 308) {
                description.setText(getResources().getString(R.string.violation308));
            }else if(number == 309) {
                description.setText(getResources().getString(R.string.violation309));
            }else if(number == 310) {
                description.setText(getResources().getString(R.string.violation310));
            }else if(number == 311) {
                description.setText(getResources().getString(R.string.violation311));
            }else if(number == 312) {
                description.setText(getResources().getString(R.string.violation312));
            }else if(number == 313) {
                description.setText(getResources().getString(R.string.violation313));
            }else if(number == 314) {
                description.setText(getResources().getString(R.string.violation314));
            }else if(number == 315) {
                description.setText(getResources().getString(R.string.violation315));
            }else if(number == 401) {
                description.setText(getResources().getString(R.string.violation401));
            }else if(number == 402) {
                description.setText(getResources().getString(R.string.violation402));
            }else if(number == 403) {
                description.setText(getResources().getString(R.string.violation403));
            }else if(number == 404) {
                description.setText(getResources().getString(R.string.violation404));
            }else if(number == 501) {
                description.setText(getResources().getString(R.string.violation501));
            }else if(number == 502) {
                description.setText(getResources().getString(R.string.violation502));
            }


            return itemView;
        }

    }


}