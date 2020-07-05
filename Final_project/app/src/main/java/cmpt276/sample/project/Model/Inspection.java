package cmpt276.sample.project.Model;

import cmpt276.sample.project.Model.Violation;

import java.util.List;

public class Inspection implements Comparable<Inspection> {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int numOfCritical;
    private int numOfNonCritical;
    private String hazardRating;
    private List<Violation> violations;

    public Inspection(String trackingNumber, int inspectionDate, String inspectionType, int numOfCritical, int numOfNonCritical, String hazardRating, List<Violation> violations) {
        this.trackingNumber = trackingNumber;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numOfCritical = numOfCritical;
        this.numOfNonCritical = numOfNonCritical;
        this.hazardRating = hazardRating;
        this.violations = violations;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public int getInspectionDate() {
        return inspectionDate;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public int getNumOfCritical() {
        return numOfCritical;
    }

    public int getNumOfNonCritical() {
        return numOfNonCritical;
    }

    public String getHazardRating() {
        return hazardRating;
    }

    public List<Violation> getViolations() {
        return violations;
    }



    @Override
    public String toString() {
        return "Inspection{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", inspectionDate=" + inspectionDate +
                ", inspectionType='" + inspectionType + '\'' +
                ", numOfCritical=" + numOfCritical +
                ", numOfNonCritical=" + numOfNonCritical +
                ", hazardRating='" + hazardRating + '\'' +
                ", violLump='" + violations + '\'' +
                '}';
    }

    @Override
    public int compareTo(Inspection o) {
        return (this.getInspectionDate() - o.getInspectionDate());
    }
}
