package aritzh.myDiary.db;

import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by aritzh on 18/02/14.
 */
public class DBUpdaterHelper {

    public static final Map<Integer, DBUpdater> updaters = Maps.newHashMap();

    public static void update(SQLiteDatabase oldDB, int oldVersion, int newVersion) {
        int DBVersion = oldVersion;

        while (DBVersion != newVersion) {
            DBUpdater updater = updaters.get(DBVersion);
            if (updater == null)
                throw new DBUpdateImpossibleException(oldVersion, newVersion, DBVersion);
            updater.update(oldDB);
            DBVersion = updater.newDBVersion;
        }
        DBUpdater updater = DBUpdaterHelper.updaters.get(oldVersion);
    }

    public static abstract class DBUpdater {
        public final int oldDBVersion;
        public final int newDBVersion;

        public DBUpdater(int oldDBVersion, int newDBVersion) {
            this.oldDBVersion = oldDBVersion;
            this.newDBVersion = newDBVersion;
            DBUpdaterHelper.updaters.put(this.oldDBVersion, this);
        }

        public abstract void update(SQLiteDatabase oldDB);
    }
}
