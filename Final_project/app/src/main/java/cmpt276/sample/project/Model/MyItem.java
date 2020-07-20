package cmpt276.sample.project.Model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private String mIcon;
    private String trackingNumber;

    public MyItem(LatLng mPosition, String mTitle, String mSnippet, String mIcon, String trackingNumber) {
        this.mPosition = mPosition;
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.mIcon = mIcon;
        this.trackingNumber = trackingNumber;
    }

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "";
        mSnippet = "";
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() { return mPosition; }

    public String getTitle() { return mTitle; }

    public String getSnippet() { return mSnippet; }

    public String getIcon() { return mIcon; }

    public String getTrackingNumber() { return trackingNumber; }

}
