package aritzh.myDiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import aritzh.myDiary.MainActivity;
import aritzh.myDiary.diary.Entry;
import aritzh.myDiary.util.Date;

import static aritzh.myDiary.db.DBEntryRow.Queries;
import static aritzh.myDiary.db.DBEntryRow.TableData;

/**
 * Created by aritzh on 18/02/14.
 */
@SuppressWarnings("UnusedDeclaration")
public class DBDiaryHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1; // TODO If schema is changed, increase the database version
    public static final String DATABASE_NAME = "Diary.db";

    private SQLiteDatabase cachedDatabase;

    public DBDiaryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static ContentValues getValuesFromEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(TableData.COLUMN_DATE, String.valueOf(entry.getDate().getUnixTime()));
        values.put(TableData.COLUMN_TITLE, entry.getTitle());
        values.put(TableData.COLUMN_ENTRY_MESSAGE, entry.getMessage());
        return values;
    }

    public static void putEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);
        long newRowID = db.insert(TableData.TABLE_NAME, "null", values);
    }

    public static boolean updateEntry(Entry entry, SQLiteDatabase db) {
        ContentValues values = getValuesFromEntry(entry);

        int affected = db.update(TableData.TABLE_NAME, values, TableData.COLUMN_DATE + " = " + entry.getDate().toUnixString(), null);
        if (affected > 1)
            throw new DBEntryDuplicatedException("Found multiple rows matching date " + entry.getDate().toString() + " (" + entry.getDate().toUnixString() + ")");
        return affected != 0;
    }

    public static Entry getEntry(Date date, SQLiteDatabase db) {

        String[] projection = {
                TableData._ID,
                TableData.COLUMN_DATE,
                TableData.COLUMN_TITLE,
                TableData.COLUMN_ENTRY_MESSAGE
        };

        String sortOrder = TableData.COLUMN_DATE + " DESC";

        Cursor c = db.query(
                TableData.TABLE_NAME,               // The table to query
                projection,                         // The columns to return
                TableData.COLUMN_DATE,              // The columns for the WHERE clause
                new String[]{date.toUnixString()},  // The values for the WHERE clause
                null,                               // don't group the rows
                null,                               // don't filter by row groups
                sortOrder                           // order by date, just in case
        );

        if (c.getColumnCount() > 1)
            Log.e(MainActivity.LOG_TAG, "There have been found more than one row with the same date!"); // TODO Tell the user there is something wrong with the DB

        if (c.getColumnCount() == 0) return null;

        c.moveToFirst();
        String message = c.getString(c.getColumnIndexOrThrow(TableData.COLUMN_ENTRY_MESSAGE));
        String title = c.getString(c.getColumnIndexOrThrow(TableData.COLUMN_TITLE));
        String unixDate = c.getString(c.getColumnIndexOrThrow(TableData.COLUMN_DATE));

        if (!date.toUnixString().equals(unixDate))
            Log.e(MainActivity.LOG_TAG, "The input date and the entry found don't match dates!");

        return new Entry(Date.parseUnix(unixDate), message, title);
    }

    public static void removeEntry(Date date, SQLiteDatabase db) {
        String selection = TableData.COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = {date.toUnixString()};
        db.delete(TableData.TABLE_NAME, selection, selectionArgs);
    }

    public void begin() {
        this.cachedDatabase = this.getWritableDatabase();
    }

    public void finish() {
        this.cachedDatabase = null;
    }

    public boolean updateEntry(Entry entry) {
        this.checkCachedDBIsAvailable();
        return DBDiaryHelper.updateEntry(entry, this.getWritableDatabase());
    }

    private void checkCachedDBIsAvailable() {
        if (this.cachedDatabase == null)
            throw new IllegalStateException("\"begin()\" must be called before calling methods that modify the DB or the \"finish()\" method");
    }

    public void clear() {
        this.checkCachedDBIsAvailable();
        this.onCreate(this.getWritableDatabase());
    }

    public void addAll(List<Entry> entries) {
        this.checkCachedDBIsAvailable();
        for (Entry e : entries) {
            DBDiaryHelper.putEntry(e, this.cachedDatabase);
        }
    }

    public Entry getEntry(Date date) {
        this.checkCachedDBIsAvailable();
        return DBDiaryHelper.getEntry(date, this.getReadableDatabase());
    }

    public boolean isEntryPresent(Date date) {
        this.checkCachedDBIsAvailable();
        return this.getEntry(date) != null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Queries.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DBUpdaterHelper.update(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade(db, oldVersion, newVersion); // TODO Warn users whether they want to downgrade or not, warning of data loss
    }

}