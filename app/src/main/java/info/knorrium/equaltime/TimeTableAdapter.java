package info.knorrium.equaltime;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by felipek on 4/4/15.
 */
public class TimeTableAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final TextView entryIdView;
        public final TextView creatorView;
        public final TextView dateView;
        public final TextView timespentView;
        public final TextView latitudeView;
        public final TextView longitudeView;

        public ViewHolder(View view) {
            entryIdView = (TextView) view.findViewById(R.id.entryIdView);
            creatorView = (TextView) view.findViewById(R.id.creatorView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            timespentView = (TextView) view.findViewById(R.id.timespentView);
            latitudeView = (TextView) view.findViewById(R.id.latitudeView);
            longitudeView = (TextView) view.findViewById(R.id.longitudeView);
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

        viewHolder.entryIdView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_ID));
        viewHolder.latitudeView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_COORD_LAT));
        viewHolder.longitudeView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_COORD_LONG));
        viewHolder.dateView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_DATE));
        viewHolder.timespentView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_DURATION));
        viewHolder.creatorView.setText(cursor.getString(PlaceholderFragment.COL_EVENT_CREATOR));

    }
}
