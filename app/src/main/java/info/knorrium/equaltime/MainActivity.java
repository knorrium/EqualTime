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
    static final int NAME_SETTINGS = 0;



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //V/MainActivity( 7285): onActivityResult: 65537 - -1 - Intent { act=com.google.android.gms.location.places.ui.PICK_PLACE pkg=com.google.android.gms cmp=com.google.android.gms/com.google.android.location.places.ui.placepicker.PlacePickerActivity (has extras) }
        if (requestCode == 65537) {
            if (resultCode == RESULT_OK) {
                if (!data.toString().isEmpty()) {
                    Log.v(LOG_TAG, "onActivityResult: " + requestCode + " - " + resultCode + " - " + data.toString());
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());

                    double lat = place.getLatLng().latitude;
                    TextView txtLat = (TextView) findViewById(R.id.txtLat);
                    txtLat.setText(String.valueOf(lat));
                    txtLat.setVisibility(View.GONE);

                    double lon = place.getLatLng().longitude;
                    TextView txtLon = (TextView) findViewById(R.id.txtLon);
                    txtLon.setText(String.valueOf(lon));
                    txtLon.setVisibility(View.GONE);

                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    TextView txtPlace = (TextView) findViewById(R.id.txtPlaceDetails);
                    txtPlace.setText(place.getName());

                } else {
                    Toast.makeText(this, "No Place selected", Toast.LENGTH_LONG).show();
                }
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        recreate();
    }
}
