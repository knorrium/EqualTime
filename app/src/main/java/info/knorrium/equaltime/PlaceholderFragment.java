package info.knorrium.equaltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by felipek on 4/3/15.
 */
public class PlaceholderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        TextView name1view = (TextView) rootView.findViewById(R.id.txtName1);
        String strName1 = prefs.getString(context.getString(R.string.pref_first_name_key), context.getString(R.string.pref_first_name_default));
        name1view.setText(strName1);

        TextView name2view = (TextView) rootView.findViewById(R.id.txtName2);
        String strName2 = prefs.getString(context.getString(R.string.pref_second_name_key), context.getString(R.string.pref_second_name_default));
        name2view.setText(strName2);

        return rootView;
    }
}
