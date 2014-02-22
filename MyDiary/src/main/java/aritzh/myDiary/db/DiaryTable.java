package aritzh.myDiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.common.collect.Lists;

import org.joda.time.LocalDate;

import java.net.MulticastSocket;
import java.util.List;

import aritzh.myDiary.MainActivity;
import aritzh.myDiary.diary.Entry;
import aritzh.myDiary.util.MiscUtil;

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
        values.put(COLUMN_DATE, MiscUtil.dateToString(entry.getDate()));
        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_ENTRY_MESSAGE, entry.getMessage());
        return values;
    }

    public static Entry getEntry(LocalDate date, SQLiteDatabase db) {

        String[] projection = {
                _ID,
                COLUMN_DATE,
                COLUMN_TITLE,
                COLUMN_ENTRY_MESSAGE
        };

        String sortOrder = COLUMN_DATE + " DESC";

        String dateString1 = MiscUtil.dateToString(date);
        Log.d(MainActivity.LOG_TAG, "Looking for entry(ies) with date " + dateString1);

        Cursor c = db.query(
                TABLE_NAME,                         // The table to query
                projection,                         // The columns to return
                COLUMN_DATE + " = ?",                // The columns for the WHERE clause
                new String[]{dateString1},  // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                sortOrder                           // order by date, just in case
        );

        if (c == null || !c.moveToFirst() || c.getCount() == 0) return null;

        if (c.getCount() > 1)
            Log.e(MainActivity.LOG_TAG, "There have been found more than one row with the same date!"); // TODO Tell the user there is something wrong with the DB

        String message = c.getString(c.getColumnIndexOrThrow(COLUMN_ENTRY_MESSAGE));
        String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));

        final String dateString = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE));
        final LocalDate foundDate = MiscUtil.parseDate(dateString);
        if (date.equals(foundDate)) return new Entry(foundDate, message, title);
        else Log.e(MainActivity.LOG_TAG, "The input date and the entry found don't match dates!");

        return null;
    }

    public static void putEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);
        long newRowID = db.insert(TABLE_NAME, "null", values);
        Log.d(MainActivity.LOG_TAG, "Inserted row with ID = " + newRowID);
    }

    public static boolean updateEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);

        int affected = db.update(TABLE_NAME, values, COLUMN_DATE + " = ?", new String[]{MiscUtil.dateToString(entry.getDate())});
        if (affected > 1)
            throw new DBEntryDuplicatedException("Found multiple rows matching date " + MiscUtil.dateToString(entry.getDate()));
        Log.d(MainActivity.LOG_TAG, "Updated " + affected + " rows");
        return affected != 0;
    }

    public static void removeEntry(LocalDate date, SQLiteDatabase db) {
        String selection = COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = {MiscUtil.dateToString(date)};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public static void initDB(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    public static List<Entry> getAllEntries(SQLiteDatabase db) {
        String[] projection = {
                _ID,
                COLUMN_DATE,
                COLUMN_TITLE,
                COLUMN_ENTRY_MESSAGE
        };

        String sortOrder = COLUMN_DATE + " DESC";

        Cursor c = db.query(
                TABLE_NAME, // The table to query
                projection, // The columns to return
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                sortOrder   // order by date, just in case
        );

        List<Entry> entries = Lists.newArrayList();

        Log.d(MainActivity.LOG_TAG, "Found " + c.getCount() + " matches");
        if (c == null || !c.moveToFirst() || c.getCount() == 0) return entries;

        do {
            final String dateString = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE));
            LocalDate date = MiscUtil.parseDate(dateString);
            final String message = c.getString(c.getColumnIndexOrThrow(COLUMN_ENTRY_MESSAGE));
            final String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));
            Entry entry = new Entry(date, message, title);
            entries.add(entry);
        } while (c.moveToNext());


        return entries;
    }

    public static boolean isEntryPresent(LocalDate date, SQLiteDatabase db) {
        String[] projection = {
                _ID,
                COLUMN_DATE
        };

        String dateString = MiscUtil.dateToString(date);
        Log.d(MainActivity.LOG_TAG, "Looking for entry(ies) with date " + dateString);

        Cursor c = db.query(
                TABLE_NAME,                                 // The table to query
                projection,                                 // The columns to return
                COLUMN_DATE + "= ?",                        // The columns for the WHERE clause
                new String[]{dateString},                   // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // order by date, just in case
        );

        return c != null && c.moveToFirst() && c.getCount() > 0;
    }

    public void addAll(List<Entry> entries, SQLiteDatabase db) {
        for (Entry e : entries) {
            putEntry(e, db);
        }
    }

    public void clear(SQLiteDatabase db) {
        initDB(db);
    }
}
