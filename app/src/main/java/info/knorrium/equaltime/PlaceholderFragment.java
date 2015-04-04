package info.knorrium.equaltime;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import info.knorrium.equaltime.data.TimeTableContract;

/**
 * Created by felipek on 4/3/15.
 */
public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EVENT_LOADER = 0;
    private TimeTableAdapter mTimeTableAdapter;

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTimeTableAdapter = new TimeTableAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        TextView name1view = (TextView) rootView.findViewById(R.id.txtName1);
        String strName1 = prefs.getString(context.getString(R.string.pref_first_name_key), context.getString(R.string.pref_first_name_default));
        name1view.setText(strName1);

        TextView name2view = (TextView) rootView.findViewById(R.id.txtName2);
        String strName2 = prefs.getString(context.getString(R.string.pref_second_name_key), context.getString(R.string.pref_second_name_default));
        name2view.setText(strName2);

        Button btnTimer1 = (Button) rootView.findViewById(R.id.btnTimer1);

        ListView listView = (ListView) rootView.findViewById(R.id.listEntries1);
        listView.setAdapter(mTimeTableAdapter);

        btnTimer1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                View rootView = v.getRootView();
                Chronometer timer1 = (Chronometer) rootView.findViewById(R.id.chronometer1);

                if (button.getText().equals("Start")) {
                    timer1.setBase(SystemClock.elapsedRealtime());
                    timer1.start();

                    Toast.makeText(getActivity().getApplicationContext(), "Timer started", Toast.LENGTH_SHORT).show();
                    button.setText("Stop");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Timer stopped", Toast.LENGTH_SHORT).show();
                    button.setText("Start");
                    timer1.stop();

                    ContentValues values = new ContentValues();

                    values.put(TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR, Utility.getSavedName(v.getContext(), 1));


                    Date today = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM, d");
                    String todayText = sdf.format(today);

                    values.put(TimeTableContract.EventEntry.COLUMN_EVENT_DATE, todayText);
                    values.put(TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LAT, "123");
                    values.put(TimeTableContract.EventEntry.COLUMN_EVENT_COORD_LONG, "456");
                    values.put(TimeTableContract.EventEntry.COLUMN_EVENT_DURATION, "789");

                    Uri insertedUri = getActivity().getApplicationContext().getContentResolver().insert(TimeTableContract.EventEntry.CONTENT_URI, values);
                    long eventId = ContentUris.parseId(insertedUri);



                }
            }
        });

        return rootView;
    }
}
