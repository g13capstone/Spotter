package g13capstone.spotter.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import g13capstone.spotter.R;
import g13capstone.spotter.objects.Stall;
import g13capstone.spotter.persistence.SpotAdapter;

public class LotPageActivity extends AppCompatActivity {

    //Lot Info Variables
    String lotName;
    String lotLocation;
    int lotAvailSpots;
    int lotCapacity;
    int lotImageID;
    String lotID;
    String lotPrice;
    String lotTemp;


    //Used to check if the lot is on favourites or not
    boolean isFave;
    boolean sortByName;

    //List of all stalls in the lot
    ArrayList<Stall> spotArrayList = new ArrayList<>();
//    List<Stall> spotArrayList = new ArrayList<>();

    //Layout Variables
    String subtitle_avail;      //Used as toolbar text
    String subtitle_price;      //Used as toolbar text
    String subtitle_capacity;   //Used as toolbar text
//    String subtitle_temp;       //Used as toolbar text
    ListView spots;           //Used as the list of spots in the lot to display in the lot
    FloatingActionButton fab; //Used to go to lot image view
    SpotAdapter customListView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        spots = (ListView) findViewById(R.id.spotList);

        //Get Lot Information
        Bundle bundle = this.getIntent().getExtras();       //Get bundle from parent activity (main)
        lotID = bundle.getString("id");

        //Get Info
        fetchData(lotID);

        //Assign info to lot variables
        lotName = bundle.getString("name");
        lotLocation = bundle.getString("location");
//        lotTemp = bundle.getString("temp");
        lotPrice = bundle.getString("price");
        lotLocation = bundle.getString("location");
        lotCapacity = Integer.parseInt(bundle.getString("capacity"));
        lotAvailSpots = Integer.parseInt(bundle.getString("avail"));
        lotImageID = Integer.parseInt(bundle.getString("image"));




        //Set Activity Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_page);


        isFave = false;                       //TEMPORARY LINE
        sortByName = true;


        //Initialize UI elements
        //Initialize toolbar elements

        setTitle(lotLocation);

        subtitle_avail = getString(R.string.subtitle_lot_avail);
        subtitle_price = getString(R.string.subtitle_lot_price);
        subtitle_capacity = getString(R.string.subtitle_lot_capacity);
//        subtitle_temp = getString(R.string.subtitle_lot_temp);

        //Update toolbar information
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(lotName);
        toolbar.setSubtitle(subtitle_avail+"  "+lotAvailSpots+"                "+subtitle_price+lotPrice);
        ImageView imageView = (ImageView) findViewById(R.id.app_bar_image);
        imageView.setImageResource(lotImageID);
        TextView lotInfo = (TextView) findViewById(R.id.lotInfo);
        lotInfo.setText(subtitle_capacity+"  "+lotCapacity);


        //Initialize FAB
        fab = (FloatingActionButton)findViewById(R.id.lotImageView);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if(!sortByName){sortName(spotArrayList, customListView);}

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("stallList", spotArrayList);
                Intent intent = new Intent(LotPageActivity.this, LotImageActivity.class );
                intent.putExtras(bundle);
                startActivity(intent);

                if(!sortByName){sortAvail(spotArrayList, customListView);}
            }
        });


    }//end onCreate


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_lotpage, menu);

        return true;
    }//end onCreateOptions Menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortMessage;
        Toast sortToast;

        switch (item.getItemId()){
            case R.id.action_comment_lot:

                Intent intent = new Intent(this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("activity", "Lot");
                bundle.putString("lotID", ""+lotID);
                intent.putExtras(bundle);
                this.startActivity(intent);

                return true;

            case R.id.action_refresh:

                ProgressBar progressBar = (ProgressBar)this.findViewById(R.id.stallProgressBar);
                progressBar.setVisibility(View.VISIBLE);
                //do refresh function here
                refresh(lotID, customListView);

                return true;

            case R.id.action_sort:
                if(sortByName){
                    sortAvail(spotArrayList, customListView);
                    item.setIcon(R.mipmap.ic_sort_avail);
                    sortMessage = getString(R.string.sortAvailMessage);
                    sortToast = Toast.makeText(getApplicationContext(), sortMessage, Toast.LENGTH_SHORT);
                    sortToast.show();
                    sortByName = false;
                }

                else{
                    sortName(spotArrayList, customListView);
                    item.setIcon(R.mipmap.ic_sort_name);
                    sortMessage = getString(R.string.sortNameMessage);
                    sortToast = Toast.makeText(getApplicationContext(), sortMessage, Toast.LENGTH_SHORT);
                    sortToast.show();
                    sortByName = true;
                }

                return true;


        }

        return super.onOptionsItemSelected(item);

    }//end onCreateOptionsMenu

    public void doFavourite(MenuItem item){
        /*This method is used for changing the favourites button to show if the lot is in
        favourites or not. This also shows feedback messages to the user to show successful favouriting action*/

        String faveMessage;
        Toast faveToast;

        //Check if the lot is already in favourites
        if(!isFave) {
            if(FavouritesActivity.addToFaveList(lotID)){
                item.setIcon(R.mipmap.ic_fav_star);

                isFave = true;

                faveMessage = getString(R.string.fave_add_success);
                faveToast =  Toast.makeText(getApplicationContext(), faveMessage, Toast.LENGTH_SHORT);
                faveToast.show();
            }
            else{

                faveMessage = getString(R.string.fave_add_fail);
                faveToast =  Toast.makeText(getApplicationContext(), faveMessage, Toast.LENGTH_SHORT);
                faveToast.show();
                //Show toast saying unable to add
            }
            //add adding to favourites algorithm here

        }
        else{
            if(FavouritesActivity.removeFromFaveList(lotID)) {
//            if(FavouritesActivity.removeFromFaveList(lotID)) {
                item.setIcon(R.mipmap.ic_fav_star_empty);
                isFave = false;


                faveMessage = getString(R.string.fave_del_success);
                faveToast =  Toast.makeText(getApplicationContext(), faveMessage, Toast.LENGTH_SHORT);
                faveToast.show();
                //add remove from favourites algorithm here
            }

            else{
                //Show toast saying unable to remove

                faveMessage = getString(R.string.fave_del_fail);
                faveToast =  Toast.makeText(getApplicationContext(), faveMessage, Toast.LENGTH_SHORT);
                faveToast.show();
            }
        }

    }//end doFavourite



    //Methods for backend function
    public void refresh(String lotID, SpotAdapter spotAdapter){

        /*This method is used for re-fetching the data from the server. This method also refreshes
        * the UI elements to reflect the newly fetched data*/
        int newAvailCount;
        spotArrayList.clear();

        fetchData(lotID);
        if(sortByName){sortName(spotArrayList,spotAdapter);}
        else{sortAvail(spotArrayList, spotAdapter);}

//        fetchData(lotID);

//        try {
//            spotArrayList = new DBLotSpotAccess(this).execute(getString(R.string.spot_link)+lotID).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        spotAdapter.notifyDataSetChanged();
//        newAvailCount = getAvailCount(spotArrayList);
//
//        lotAvailSpots = newAvailCount;
//        toolbar.setSubtitle(subtitle_avail+"  "+lotAvailSpots+"                "+subtitle_price+lotPrice);


        String refreshMessage = getString(R.string.refresh_message);
        Toast refreshToast = Toast.makeText(getApplicationContext(), refreshMessage, Toast.LENGTH_SHORT);
        refreshToast.show();

    }//end refresh

    public void fetchData(String lotID){
        /*This method is used for obtaining the lot data from the server*/

        //Fetch Spot information from database server
        String output = "";
//        try {
//            output = new DBLotSpotAccess(this).execute(getString(R.string.spot_link)+lotID).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        new DBLotSpotAccess(this).execute(getString(R.string.spot_link)+lotID);


        //Parse the information given
        try {
            getLotJSON(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }//end fetchData



    public void getLotJSON(String result) throws JSONException{
        /*This method is used for building the list of all the stalls in the lot and their status*/
        JSONArray jsonArray = new JSONArray(result);
        for (int i=0; i<jsonArray.length(); i++){
            Stall stall = new Stall();
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            stall.setSpotID(jsonObject.getString("stall_id"));
            stall.setStatus(jsonObject.getString("status"));

            spotArrayList.add(stall);
        }
    }//end getLotJSON

    public int getAvailCount(ArrayList<Stall> spotArrayList) {
        /*This method counts the number of available spots by iterating through the list of stalls*/
        int result = 0;

        for (int i = 0; i < spotArrayList.size(); i++) {
            if (spotArrayList.get(i).getStatus().equals("AVAILABLE")) { result++; }
        }
        return result;

    }//end getAvailCount

    public void sortAvail(ArrayList<Stall> spotArrayList, SpotAdapter spotAdapter){
        Collections.sort(spotArrayList, Stall.AvailComparator);
        spotAdapter.notifyDataSetChanged();
    }//end sortAvail

    public void sortName(ArrayList<Stall> spotArrayList, SpotAdapter spotAdapter){
        Collections.sort(spotArrayList, Stall.NameComparator);
        spotAdapter.notifyDataSetChanged();
    }//end sortName

    public class DBLotSpotAccess extends AsyncTask<String, String, ArrayList<Stall>> {


        private Activity activity;
        private ArrayList<Stall> stallArrayList;

        public DBLotSpotAccess(Activity activity){

            this.activity=activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Stall> doInBackground(String... params) {

            StringBuilder data = new StringBuilder("");
            stallArrayList = new ArrayList<>();

            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = rd.readLine();
                data.append(line);

                stallArrayList = getLotJSON(data.toString());

                return stallArrayList;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Stall> stalls) {
            super.onPostExecute(stalls);
            spotArrayList = stalls;
            spots = (ListView) findViewById(R.id.spotList);
            customListView=new SpotAdapter(activity, spotArrayList);

            ProgressBar progressBar = (ProgressBar)activity.findViewById(R.id.stallProgressBar);
            progressBar.setVisibility(View.INVISIBLE);

            spots.setAdapter(customListView);
            ViewCompat.setNestedScrollingEnabled(spots, true);

            lotAvailSpots = getAvailCount(spotArrayList);
            toolbar.setSubtitle(subtitle_avail+"  "+lotAvailSpots+"                "+subtitle_price+lotPrice);

        }

        private ArrayList<Stall> getLotJSON(String result) throws JSONException{
            JSONArray jsonArray = new JSONArray(result);
            for (int i=0; i<jsonArray.length(); i++){
                Stall stall = new Stall();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                stall.setSpotID(jsonObject.getString("stall_id"));
                stall.setStatus(jsonObject.getString("status"));

                stallArrayList.add(stall);
            }
            return stallArrayList;
        }//end getLotJSON
    }

}//end LotPageActivity
//sd
