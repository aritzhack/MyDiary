package aritzh.myDiary.db;

import android.provider.BaseColumns;

/**
 * Enum so that it cannot be instantiated (it is a static utility class after all)
 * Created by aritzh on 18/02/14.
 */
public enum DBEntryRow {
    ;

    public static class TableData implements BaseColumns {
        public static final String TABLE_NAME = "Diary";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "entryTitle";
        public static final String COLUMN_ENTRY_MESSAGE = "entryMessage";
    }

    public static class Queries {
        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TableData.TABLE_NAME + " (" +
                        TableData._ID + " INTEGER PRIMARY KEY," +
                        TableData.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                        TableData.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                        TableData.COLUMN_ENTRY_MESSAGE + TEXT_TYPE + COMMA_SEP + " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TableData.TABLE_NAME;
    }
}
