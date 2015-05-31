package info.knorrium.equaltime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import info.knorrium.equaltime.data.TimeTableContract.EventEntry;
/**
 * Created by felipek on 4/4/15.
 */
public class TimeTableDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "weather.db";

    public TimeTableDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " (" +
            EventEntry._ID + " INTEGER PRIMARY KEY, " +
            EventEntry.COLUMN_EVENT_DATE + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EVENT_DURATION + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EVENT_COORD_LAT + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EVENT_COORD_LONG + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EVENT_CREATOR + " TEXT NOT NULL, " +
            EventEntry.COLUMN_EVENT_TITLE + " TEXT NOT NULL " +
            " );";
        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}