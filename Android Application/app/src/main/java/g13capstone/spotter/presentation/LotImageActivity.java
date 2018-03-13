package g13capstone.spotter.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import g13capstone.spotter.R;
import g13capstone.spotter.objects.Stall;

public class LotImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_image);

        //Set the UI Elements
        setTitle("");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Obtain the list of stalls to find the number of stalls in the lot and also their status
        Bundle extras = getIntent().getExtras();
        ArrayList<Stall> spotArrayList = extras.getParcelableArrayList("stallList");
        int capacity = spotArrayList.size();

        //Used for scaling the
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double scale = 0.85;

        //Load the correct background image for the image view based on the number of stalls in the lot
        ImageView imageView = findViewById(R.id.imageView3);
        String mDrawableName = "lot_image_stall_"+capacity;
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        imageView.setImageResource(resID);

        //Draw the blocks that represents the cars in the lot
        createBlocks(spotArrayList, scale);


        //Scale the entire layout in order to better display the whole image
        getWindow().setLayout((int)(scale*width),(int)(scale* height));

    }//end onCreate


    public void createBlocks(ArrayList<Stall> spotArrayList, double scale){
        /*This method is used for creating the blocks that represent the parked cars in the lot*/


        //Get the width of the screen (since lot image will fill the width first due to ratio)
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int width = (int)(scale*dm.widthPixels);
        int height = (int)(1.5*width);
        //The height is 1.5 times the width. Calculated from width to avoid some issues with pixel counts


        //Determine the size of the block. Must scale with the number of spots
        int blockHeight = (int)(dm.widthPixels/spotArrayList.size())/2;
        int blockWidth = (int) (dm.heightPixels/spotArrayList.size())/2;


        //The Offset for the first location. Also used for the spacing between lots
        int spacing =  height/spotArrayList.size()/2;
        for (int i = 0; i < spotArrayList.size(); i++){


            //If the particular stall is taken, draw a block to signify that the lot is taken
            if(spotArrayList.get(i).getStatus().equals("TAKEN")){


                //Modify Layout that will contain the new image views (the block)
                FrameLayout imageLayout = findViewById(R.id.imageLinearLayout);

                ImageView block= new ImageView(this);
                block.setBackgroundResource(R.drawable.lot_image_block);

                //Position the block in the right parking stall
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(blockWidth, blockHeight);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                layoutParams.topMargin = spacing + 2*i*spacing -blockHeight/2;

                //Add the imageView to the layout
                block.setLayoutParams(layoutParams);
                imageLayout.addView(block);

            }
        }

    }//end createBlocks
}//end LotImageActivity
