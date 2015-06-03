package info.knorrium.equaltime;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by felipek on 4/4/15.
 */
public class TimeTableAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final TextView creatorView;
        public final TextView dateView;
        public final TextView timespentView;
        public final TextView placeView;

        public ViewHolder(View view) {
            creatorView = (TextView) view.findViewById(R.id.creatorView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            timespentView = (TextView) view.findViewById(R.id.timespentView);
            placeView = (TextView) view.findViewById(R.id.placeView);
        }
    }

    public TimeTableAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = R.layout.list_item_entry;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.dateView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_DATE));
        viewHolder.timespentView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_DURATION));
        viewHolder.creatorView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_CREATOR));
        viewHolder.placeView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_TITLE));
    }
}
