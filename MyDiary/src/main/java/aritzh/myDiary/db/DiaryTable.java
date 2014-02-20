package aritzh.myDiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import aritzh.myDiary.MainActivity;
import aritzh.myDiary.diary.Entry;
import aritzh.myDiary.util.Date;

/**
 * Created by aritzh on 20/02/14.
 */
public class DiaryTable implements BaseColumns, SQLConstants {

    public static final String TABLE_NAME = "Diary";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TITLE = "entryTitle";
    public static final String COLUMN_ENTRY_MESSAGE = "entryMessage";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_ENTRY_MESSAGE + TEXT_TYPE + " )";

    private DiaryTable() {

    }

    public static ContentValues getValuesFromEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, String.valueOf(entry.getDate().getUnixTime()));
        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_ENTRY_MESSAGE, entry.getMessage());
        return values;
    }

    public static Entry getEntry(Date date, SQLiteDatabase db) {

        String[] projection = {
                _ID,
                COLUMN_DATE,
                COLUMN_TITLE,
                COLUMN_ENTRY_MESSAGE
        };

        String sortOrder = COLUMN_DATE + " DESC";

        Cursor c = db.query(
                TABLE_NAME,               // The table to query
                projection,                         // The columns to return
                COLUMN_DATE,              // The columns for the WHERE clause
                new String[]{date.toUnixString()},  // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                sortOrder                           // order by date, just in case
        );

        if (c.getColumnCount() > 1)
            Log.e(MainActivity.LOG_TAG, "There have been found more than one row with the same date!"); // TODO Tell the user there is something wrong with the DB

        if (c.getColumnCount() == 0) return null;

        c.moveToFirst();
        String message = c.getString(c.getColumnIndexOrThrow(COLUMN_ENTRY_MESSAGE));
        String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));
        String unixDate = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE));

        if (!date.toUnixString().equals(unixDate))
            Log.e(MainActivity.LOG_TAG, "The input date and the entry found don't match dates!");

        return new Entry(Date.parseUnix(unixDate), message, title);
    }

    public static void putEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);
        long newRowID = db.insert(TABLE_NAME, "null", values);
    }

    public static boolean updateEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);

        int affected = db.update(TABLE_NAME, values, COLUMN_DATE + " = " + entry.getDate().toUnixString(), null);
        if (affected > 1)
            throw new DBEntryDuplicatedException("Found multiple rows matching date " + entry.getDate().toString() + " (" + entry.getDate().toUnixString() + ")");
        return affected != 0;
    }

    public static void removeEntry(Date date, SQLiteDatabase db) {
        String selection = COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = {date.toUnixString()};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void addAll(List<Entry> entries, SQLiteDatabase db) {
        for (Entry e : entries) {
            putEntry(e, db);
        }
    }

    public void clear(SQLiteDatabase db) {
        initDB(db);
    }

    public boolean isEntryPresent(Date date, SQLiteDatabase db) {
        return getEntry(date, db) != null;
    }

    public static void initDB(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }
}
