package info.knorrium.equaltime;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import info.knorrium.equaltime.data.MyPlace;
import info.knorrium.equaltime.data.TimeTableContract;

/**
 * Created by felipek on 4/3/15.
 */
public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EVENT_LOADER = 0;
    private TimeTableAdapter mTimeTableAdapter;
    private static final int REQUEST_PLACE_PICKER = 1;

    private static final String[] EVENT_COLUMNS = {
            TimeTableContract.EventEntry.TABLE_NAME + "." + TimeTableContract.EventEntry._ID,
            TimeTableContract.EventEntry.COLUMN_EVENT_DATE,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LAT,
            TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LONG,
            TimeTableContract.EventEntry.COLUMN_EVENT_DURATION,
            TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR
    };

    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_DATE = 1;
    static final int COL_EVENT_COORD_LAT = 2;
    static final int COL_EVENT_COORD_LONG = 3;
    static final int COL_EVENT_DURATION = 4;
    static final int COL_EVENT_CREATOR = 5;

    private MyPlace place;
    private long startTime = 0;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("onActivtyResult", "requestCode: " + requestCode + " - resultCode: " + resultCode + " - " + data.toString());
        //V/MainActivity( 7285): onActivityResult: 65537 - -1 - Intent { act=com.google.android.gms.location.places.ui.PICK_PLACE pkg=com.google.android.gms cmp=com.google.android.gms/com.google.android.location.places.ui.placepicker.PlacePickerActivity (has extras) }
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (!data.toString().isEmpty()) {
                    Log.v(LOG_TAG, "onActivityResult: " + requestCode + " - " + resultCode + " - " + data.toString());
                    Place place = PlacePicker.getPlace(data, this.getActivity());
                    String toastMsg = String.format("Place: %s", place.getName());
                    this.place = new MyPlace(place);
                    populatePlace();
                } else {
                    //TODO: Fix the bug where an entry can be added without picking a location first
                    Toast.makeText(this.getActivity(), "No Place selected", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

//        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = TimeTableContract.EventEntry.COLUMN_EVENT_DATE + " ASC";
//        Uri weatherForLocationUri = TimeTableContract.EventEntry.buildEventUri(1);
        Uri contenturi = Uri.parse("content://info.knorrium.equaltime");
        Uri tableuri = Uri.withAppendedPath(contenturi,TimeTableContract.EventEntry.TABLE_NAME);

        return new CursorLoader(getActivity(), tableuri, EVENT_COLUMNS, null, null, sortOrder);
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mTimeTableAdapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mTimeTableAdapter.swapCursor(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EVENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v("PlaceHolderFragment", "onPause - startTime -" + startTime);
        if (place != null) {
            Log.v("PlaceHolderFragment", "onPause - Place: " + place.toString());

        }

        getActivity().getSupportFragmentManager().findFragmentByTag("TFTAG").setRetainInstance(true);
    }

    public void onResume() {
        super.onResume();
        Log.v("PlaceHolderFragment", "onResume");
        getActivity().getSupportFragmentManager().findFragmentByTag("TFTAG").getRetainInstance();
        if (place != null) {
            Log.v("PlaceHolderFragment", "onResume - Place: " + place.toString());
            populatePlace();
        }
        populateTimer(getView());
        Log.v("PlaceHolderFragment", "onResume startTime - " + startTime);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        Log.v("PlaceholderFragment", "onSaveInstanceState -> startTime: " + startTime);
        Log.v("PlaceholderFragment", "onSaveInstanceState -> place: " + place);
        savedInstanceState.putSerializable("place", place);

        savedInstanceState.putLong("startTime", startTime);
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        Log.v("PlaceholderFragment", "onRestoreInstanceState -> startTime: " + savedInstanceState.getLong("startTime", startTime));
//
//    }

    // since we read the location when we create the loader, all we need to do is restart things
    void onLocationChanged( ) {
//        updateWeather();
        getLoaderManager().restartLoader(EVENT_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_refresh) {
//            updateWeather();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void populateTimer(View v) {
        Button btnTimer = (Button) v.findViewById(R.id.btnTimer1);
        Chronometer timer1 = (Chronometer) v.findViewById(R.id.chronometer1);

        if (this.startTime > 0) {
            btnTimer.setText(getResources().getString(R.string.btn_timer_stop));
            timer1.setBase(this.startTime);
            timer1.start();
        } else {
            Log.v("populateTimer", getString(R.string.btn_timer_start));
            Log.v("populateTimer", getResources().getString(R.string.btn_timer_start));
            btnTimer.setText(getResources().getString(R.string.btn_timer_start));
        }

        btnTimer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                View rootView = v.getRootView();
                Chronometer timer1 = (Chronometer) rootView.findViewById(R.id.chronometer1);

                int id = ((RadioGroup) rootView.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();

                TextView txtPlace = (TextView) rootView.findViewById(R.id.txtPlaceDetails);
                if (id == -1){
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a person", Toast.LENGTH_SHORT).show();
                }

                else if (txtPlace.getVisibility() == View.GONE) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a place", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (button.getText().equals("Start")) {
                        startTime = SystemClock.elapsedRealtime();
                        timer1.setBase(startTime);
                        timer1.start();

                        Toast.makeText(getActivity().getApplicationContext(), "Timer started", Toast.LENGTH_SHORT).show();
                        button.setText("Stop");
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Timer stopped", Toast.LENGTH_SHORT).show();
                        button.setText("Start");
                        timer1.stop();
                        startTime = 0;

                        ContentValues values = new ContentValues();
                        String name = "";
                        id = ((RadioGroup) rootView.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
                        if (id == R.id.rdName1) {
                            name = ((RadioButton) rootView.findViewById(R.id.rdName1)).getText().toString();
                        } else if (id == R.id.rdName2) {
                            name = ((RadioButton) rootView.findViewById(R.id.rdName2)).getText().toString();
                        }
                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR, name);

                        Date today = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM, d");
                        String todayText = sdf.format(today);

                        TextView txtLat = (TextView) rootView.findViewById(R.id.txtLat);
                        TextView txtLon = (TextView) rootView.findViewById(R.id.txtLon);

                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_DATE, todayText);
                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LAT, String.valueOf(txtLat.getText()));
                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LONG, String.valueOf(txtLon.getText()));
                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_DURATION, timer1.getText().toString());
                        values.put(TimeTableContract.EventEntry.COLUMN_EVENT_TITLE, txtPlace.getText().toString());

                        Uri insertedUri = getActivity().getApplicationContext().getContentResolver().insert(TimeTableContract.EventEntry.CONTENT_URI, values);
                        long eventId = ContentUris.parseId(insertedUri);
                        timer1.setBase(SystemClock.elapsedRealtime());
                    }
                }
            }
        });
    }

    private void populatePlace() {
        double lat = place.getLat();
        TextView txtLat = (TextView) getActivity().findViewById(R.id.txtLat);
        txtLat.setText(String.valueOf(lat));
        txtLat.setVisibility(View.GONE);

        double lon = place.getLon();
        TextView txtLon = (TextView) getActivity().findViewById(R.id.txtLon);
        txtLon.setText(String.valueOf(lon));
        txtLon.setVisibility(View.GONE);

//        Toast.makeText(this.getActivity(), toastMsg, Toast.LENGTH_LONG).show();
        TextView txtPlace = (TextView) getActivity().findViewById(R.id.txtPlaceDetails);
        txtPlace.setVisibility(View.VISIBLE);
        txtPlace.setText(place.getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTimeTableAdapter = new TimeTableAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        RadioButton name1radio = (RadioButton) rootView.findViewById(R.id.rdName1);
        String strName1 = prefs.getString(context.getString(R.string.pref_first_name_key), context.getString(R.string.pref_first_name_default));
        name1radio.setText(strName1);

        RadioButton name2radio = (RadioButton) rootView.findViewById(R.id.rdName2);
        String strName2 = prefs.getString(context.getString(R.string.pref_second_name_key), context.getString(R.string.pref_second_name_default));
        name2radio.setText(strName2);

        Button btnLocation = (Button) rootView.findViewById(R.id.btnLocation);

        TextView txtPlace = (TextView) rootView.findViewById(R.id.txtPlaceDetails);
        txtPlace.setVisibility(View.GONE);

        btnLocation.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               // Construct an intent for the place picker
                                               try {
                                                   PlacePicker.IntentBuilder intentBuilder =
                                                           new PlacePicker.IntentBuilder();
                                                   Intent intent = intentBuilder.build(getActivity());
                                                   // Start the intent by requesting a result,
                                                   // identified by a request code.
                                                   startActivityForResult(intent, REQUEST_PLACE_PICKER);

                                               } catch (GooglePlayServicesRepairableException e) {
                                                   // ...
                                               } catch (GooglePlayServicesNotAvailableException e) {
                                                   // ...
                                               }
                                           }
                                       }
        );

        populateTimer(rootView);

        ListView listView = (ListView) rootView.findViewById(R.id.listEntries1);
        listView.setAdapter(mTimeTableAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(TimeTableContract.EventEntry.buildEventUri(cursor.getLong(COL_EVENT_ID)));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
