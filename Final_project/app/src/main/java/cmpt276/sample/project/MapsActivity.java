package cmpt276.sample.project;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import cmpt276.sample.project.Model.CustomInfoAdapter;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private Location mLastKnownLocation;

    private final LatLng Surrey = new LatLng(49.187500,-122.849000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get Permission
        getLocationPermission();

        addRestaurantMarkers();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    //add restaurants markers
    private void addRestaurantMarkers(){
        mMap.setInfoWindowAdapter(new CustomInfoAdapter(MapsActivity.this));
        for(Restaurant res : restaurantManager){
            double lat = res.getLatitude();
            double lon = res.getLongitude();
            LatLng restaurantPosition = new LatLng(lat,lon);
            String restaurantLocation = res.getAddress();
            if(res.getInspections().size()!=0) {
                if(res.getInspections().get(0).getHazardRating().equals("Low")){
                    Bitmap resized = resizeMapIcon("low_hazard_marker",100,100);
                    mMap.addMarker(new MarkerOptions().position(restaurantPosition)
                            .title(res.getName())
                            .snippet("Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating())
                            .icon(BitmapDescriptorFactory.fromBitmap(resized)));
                }
                else if(res.getInspections().get(0).getHazardRating().equals("Moderate")){
                    Bitmap resized = resizeMapIcon("medium_hazard_marker",100,100);
                    mMap.addMarker(new MarkerOptions().position(restaurantPosition)
                            .title(res.getName())
                            .snippet("Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating())
                            .icon(BitmapDescriptorFactory.fromBitmap(resized)));
                }
                else {
                    Bitmap resized = resizeMapIcon("high_hazard_marker",100,100);
                    mMap.addMarker(new MarkerOptions().position(restaurantPosition)
                            .title(res.getName())
                            .snippet("Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating())
                            .icon(BitmapDescriptorFactory.fromBitmap(resized)));
                }
            }
            else {
                Bitmap resized = resizeMapIcon("no_inspection_marker",100,100);
                mMap.addMarker(new MarkerOptions().position(restaurantPosition).
                        title(res.getName())
                        .snippet("Address: " + restaurantLocation + "\n" + "Hazard Level: Unknown")
                        .icon(BitmapDescriptorFactory.fromBitmap(resized)));
            }
        }
    }

    public Bitmap resizeMapIcon(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName,"drawable",getPackageName()));
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(imageBitmap,width,height,false);
        return resizeBitmap;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 12));
                        } else {
                            Log.d("current location", "Current location is null. Using defaults.");
                            Log.e("current location", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Surrey, 12));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }




}