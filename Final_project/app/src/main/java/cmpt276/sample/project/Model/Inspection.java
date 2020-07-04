package cmpt276.sample.project.Model;

public class Inspection {
    private String trackingNumber;
    private int inspectionDate;
    private String inspectionType;
    private int numOfCritical;
    private int numOfNonCritical;
    private String hazardRating;
    private String violLump;

//    public Inspection(String trackingNumber, int inspectionDate, String inspectionType, int numOfCritical, int numOfNonCritical, String hazardRating, String violLump) {
//        this.trackingNumber = trackingNumber;
//        this.inspectionDate = inspectionDate;
//        this.inspectionType = inspectionType;
//        this.numOfCritical = numOfCritical;
//        this.numOfNonCritical = numOfNonCritical;
//        this.hazardRating = hazardRating;
//        this.violLump = violLump;
//    }

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

    public String getViolLump() {
        return violLump;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setInspectionDate(int inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public void setNumOfCritical(int numOfCritical) {
        this.numOfCritical = numOfCritical;
    }

    public void setNumOfNonCritical(int numOfNonCritical) {
        this.numOfNonCritical = numOfNonCritical;
    }

    public void setHazardRating(String hazardRating) {
        this.hazardRating = hazardRating;
    }

    public void setViolLump(String violLump) {
        this.violLump = violLump;
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
                ", violLump='" + violLump + '\'' +
                '}';
    }
}
