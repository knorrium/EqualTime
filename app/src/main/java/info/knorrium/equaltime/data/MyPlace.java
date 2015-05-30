package info.knorrium.equaltime.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by felipek on 5/30/15.
 */
public class MyPlace implements Serializable {
    private double lat;
    private double lon;
    private String id;
    private CharSequence address;
    private CharSequence name;


    public MyPlace(Place p) {
        this.id = p.getId();
        this.address = p.getAddress();
        this.name = p.getName();
        this.lat = p.getLatLng().latitude;
        this.lon = p.getLatLng().longitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getId() {
        return id;
    }

    public CharSequence getName() {
        return name;
    }

    public CharSequence getAddress() {
        return address;
    }
}
