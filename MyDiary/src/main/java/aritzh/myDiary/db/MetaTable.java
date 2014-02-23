package aritzh.myDiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by aritzh on 20/02/14.
 */
public class MetaTable implements BaseColumns, SQLConstants {

    public static final String TABLE_NAME = "Meta";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String COLUMN_FIELD = "field";
    public static final String COLUMN_VALUE = "value";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_FIELD + TEXT_TYPE + COMMA_SEP +
                    COLUMN_VALUE + TEXT_TYPE + " )";

    private MetaTable() {

    }

    public static void initDB(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    public static String getProperty(String property, SQLiteDatabase db) {
        String[] projection = {COLUMN_FIELD, COLUMN_VALUE};

        String sortOrder = COLUMN_FIELD + " DESC";

        Cursor c = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The columns to return
                COLUMN_FIELD + " = ?",           // The columns for the WHERE clause
                new String[]{property}, // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // order by date, just in case
        );

        if (c.getCount() > 1)
            throw new DBEntryDuplicatedException("There have been found more than one property with the same key!"); // TODO Tell the user there is something wrong with the DB
        if (c.getCount() == 0) return null;

        c.moveToFirst();
        return c.getString(c.getColumnIndexOrThrow(COLUMN_VALUE));
    }

    public static void setProperty(String property, String value, SQLiteDatabase db) {
        ContentValues values = getValuesForPropery(property, value);
        long newRowID = db.insert(TABLE_NAME, null, values);
    }

    private static ContentValues getValuesForPropery(String property, String value) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIELD, property);
        values.put(COLUMN_VALUE, value);
        return values;
    }

    public static boolean updateProperty(String property, String newValue, SQLiteDatabase db) {

        int affected = db.update(TABLE_NAME, getValuesForPropery(property, newValue), COLUMN_FIELD + " = " + property, null);
        if (affected > 1)
            throw new DBEntryDuplicatedException("Found multiple rows matching property " + property);
        return affected != 0;
    }

    public static void clear(SQLiteDatabase db) {
        initDB(db);
    }

    public static boolean isPropertyPresent(String property, SQLiteDatabase db) {
        return getProperty(property, db) != null;
    }
}
