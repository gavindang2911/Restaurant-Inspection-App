package cmpt276.sample.project.Model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class InspectionManager {
    private List<Inspection> inspections;

    private static InspectionManager instance;

    private InspectionManager() {
        // private to prevent anyone else from instantiating
        inspections = new ArrayList();
    }

    public static InspectionManager getInstance() {
        if (instance == null) {
            instance = new InspectionManager();
        }
        return instance;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public void add(Inspection inspection) {
        inspections.add(inspection);


    }
}
