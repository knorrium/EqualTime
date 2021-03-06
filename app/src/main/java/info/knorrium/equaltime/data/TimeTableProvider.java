package info.knorrium.equaltime.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by felipek on 4/4/15.
 */
public class TimeTableProvider extends ContentProvider {
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private TimeTableDbHelper mOpenHelper;

    static final int EVENT = 100;
    static final int EVENT_WITH_ID = 101;

    public static final SQLiteQueryBuilder sEventQueryBuilder;

    static {
        sEventQueryBuilder = new SQLiteQueryBuilder();
        sEventQueryBuilder.setTables(
                TimeTableContract.EventEntry.TABLE_NAME
        );
    }

    public static final String sEventOwnerSelection =
            TimeTableContract.EventEntry.TABLE_NAME +
                "." + TimeTableContract.EventEntry.COLUMN_EVENT_CREATOR + " = ? ";

    public static final String sEventIdSelection =
            TimeTableContract.EventEntry.TABLE_NAME +
                    "." + TimeTableContract.EventEntry._ID + " = ? ";

    private Cursor getEventById(Uri uri, String[] projection, String sortOrder) {
        String selectedId = TimeTableContract.EventEntry.getEventFromUri(uri);
        String[] selectionArgs;
        String selection;

        return sEventQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sEventIdSelection,
                new String[]{selectedId},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(TimeTableContract.CONTENT_AUTHORITY, TimeTableContract.PATH_EVENT, EVENT);
        matcher.addURI(TimeTableContract.CONTENT_AUTHORITY, TimeTableContract.PATH_EVENT + "/*", EVENT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TimeTableDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENT:
                return TimeTableContract.EventEntry.CONTENT_ITEM_TYPE;
            case EVENT_WITH_ID:
                return TimeTableContract.EventEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "event"
            case EVENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(TimeTableContract.EventEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            // "event/*"
            case EVENT_WITH_ID: {
                retCursor = getEventById(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case EVENT: {
                long _id = db.insert(TimeTableContract.EventEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TimeTableContract.EventEntry.buildEventUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deleted = 0;

        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case EVENT: {
                deleted = db.delete(TimeTableContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updated = 0;

        switch (match) {
            case EVENT: {
                updated = db.update(TimeTableContract.EventEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
