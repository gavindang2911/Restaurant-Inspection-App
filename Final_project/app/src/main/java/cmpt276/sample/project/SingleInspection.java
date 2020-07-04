package cmpt276.sample.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }

    private void populateInspectionList() {
        myInspection.add(new Inspection(0, "description", "violation", 0));

    }


}