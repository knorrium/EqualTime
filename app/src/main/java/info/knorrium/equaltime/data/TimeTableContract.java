package info.knorrium.equaltime.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by felipek on 4/3/15.
 */
public class TimeTableContract {

    public static final String CONTENT_AUTHORITY = "info.knorrium.equaltime";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENT = "event";

    public static final class EventEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_EVENT_DATE = "event_date";

        public static final String COLUMN_EVENT_COORD_LAT = "event_lat";

        public static final String COLUMN_EVENT_COORD_LONG = "event_long";

        public static final String COLUMN_EVENT_DURATION = "event_duration";

        public static final String COLUMN_EVENT_CREATOR = "event_creator";

        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
