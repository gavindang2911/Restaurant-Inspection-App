package cmpt276.sample.project.Model;

public class Inspection {
    private int iconNature;
    private String description;
    private String severity;
    private int iconSeverity;

    public Inspection(int iconNature, String description, String severity, int iconSeverity){
        super();
        this.iconNature = iconNature;
        this.description = description;
        this.severity = severity;
        this.iconSeverity = iconSeverity;
    }

    public int getIconNature() {
        return iconNature;
    }

    public int getIconSeverity() {
        return iconSeverity;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }
}
