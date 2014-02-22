package aritzh.myDiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aritzh on 18/02/14.
 */
@SuppressWarnings("UnusedDeclaration")
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1; // TODO If schema is changed, increase the database version
    public static final String DATABASE_NAME = "Diary.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DiaryTable.initDB(db);
        MetaTable.initDB(db);
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