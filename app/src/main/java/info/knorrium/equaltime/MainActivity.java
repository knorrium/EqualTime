package info.knorrium.equaltime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import info.knorrium.equaltime.data.TimeTableContract;

public class MainActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String TIME_FRAGMENT_TAG = "TFTAG";
    static final int NAME_SETTINGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (VERBOSE) {
            Log.v(LOG_TAG, "+++ ON CREATE +++");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), TIME_FRAGMENT_TAG)
                    .commit();
        } else {
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, NAME_SETTINGS);
        } else if (id == R.id.action_erase) {
            getContentResolver().delete(TimeTableContract.EventEntry.CONTENT_URI, null, null);
        }

        return super.onOptionsItemSelected(item);
    }

    private static final boolean VERBOSE = true;

    @Override
    public void onStart() {
        super.onStart();
        if (VERBOSE) {
            Log.v(LOG_TAG, "++ ON START ++");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
            PlaceholderFragment pf = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(TIME_FRAGMENT_TAG);
            if (null != pf) {

            }
        }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, savedInstanceState.toString());
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        long startTime = savedInstanceState.getLong("startTime");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        recreate();
    }
}
