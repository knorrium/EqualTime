package info.knorrium.equaltime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

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

        Button btnTimer1 = (Button) rootView.findViewById(R.id.btnTimer1);

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
                }
            }
        });

        return rootView;
    }
}
