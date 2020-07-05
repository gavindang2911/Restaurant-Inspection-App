package cmpt276.sample.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class SingleInspection extends AppCompatActivity {
    private List<Inspection> myInspection= new ArrayList<Inspection>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_inspection);
        populateInspectionList();
        populateListView();
    }


    private void populateInspectionList() {
        myInspection.add(new Inspection(R.drawable.bang, "description", "violation", 0));

    }

    private void populateListView() {
        ArrayAdapter<Inspection> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.ListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Inspection> {

        public MyListAdapter() {
            super(SingleInspection.this, R.layout.single_violation, myInspection);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if(itemView ==null){
                itemView = getLayoutInflater().inflate(R.layout.single_violation, parent, false);
            }
            Inspection currentInspection = myInspection.get(position);

            ImageView imageViewNature = (ImageView)itemView.findViewById(R.id.imageViewNature);
            imageViewNature.setImageResource(currentInspection.getIconNature());

            TextView description = (TextView)itemView.findViewById(R.id.textViewDescription);
            description.setText(currentInspection.getDescription());

            TextView severity = (TextView)itemView.findViewById(R.id.textViewSeverity);
            severity.setText(currentInspection.getSeverity());

            ImageView imageViewSeverity = (ImageView)itemView.findViewById(R.id.imageViewSeverity);
            imageViewSeverity.setImageResource(currentInspection.getIconNature());
            return itemView;
        }

    }
}