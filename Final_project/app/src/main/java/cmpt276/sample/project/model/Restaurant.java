package cmpt276.sample.project.Model;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

public class Restaurant {
    private String trackingNumber;
    private String name;
    private String address;
    private String type;
    private double latitude;
    private double longitude;
    private int icon;
    private List<Inspection> inspections;
    private int numOfViolations;

    public void addInspection(Inspection inspection)
    {
        inspections.add(inspection);
        sort(inspections, Collections.<Inspection>reverseOrder());
        numOfViolations += inspection.getNumOfCritical() + inspection.getNumOfNonCritical();
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}