package cmpt276.sample.project.Model;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cmpt276.sample.project.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataManager {
    private UpdateManager updateManager = UpdateManager.getInstance();
    private Context context;
    private static DataManager instance;

    private String url1; // URL for restaurant
    private String url2; // URL for inspection
    private String urlForRestaurantCSV;
    private String urlForInspectionCSV;

    public DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            throw new AssertionError("Call DataManager.init() method first");
        }
        return instance;
    }

    public static DataManager init(Context context) {
        if (instance != null) {
            return null;
        }
        instance = new DataManager(context);
        return instance;
    }

    private DataManager(Context context) {
        this.context = context;
        this.url1 = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
        this.url2 = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
//        readRestaurantURL();
//        readInspectionsURL();
    }

    private void setURLForRestaurantCSV(String urlReturn) {
        this.urlForRestaurantCSV = urlForRestaurantCSV;
    }

    private void setURLForInspectionCSV(String urlReturn) {
        this.urlForInspectionCSV = urlReturn;
    }

    public void readRestaurantURL() {

        /** Create okHttp to make get request **/
        OkHttpClient client = new OkHttpClient();

        /** To hold request **/
        final Request request = new Request.Builder()
                .url(url1)
                .build();

        /** Make get request **/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("NOT RESPONSE", "abc");

                    throw new IOException("Unexpected error " + response);
                } else {
                    /** Store the response String. **/
                    final String myResponse = response.body().string();
                    try {
                        /** Read whole file as a JsonObject **/
                        JSONObject obj = new JSONObject(myResponse);

                        Log.i("dataaa", "abc" + obj.getJSONObject("result"));

                        /** Get real url to read actual data. **/
                        String tmp = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("url").toString();

                        String urlReturn = tmp;
                        if (!urlReturn.contains("https")) {
                            urlReturn = urlReturn.replace("http", "https");
                        }

                        String last_modified_restaurant = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
                        //  Set default for the last_modified_restaurant to null
                        String saved_last_modified_restaurant = pref.getString("last_modified_restaurants",
                                null);

                        if (saved_last_modified_restaurant == null) {
                            updateManager.setLastModifiedRestaurantsFirstTime(last_modified_restaurant);
                        } else {
                            updateManager.setLastModifiedRestaurants(last_modified_restaurant);
                        }

                        setURLForRestaurantCSV(urlReturn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void readInspectionsURL() {
        OkHttpClient client2 = new OkHttpClient();
        Request request2 = new Request.Builder()
                .url(url2)
                .build();
        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String myResponse = response.body().string();
                    try {
                        JSONObject obj2 = new JSONObject(myResponse);
                        String tmp2 = obj2.getJSONObject("result").getJSONArray("resources").getJSONObject(0).getString("url");

                        String urlReturn = tmp2;
                        if (!urlReturn.contains("https")) {
                            urlReturn = urlReturn.replace("http", "https");
                        }

                        String last_modified_inspection = obj2.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

//                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
//                        // Set default for the last_modified_restaurant to null
//                        String saved_last_modified_inspection = pref.getString("last_modified_inspections", null);
//                        if (saved_last_modified_inspection == null) {
//                            updateManager.setLastModifiedInspectionsFirstTime(last_modified_inspection);
//                        } else {
//                            updateManager.setLastModifiedInspections(last_modified_inspection);
//                        }

                        setURLForInspectionCSV(urlReturn);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void readURLForRestaurantCSVFile() {
        OkHttpClient client3 = new OkHttpClient();
        Request request3 = new Request.Builder()
                .url(urlForRestaurantCSV)
                .build();
        client3.newCall(request3).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected errors " + response);
                } else {
                    final String restaurantCSV = response.body().string();
//                    Log.i("HHHHHHHHHH", "this is write"+restaurantCSV);

                    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


                    File file = new File(path, "update_restaurants.csv");

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.out.println("Can not create files.");
                        e.printStackTrace();
                    }
                    try {

                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(restaurantCSV);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to write to File " + e);

                    }

                }
            }
        });
    }

    public void readURLForInspectionCSVFile() {
        OkHttpClient client4 = new OkHttpClient();
        Request request4 = new Request.Builder()
                .url(urlForInspectionCSV)
                .build();
        client4.newCall(request4).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected errors " + response);
                } else {
                    final String inspectionCSV = response.body().string();
//                    Log.i("HHHHHHHHHH", "this is write"+restaurantCSV);

                    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


                    File file = new File(path, "update_inspections.csv");

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.out.println("Can not create files.");
                        e.printStackTrace();
                    }
                    try {

                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(inspectionCSV);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to write to File " + e);

                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setLastUpdateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'hh:mm");
        LocalDateTime today = LocalDateTime.now();
        String todayString = formatter.format(today);
        updateManager.setLastUpdate(todayString);
    }
}
