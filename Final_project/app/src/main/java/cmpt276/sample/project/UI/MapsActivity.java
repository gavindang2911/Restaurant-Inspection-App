package cmpt276.sample.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;

import android.widget.Button;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


import cmpt276.sample.project.Adapter.CustomInfoAdapter;
import cmpt276.sample.project.Adapter.CustomInfoAdapter;
import cmpt276.sample.project.Model.MyItem;
import cmpt276.sample.project.Model.Restaurant;
import cmpt276.sample.project.Model.RestaurantManager;
import cmpt276.sample.project.R;

/**
 * Reference Document: Google Maps Platform Documentation: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial#connect-client
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MAP_ACTIVITY_RESULT_MAP = 200;
    private static final int MAP_ACTIVITY_RESULT_SEARCH = 115;
    private GoogleMap mMap;
    private RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private Location mLastKnownLocation;
    private ClusterManager mClusterManager;

    private static final String FROM_ACTIVITY = "fromActivity";
    private static final String FROM_MAP = "fromMap";

    private static final String HAZARD_LEVEL = "hazard_level";
    private static final String FAVOURITE = "favourite_or_not";
    private static final String LAGER_THAN_NUM= "lager_than";
    private static final String LESS_THAN_NUM = "less_than";
    private static final String RESET = "reset";
    private static final String SEARCH_TEXT = "search_text";

    int largerNum = -1;
    int lessNum = Integer.MAX_VALUE;
    String searchText = "";
    String hazard_level = "";
    String favourite_or_not = "";
    String reset = "";

    private final LatLng Surrey = new LatLng(49.187500,-122.849000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        extractDataFromIntent(this.getIntent());


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

//        addRestaurantMarkers();

        Intent intent = getIntent();
        String restaurantID = intent.getStringExtra("PassingID");

        if (restaurantID != null) {
            //cluster
            setUpCluster();

            HandleReceivingCoordinates(restaurantID);
        }

        else {
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();

            //cluster
            setUpCluster();
        }

        setUpRestaurant();
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
                    MyItem cluster = new MyItem(restaurantPosition, res.getName()
                            ,"Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating()
                            ,"Low"
                            ,res.getTrackingNumber());
                    mClusterManager.addItem(cluster);
                }
                else if(res.getInspections().get(0).getHazardRating().equals("Moderate")){
                    Bitmap resized = resizeMapIcon("medium_hazard_marker",100,100);
                    MyItem cluster = new MyItem(restaurantPosition
                            ,res.getName()
                            ,"Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating()
                            ,"Moderate"
                            ,res.getTrackingNumber());
                    mClusterManager.addItem(cluster);
                }
                else {
                    Bitmap resized = resizeMapIcon("high_hazard_marker",100,100);
                    MyItem cluster = new MyItem(restaurantPosition
                            ,res.getName()
                            ,"Address: " + restaurantLocation + "\n" + "Hazard Level: " + res.getInspections().get(0).getHazardRating()
                            ,"High"
                            ,res.getTrackingNumber());
                    mClusterManager.addItem(cluster);
                }
            }
            else {
                Bitmap resized = resizeMapIcon("no_inspection_marker",100,100);
                MyItem cluster = new MyItem(restaurantPosition
                        ,res.getName()
                        ,"Address: " + restaurantLocation + "\n" + "Hazard Level: Unknown"
                        ,"Unknown"
                        ,res.getTrackingNumber());
                mClusterManager.addItem(cluster);
            }
        }
    }

    public Bitmap resizeMapIcon(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName,"drawable",getPackageName()));
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(imageBitmap,width,height,false);
        return resizeBitmap;
    }

    private void getLocationPermission() {
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


    private void setUpCluster(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.27645,-122.917587),13));
        mClusterManager = new ClusterManager<MyItem>(this,mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        addRestaurantMarkers();
        mClusterManager.setRenderer(new CustomClusterRenderer(getApplicationContext()));
        clusterInfoWindow();
    }
    private class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {
        public CustomClusterRenderer(Context context) {
            super(context, mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(@NonNull MyItem item, @NonNull MarkerOptions markerOptions) {
            if(item.getIcon().equals("Low")){
                Bitmap resized = resizeMapIcon("low_hazard_marker",100,100);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
            else if(item.getIcon().equals("Moderate")){
                Bitmap resized = resizeMapIcon("medium_hazard_marker",100,100);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
            else if(item.getIcon().equals("High")){
                Bitmap resized = resizeMapIcon("high_hazard_marker",100,100);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
            else {
                Bitmap resized = resizeMapIcon("no_inspection_marker",100,100);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
            }
        }
    }

    private void setUpRestaurant(){
        Button button = (Button) findViewById(R.id.buttonGoToList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(MainActivity.RESULT_OK, intent);
                finish();
            }
        });
    }


    private void clusterInfoWindow() {
        mClusterManager.getMarkerCollection().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                final View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView nameView = view.findViewById(R.id.title);
                TextView detailsView = view.findViewById(R.id.snippet);

                String name = marker.getTitle();
                nameView.setText(name);
                String details = marker.getSnippet();

                detailsView.setText(details);

                return view;
            }
        });

        mClusterManager.setRenderer(new CustomClusterRenderer(getApplicationContext()));

        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MyItem item) {
                String resId = item.getTrackingNumber();
                Intent intent = SingleRestaurant.makeIntentForSingleRestaurant(MapsActivity.this, resId);
                startActivityForResult(intent, MAP_ACTIVITY_RESULT_MAP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MAP_ACTIVITY_RESULT_MAP) {
            String id = data.getStringExtra("restaurantID");
            if (!id.equals(null)){
            }
        }
    }

    private void HandleReceivingCoordinates(String ID) {
        Restaurant returnRestaurant = null;
        boolean found = false;
        for (Restaurant temp : restaurantManager) {
            if (ID.equals(temp.getTrackingNumber())) {
                returnRestaurant = temp;
                found = true;
                break;
            }
        }
        if (found) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(returnRestaurant.getLatitude(),
                            returnRestaurant.getLongitude()), 30));

            MarkerOptions options = new MarkerOptions().position(new LatLng(returnRestaurant.getLatitude(),
                    returnRestaurant.getLongitude())).title(returnRestaurant.getName())
                    .snippet("Address: " + returnRestaurant.getAddress() + "\n" + "Hazard Level: "
                            + returnRestaurant.getInspections().get(0).getHazardRating());

            Marker mMarker;
            mMarker = mMap.addMarker(options);
            mMarker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(returnRestaurant.getLatitude(),
                            returnRestaurant.getLongitude()), 30));
        }
    }

    private void extractDataFromIntent(Intent intent)
    {
        largerNum = intent.getIntExtra(LAGER_THAN_NUM, -1);
        lessNum = intent.getIntExtra(LESS_THAN_NUM, -1);
        searchText = intent.getStringExtra(SEARCH_TEXT);
        hazard_level = intent.getStringExtra(HAZARD_LEVEL);
        favourite_or_not = intent.getStringExtra(FAVOURITE);
        reset = intent.getStringExtra(RESET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.search_restaurant){
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra(FROM_ACTIVITY, 0);
            intent.putExtra(FROM_MAP, 1);
            intent.putExtra(FAVOURITE, favourite_or_not);
            intent.putExtra(HAZARD_LEVEL, hazard_level);
            intent.putExtra(LAGER_THAN_NUM, largerNum);
            intent.putExtra(LESS_THAN_NUM, lessNum);
            intent.putExtra(SEARCH_TEXT, searchText);

            startActivityForResult(intent, MAP_ACTIVITY_RESULT_SEARCH);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}