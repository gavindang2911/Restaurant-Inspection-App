package cmpt276.sample.project.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

/**
 * Restaurant class which holds the information of a restaurant (id, name, address,..)
 * It also has an array list for it's own inspections
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */
public class Restaurant {
    private String trackingNumber;
    private String name;
    private String address;
    private String city;
    private String type;
    private double latitude;
    private double longitude;
    private String iconName;
    private int icon;
    private List<Inspection> inspections = new ArrayList<>();
    private int numOfViolations;

    private boolean isFavourite = false;


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

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

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

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconName() { return iconName; }

    public void setIconName(String iconName) { this.iconName = iconName; }

    public int getNumOfViolations() { return numOfViolations; }

    public void setNumOfViolations(int numOfViolations) { this.numOfViolations = numOfViolations; }

    @Override
    public String toString() {
        return "Restaurant{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}