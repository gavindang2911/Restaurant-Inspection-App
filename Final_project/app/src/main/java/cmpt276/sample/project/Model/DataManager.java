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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cmpt276.sample.project.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataManager {
    private Context context;
    private static DataManager instance;

    private String urlForRestaurant; // URL for restaurant
    private String urlForInspection; // URL for inspection

    private String lastTimeModifiedRestaurants;
    private String lastTimeModifiedInspections;

    private boolean update = false;
    private boolean cancel = false;

    public boolean isUpdated() {
        return update;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancel(boolean cancelled) {
        this.cancel= cancelled;
    }

    public void setUpdate(boolean updated) {
        this.update = updated;
    }


    public void setLastTimeModifiedRestaurants(String lastTimeModifiedRestaurants) {
        this.lastTimeModifiedRestaurants = lastTimeModifiedRestaurants;
    }

    public void setLastTimeModifiedInspections(String lastTimeModifiedInspections) {
        this.lastTimeModifiedInspections = lastTimeModifiedInspections;
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
        this.urlForRestaurant = "https://data.surrey.ca/api/3/action/package_show?id=restaurants";
        this.urlForInspection = "https://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";

        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
        SharedPreferences.Editor editor = pref.edit();

        /** Set some default last updated date **/
        if (pref.getString("last_modified_restaurants", null) == null) {
            editor.putString("last_modified_restaurants", "2010-01-01 00:00:00");
        }

        if (pref.getString("last_modified_inspections", null) == null) {
            editor.putString("last_modified_inspections", "2010-01-01 00:00:00");
        }

        if (pref.getString("last_updated", null) == null) {
            editor.putString("last_updated", "2020-07-18 00:00:00");
        }

        editor.apply();
    }



    public void readRestaurantURL() {

        /** Create okHttp to make get request **/
        OkHttpClient client = new OkHttpClient();

        /** To hold request **/
        final Request request = new Request.Builder()
                .url(urlForRestaurant)
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


                        /** Get real url to read actual data. **/
                        String urlReturn = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("url").toString();

//                        if (!urlReturn.contains("https")) {
//                            urlReturn = urlReturn.replace("http", "https");
//                        }

                        String lastModifiedRestaurantFromServer = obj.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);


                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("last_modified_restaurants", lastModifiedRestaurantFromServer);
                        editor.apply();

                        setLastTimeModifiedRestaurants(lastModifiedRestaurantFromServer);
//                        Log.i("lasttime", "abc" + lastModifiedRestaurantFromServer);
//                        Log.i("lasttimelocal", "abc" + lastTimeModifiedRestaurants);


                        Request request3 = new Request.Builder()
                                .url(urlReturn)
                                .build();

                        OkHttpClient client3 = new OkHttpClient();

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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void readInspectionsURL() {
        OkHttpClient client2 = new OkHttpClient();
        Request request2 = new Request.Builder()
                .url(urlForInspection)
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

                        String last_modified_inspection_from_server = obj2.getJSONObject("result").getJSONArray("resources").getJSONObject(0).get("last_modified").toString();

                        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);

                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("last_modified_inspections",last_modified_inspection_from_server);
                        editor.apply();

                       setLastTimeModifiedInspections(last_modified_inspection_from_server);


                        OkHttpClient client4 = new OkHttpClient();
                        Request request4 = new Request.Builder()
                                .url(urlReturn)
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

                                    setLastUpdateTime();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setLastUpdateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'hh:mm:ss");
        LocalDateTime today = LocalDateTime.now();
        String todayString = formatter.format(today);

        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("last_updated", todayString);
        editor.apply();

        setUpdate(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean check20hour() {

        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);
        String[] lastUpdateDate = pref.getString("last_updated", null).replace("T", " ").replace("-", "").split(" ");


        long dateFromCurrent = DateUtils.dayFromCurrent(Integer.parseInt(lastUpdateDate[0]));
        if (dateFromCurrent >= 1) {
            return true;
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'hh:mm:ss");
            LocalDateTime currentTime = LocalDateTime.now();
            String[] currentTimeString = formatter.format(currentTime).replace("T"," ").replace("-","").split(" ");
            String end = currentTimeString[1];
            String start = lastUpdateDate[1];

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

//            for (String s: lastUpdateDate) {
//                Log.i("Time now", "isssssaa "+ s);
//            }
//            for (String ss: currentTimeString) {
//                Log.i("Time now", "isssssaa "+ ss);
//            }
            try {
                Date date1 = format.parse(start);
                Date date2 = format.parse(end);

                long difference = date2.getTime() - date1.getTime();
                int hours = (int) TimeUnit.MILLISECONDS.toHours(difference);

                // Convert time to EST time zone
                if (hours >= 17) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkIfUpdateNeeded() {
        SharedPreferences pref = context.getSharedPreferences("AppPrefs", 0);

        String lastUpdateOnDevice = pref.getString("last_updated", null);

        String lastModifiedRestaurantOnServer = pref.getString("last_modified_restaurants", null);

        String lastModifiedInspectionOnServer = pref.getString("last_modified_inspections", null);


        if (lastUpdateOnDevice.equals("2020-07-18 00:00:00")) {
            return true;
        }

        if (checkIfUpdateNeededHelper(lastUpdateOnDevice, lastModifiedRestaurantOnServer)) {
            return true;
        }

        if (checkIfUpdateNeededHelper(lastUpdateOnDevice, lastModifiedInspectionOnServer)) {
            return true;
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkIfUpdateNeededHelper(String lastUpdateOnDevice, String lastModifiedOnServer) {

        String[] lastUpdateDate = lastUpdateOnDevice.replace("T", " ").split("\\.");
        String[] lastModified = lastModifiedOnServer.replace("T", " ").split("\\.");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end = lastModified[0];
        String start = lastUpdateDate[0];

        try {
            Date date1 = format.parse(start);
            Date date2 = format.parse(end);

            long difference = date2.getTime() - date1.getTime();
            int hours = (int) TimeUnit.MILLISECONDS.toHours(difference);

            // Convert time to EST time zone
            if (hours >= -3) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
