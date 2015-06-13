package info.knorrium.equaltime;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import info.knorrium.equaltime.data.TimeTableContract;

/**
 * Created by felipek on 4/4/15.
 */
public class DetailFragment  extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ShareActionProvider mShareActionProvider;
    private String mTimeString;
    private Uri mUri;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;
    static final String DETAIL_URI = "URI";

    private static final String[] EVENT_COLUMNS = {
            TimeTableContract.EventEntry.TABLE_NAME + "." + TimeTableContract.EventEntry._ID,
            TimeTableContract.EventEntry.COLUMN_EVENT_DATE,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LAT,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LONG,
            TimeTableContract.EventEntry.COLUMN_EVENT_DURATION,
            TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR,
            TimeTableContract.EventEntry.COLUMN_EVENT_TITLE
    };

    private String txtIdValue;
    private String txtDateValue;
    private String txtTitleValue;
    private String txtLatValue;
    private String txtLonValue;
    private String txtCreatorValue;
    private String txtDurationValue;

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
        if (null != mUri) {

        Intent intent = getActivity().getIntent();
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
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            txtIdValue = data.getString(COL_EVENT_ID);
            txtCreatorValue = data.getString(COL_EVENT_CREATOR);
            txtDateValue = data.getString(COL_EVENT_DATE);
            txtDurationValue = data.getString(COL_EVENT_DURATION);
            txtLatValue = data.getString(COL_EVENT_COORD_LAT);
            txtLonValue = data.getString(COL_EVENT_COORD_LONG);
            txtTitleValue = data.getString(COL_EVENT_TITLE);

            double dblLat = Double.valueOf(txtLatValue.toString());
            double dblLong = Double.valueOf(txtLonValue.toString());
            MapView mapView = (MapView) getView().findViewById(R.id.mapview);
            mapView.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(dblLat, dblLong))
                    .title(txtTitleValue)
                    .snippet("Time Spent: " + txtDurationValue));

            MapsInitializer.initialize(this.getActivity());

            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(dblLat, dblLong), 17);
            mapView.getMap().animateCamera(cameraUpdate);

            mTimeString = "Total time spent by " + txtCreatorValue.toString() +
                    " in " + txtDateValue.toString() +
                    " at " + txtTitleValue.toString() +
                    ": " + txtDurationValue.toString();

            TextView txtCardDetails = (TextView) getView().findViewById(R.id.txtCardDetails);
            txtCardDetails.setText(mTimeString);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        Bundle arguments = getArguments();
        if (arguments != null) {
            rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        } else {
            rootView = inflater.inflate(R.layout.landing, null, false);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the Share menu if not in two pane mode
        if (mUri != null) {
            inflater.inflate(R.menu.menu_details, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // If onLoadFinished happens before this, we can go ahead and set the share intent now.
            if (mTimeString != null) {
                mShareActionProvider.setShareIntent(createShareTimeIntent());
            }
        }
    }

    private Intent createShareTimeIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mTimeString);
        return shareIntent;
    }
}
