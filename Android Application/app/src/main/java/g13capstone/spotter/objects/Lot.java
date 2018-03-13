package g13capstone.spotter.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by Vital on 2017-11-03.
 */

public class Lot implements Parcelable{
    private String id;
    private String name;
    private String location;
    private double price;
    private double longi;   // longitude
    private double lat;     // latitude
    private double temp;    // temperature
    private int capacity;   // lot capacity
    private int avail;      // number of space available
    private double dist;    // distance
    private int imageID;

    public Lot(){
        id = "";
        name = "";
        location = "";
        price = 0.00;
        longi = 0.00;
        lat = 0.00;
        temp = 0.00;
        capacity = 0;
        avail = 0;
        dist = 0.00;
        imageID =0;
    }

    public Lot(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readString();
        price = in.readDouble();
        longi = in.readDouble();
        lat = in.readDouble();
        temp = in.readDouble();
        capacity = in.readInt();
        avail = in.readInt();
        dist = in.readDouble();
        imageID = in.readInt();
    }

    /*Comparator for sorting the list by roll no*/
    public static Comparator<Lot> distanceComparator = new Comparator<Lot>() {

        public int compare(Lot lot1, Lot lot2) {
            double dist1 = lot1.getDist();
            double dist2 = lot2.getDist();
            double result = dist1-dist2;
	   /*For ascending order*/
            return (int)result;
        }};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) { this.lat = lat; }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getDist() { return dist;}

    public void setDist(double dist) { this.dist = dist; }

    public int getImageID() { return imageID;}

    public void setImageID(int imageID) {this.imageID = imageID; }

    public int getCapacity() { return  capacity; }

    public void setCapacity(int capacity) {this.capacity = capacity; }

    public int getAvail() {return avail; }

    public void setAvail (int avail) {this.avail = avail; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeDouble(price);
        dest.writeDouble(longi);
        dest.writeDouble(lat);
        dest.writeDouble(temp);
        dest.writeInt(capacity);
        dest.writeInt(avail);
        dest.writeDouble(dist);
        dest.writeInt(imageID);
    }

    public static final Parcelable.Creator<Lot> CREATOR = new Parcelable.Creator<Lot>()
    {
        public Lot createFromParcel(Parcel in)
        {
            return new Lot(in);
        }
        public Lot[] newArray(int size)
        {
            return new Lot[size];
        }
    };

}
