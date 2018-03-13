package g13capstone.spotter.persistence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.interfaces.LotClickListener;
import g13capstone.spotter.interfaces.LotListListener;
import g13capstone.spotter.objects.Lot;
import g13capstone.spotter.presentation.LotPageActivity;

import static android.location.Location.distanceBetween;

/**
 * Created by Vital on 2017-10-18.
 */

public class DBLotListAccess extends AsyncTask<String, String, List<Lot>> {

    private Activity activity;
    private List<Lot> lotArrayList;
    private LocationManager locationManager;
    private LotListListener lotListener;
    private final int MAX_DIST_ACCEPTED = 25000;

    public DBLotListAccess(Activity activity){

        this.activity=activity;
    }

    @Override
    protected List<Lot> doInBackground(String... params) {

        StringBuilder data = new StringBuilder("");
        lotArrayList = new ArrayList<>();


        try{

            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = rd.readLine();
            data.append(line);

            lotArrayList = getLotJSON(data.toString());


            return lotArrayList;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    @Override
//    // pass JSON object to MAin for parsing
    protected void onPostExecute(final List<Lot> result){
        super.onPostExecute(result);

        lotListener.onLotListResults(result);
        double currLongi = 0.0;
        double currLati = 0.0;
        Location currLocation = null;

        if (result != null) {
            //Obtain the current location
            locationManager =  (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                noLocationAlert();

            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                currLocation = getLocation();

            }

            if(currLocation != null) {
                currLati = currLocation.getLatitude();
                currLongi = currLocation.getLongitude();
                calculateDistance(lotArrayList, currLati, currLongi);
                Collections.sort(lotArrayList, Lot.distanceComparator);
            }

            int count = 0;
            while (count < lotArrayList.size()){
                if (lotArrayList.get(count).getDist() == 0){
                    lotArrayList.remove(count);
                } else {
                    count++;
                }
            }

            generateLotImageID(lotArrayList);

            //Make View to display the list of lots
            RecyclerView lot = (RecyclerView)activity.findViewById(R.id.lotList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            lot.setLayoutManager(layoutManager);
            lot.setHasFixedSize(true);

            LotAdapter customListView = new LotAdapter(activity, lotArrayList,  new LotClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Lot lot = lotArrayList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", lot.getId());
                    bundle.putString("name", lot.getName());
                    bundle.putString("location", lot.getLocation());
                    bundle.putString("price", "" + lot.getPrice());
                    bundle.putString("latitude", "" + lot.getLat());
                    bundle.putString("longitude", "" + lot.getLongi());
                    bundle.putString("temp", "" + lot.getTemp());
                    bundle.putString("image", "" + lot.getImageID());
//                    Intent newIntent = new Intent(v.getContext(), LotPageActivity.class);
                    bundle.putString("capacity", "" + lot.getCapacity());
                    bundle.putString("avail", "" + lot.getAvail());
                    Intent newIntent = new Intent(v.getContext(), LotPageActivity.class);
                    newIntent.putExtras(bundle);
                    activity.startActivity(newIntent);
                }
            });

            ProgressBar progressBar = (ProgressBar)activity.findViewById(R.id.lotProgressBar);

            progressBar.setVisibility(View.INVISIBLE);

            lot.setAdapter(customListView);
            lot.setVisibility(View.VISIBLE);



        } else {
            Toast.makeText(activity, "Unable to load", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Lot> getLotJSON(String result) throws JSONException{
        JSONArray jsonArray = new JSONArray(result);
        List<Lot> lotArrayList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            Lot lot = new Lot();
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            lot.setId(jsonObject.getString("id"));
            lot.setName(jsonObject.getString("name"));
            lot.setLocation(jsonObject.getString("location"));
            lot.setPrice(Double.parseDouble(jsonObject.getString("price")));
            lot.setLat(Double.parseDouble(jsonObject.getString("latitude")));
            lot.setLongi(Double.parseDouble(jsonObject.getString("longitude")));
            lot.setTemp(Double.parseDouble(jsonObject.getString("temp")));
            lot.setCapacity(Integer.parseInt(jsonObject.getString("capacity")));
            lot.setAvail(Integer.parseInt(jsonObject.getString("availability")));

            lotArrayList.add(lot);
        }
        return lotArrayList;
    }


    private Location getLocation() {
        /*Adapter from Chanti Surabhi
        * Link: https://chantisandroid.blogspot.ca/2017/06/get-current-location-example-in-android.html*/
        Location result = null;

        //If permission not enabled, ask
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //If permission granted, get location
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
               return location;}
            else  if (location1 != null) {
                return location1;}
            else  if (location2 != null) {
                return location2;}
            else{
                Toast.makeText(activity,"Failed to get current location",Toast.LENGTH_SHORT).show();

            }
        }
        return result;
    }


    protected void noLocationAlert() {
        /*Adapter from Chanti Surabhi
        * Link: https://chantisandroid.blogspot.ca/2017/06/get-current-location-example-in-android.html*/


        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please Turn On Location Services")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void calculateDistance(List<Lot> lot, double latitude, double longitude){
        double distance;
        float[] results = new float[3];

        for(int i = 0; i < lot.size(); i++){
            distanceBetween(latitude, longitude, lot.get(i).getLat(), lot.get(i).getLongi(), results);
            distance = results[0];

            if (distance > MAX_DIST_ACCEPTED){
                lot.get(i).setDist(0);
            } else {
                lot.get(i).setDist(distance);
            }

        }
    }


    private void generateLotImageID(List<Lot> lot){
        String imgName;
        for(int i = 0; i < lot.size(); i++){
            imgName = "img_"+lot.get(i).getId();
            int resID = activity.getResources().getIdentifier(imgName, "drawable", activity.getPackageName());
            lot.get(i).setImageID(resID);
        }

    }

    public void setOnListener(LotListListener listener){
        this.lotListener = listener;
    }


}
