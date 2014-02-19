package aritzh.myDiary.db;

/**
 * Created by aritzh on 18/02/14.
 */
public class DBUpdateImpossibleException extends DBOperationException {

    public DBUpdateImpossibleException(int oldVersion, int newVersion, int updatedToVersion) {
        super("Couldn't update DB from versiob " + oldVersion + " to " + newVersion + " since there are no updaters for version " + updatedToVersion);
    }
}
