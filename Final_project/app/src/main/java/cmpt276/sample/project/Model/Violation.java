package cmpt276.sample.project.Model;


public class Violation {
    private int violationNum;
    private String criticalOrNon;
    private String description;
    private String repeat;

    public Violation(int violationNum, String criticalOrNon, String description, String repeat) {
        this.violationNum = violationNum;
        this.criticalOrNon = criticalOrNon;
        this.description = description;
        this.repeat = repeat;
    }

    public String getDescription() {
        return description;
    }

    public int getViolationNum() {
        return violationNum;
    }

    public String getCriticalOrNon() {
        return criticalOrNon;
    }

    public String getRepeat() {
        return repeat;
    }
}
