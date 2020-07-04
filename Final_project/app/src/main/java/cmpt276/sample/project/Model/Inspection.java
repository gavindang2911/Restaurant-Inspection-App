package cmpt276.sample.project.Model;

public class Inspection {
    private int iconNature;
    private String description;
    private String Violation;
    private int iconViolation;

    public Inspection(int iconNature, String description, String Violation, int iconViolation){
        super();
        this.iconNature = iconNature;
        this.description = description;
        this.Violation = Violation;
        this.iconViolation = iconViolation;
    }

    public int getIconNature() {
        return iconNature;
    }

    public int getIconViolation() {
        return iconViolation;
    }

    public String getDescription() {
        return description;
    }

    public String getViolation() {
        return Violation;
    }
}
