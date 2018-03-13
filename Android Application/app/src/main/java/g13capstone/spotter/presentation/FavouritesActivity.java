package g13capstone.spotter.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import g13capstone.spotter.objects.Lot;
import g13capstone.spotter.R;


public class FavouritesActivity extends AppCompatActivity {

    private static ArrayList<Integer> faveList;           //List of the lotIDs of those in favourites
    String[] lotName={"Lot X","Toonie Lot", "Engineering"};
    String[] availSpots = {"10", "7", "1"};
    ArrayList<Lot> lotArrayList = new ArrayList<Lot>();
    ListView favLotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        favLotList = (ListView) findViewById(R.id.favLotList);
//        final StubAdapter customListView=new StubAdapter(this, lotArrayList, imgId);
//        favLotList.setAdapter(customListView);

    }


    public static boolean addToFaveList(String lotID){



        return true;

    }//end addToFaveList

    public static boolean removeFromFaveList(String lotID){


        return true;
    }//end removeFromFaveList

}
