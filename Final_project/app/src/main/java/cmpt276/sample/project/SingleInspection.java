package cmpt276.sample.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
        //setText();
        //populateInspection();
    }

    private void populateInspectionList() {
        myViolation.add(new Violation(201,"Critical", "description", "repeat"));
        myViolation.add(new Violation(202,"Critical", "descriptionaaa", "repeat"));
        myViolation.add(new Violation(202,"NotCritical", "descriptionooo", "NotRepeat"));

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

            if(currentViolation.getViolationNum() == 201 || currentViolation.getViolationNum() == 202){
                ImageView imageViewNature = (ImageView)itemView.findViewById(R.id.imageViewNature);
                imageViewNature.setImageResource(R.drawable.red_circle);



                ImageView imageViewSeverity = (ImageView)itemView.findViewById(R.id.imageViewSeverity);
                imageViewSeverity.setImageResource(R.drawable.bang);
            }


            TextView description = (TextView)itemView.findViewById(R.id.textViewDescription);
            description.setText(currentViolation.getDescription());

            TextView severity = (TextView)itemView.findViewById(R.id.textViewSeverity);
            severity.setText(currentViolation.getCriticalOrNon());

            return itemView;
        }

    }
}