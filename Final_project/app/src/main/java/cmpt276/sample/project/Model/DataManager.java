package cmpt276.sample.project.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataManager {
    private UpdateManager updateManager = UpdateManager.getInstance();
    private Context context;
    private static DataManager instance;

    private String url1 = "http://data.surrey.ca/api/3/action/package_show?id=restaurants"; // URL for restaurant
    private String url2 = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports"; // URL for inspection
    private String urlForRestaurantCSV;
    private String urlForInspectionCSV;

    public static DataManager getInstance(){
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
        readRestaurantURL();
        readInspectionsURL();
    }

    private void readRestaurantURL() {
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
                    throw new IOException("Unexpected error " + response);
                } else {
                    /** Store the response String. **/
                    final String myResponse = response.body().string();
                    try {
                        /** Read whole file as a JsonObject **/
                        JSONObject obj = new JSONObject(myResponse);

                        /** Get real url to read actual data. **/
                        String tmp = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("url").toString();

                        if (!tmp.contains("https")) {
                            tmp = tmp.replace("http", "https");
                        }

                        String last_modified_restaurant = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
                        // Set default for the last_modified_restaurant to null
                        String saved_last_modified_restaurant = pref.getString("last_modified_restaurants",
                                null);

                        if (saved_last_modified_restaurant == null) {
                            updateManager.setLastModifiedRestaurantsFirstTime(last_modified_restaurant);
                        } else {
                            updateManager.setLastModifiedRestaurants(last_modified_restaurant);
                        }

                        urlForRestaurantCSV = tmp;

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
                        if (!tmp2.contains("https")) {
                            tmp2 = tmp2.replace("http", "https");
                        }

                        String last_modified_inspection = obj2.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
                        // Set default for the last_modified_restaurant to null
                        String saved_last_modified_inspection = pref.getString("last_modified_inspections", null);
                        if (saved_last_modified_inspection == null) {
                            updateManager.setLastModifiedInspectionsFirstTime(last_modified_inspection);
                        } else {
                            updateManager.setLastModifiedInspections(last_modified_inspection);
                        }

                        urlForInspectionCSV = tmp2;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void readURLForCSVFile() {
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
                    throw new IOException("Unexpected code " + response);
                }
                else {
                    final String restaurantCSV = response.body().string();

                    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(path, "new_update_restaurant.csv");

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
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


}
