package g13capstone.spotter.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Stall implements Parcelable{

    private String spotID;  //The ID of the spot
    private String status;  //The status of the spot

    public Stall(){
        //Default constructor
    }

    public Stall(Parcel in){
        //Used in delivering the stall object to the arraylist
        super();
        readFromParcel(in);
    }

    //Methods tp help the object inside an array list
    public static final Parcelable.Creator<Stall> CREATOR = new Parcelable.Creator<Stall>() {
        public Stall createFromParcel(Parcel in) {
            return new Stall(in);
        }

        public Stall[] newArray(int size) {
            return new Stall[size];
        }
    };

    public void readFromParcel(Parcel in) {
        spotID = in.readString();
        status = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(spotID);
        dest.writeString(status);
    }

    //Getter and Setter methods
    public void setSpotID(String newID){spotID = newID;}
    public void setStatus(String newStatus){status= newStatus;}

    public String getSpotID(){return spotID;}
    public String getStatus(){return status;}

    //Comparator for availability
    public static Comparator<Stall> AvailComparator = new Comparator<Stall>(){

        @Override
        public int compare(Stall stallA, Stall stallB){
            String stallAvailA = stallA.getStatus().toUpperCase();
            String stallAvailB = stallB.getStatus().toUpperCase();

            return stallAvailA.compareTo(stallAvailB);

        }
    };

    //Comparator for stall name
    public static Comparator<Stall> NameComparator = new Comparator<Stall>() {
        @Override
        public int compare(Stall stallA, Stall stallB){
            String stallAvailA = stallA.getSpotID().toUpperCase();
            String stallAvailB = stallB.getSpotID().toUpperCase();

            return stallAvailA.compareTo(stallAvailB);

        }
    };
}//end Stall

