package info.knorrium.equaltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by felipek on 4/4/15.
 */
public class Utility {

    public static String getSavedName(Context context, int person) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String name;
        switch (person) {
            case 1:
                name = prefs.getString(context.getString(R.string.pref_first_name_key),
                        context.getString(R.string.pref_first_name_default));
                break;
            case 2:
                name = prefs.getString(context.getString(R.string.pref_second_name_key),
                        context.getString(R.string.pref_second_name_default));
                break;
            default:
                name = "Name not found";
        }
        return name;
    }
}
