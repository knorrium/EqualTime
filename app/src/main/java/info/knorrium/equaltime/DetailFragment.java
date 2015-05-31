package info.knorrium.equaltime;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import info.knorrium.equaltime.data.TimeTableContract;

/**
 * Created by felipek on 4/4/15.
 */
public class DetailFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;

    private static final String[] EVENT_COLUMNS = {
            TimeTableContract.EventEntry.TABLE_NAME + "." + TimeTableContract.EventEntry._ID,
            TimeTableContract.EventEntry.COLUMN_EVENT_DATE,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LAT,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LONG,
            TimeTableContract.EventEntry.COLUMN_EVENT_DURATION,
            TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR,
            TimeTableContract.EventEntry.COLUMN_EVENT_TITLE
    };

    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_DATE = 1;
    static final int COL_EVENT_COORD_LAT = 2;
    static final int COL_EVENT_COORD_LONG = 3;
    static final int COL_EVENT_DURATION = 4;
    static final int COL_EVENT_CREATOR = 5;
    static final int COL_EVENT_TITLE = 6;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                EVENT_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        Log.v(LOG_TAG, "In onLoadFinished, DATA: " + data.toString());
        if (!data.moveToFirst()) { return; }

        TextView txtId = (TextView) getView().findViewById(R.id.detail_id);
        txtId.setText(data.getString(COL_EVENT_ID));

        TextView txtCreator = (TextView) getView().findViewById(R.id.detail_creator);
        txtCreator.setText(data.getString(COL_EVENT_CREATOR));

        TextView txtDate = (TextView) getView().findViewById(R.id.detail_date);
        txtDate.setText(data.getString(COL_EVENT_DATE));

        TextView txtDuration = (TextView) getView().findViewById(R.id.detail_duration);
        txtDuration.setText(data.getString(COL_EVENT_DURATION));

        TextView txtLat = (TextView) getView().findViewById(R.id.detail_latitude);
        txtLat.setText(data.getString(COL_EVENT_COORD_LAT));
        double dblLat = Double.valueOf(txtLat.getText().toString());

        TextView txtLong = (TextView) getView().findViewById(R.id.detail_longitude);
        txtLong.setText(data.getString(COL_EVENT_COORD_LONG));
        double dblLong = Double.valueOf(txtLong.getText().toString());

        MapView mapView = (MapView) getView().findViewById(R.id.mapview);
        mapView.getMap().addMarker(new MarkerOptions()
                .position(new LatLng(dblLat, dblLong))
                .title("New Marker")
                .snippet("Placeholder snippet"));
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .alpha(0.7f);

        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(dblLat, dblLong), 17);
        mapView.getMap().animateCamera(cameraUpdate);


//        GoogleMap map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

//        GoogleMap map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//        GoogleMap map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4, -122.1), 15));
//        map.animateCamera(CameraUpdateFactory.zoomIn());
//        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
////
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(37.4, -122.1))      // Sets the center of the map to Mountain View
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        MapView mapView = (MapView) getView().findViewById(R.id.map);
//
//        CameraPosition cameraPosition = new CameraPosition.Builder()
////                .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        GoogleMap map = ((SupportMapFragment) getView().findViewById(R.id.map)).getMap();
//        map.setMyLocationEnabled(true);
//
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//        CameraUpdate center=
//                CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044,
//                        -73.98180484771729));
//        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.detailfragment, menu);


    }

}
