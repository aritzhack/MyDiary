package aritzh.myDiary.db;

/**
 * Created by aritzh on 19/02/14.
 */
public class DBEntryDuplicatedException extends DBOperationException {
    public DBEntryDuplicatedException() {
    }

    public DBEntryDuplicatedException(String detailMessage) {
        super(detailMessage);
    }

    public DBEntryDuplicatedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DBEntryDuplicatedException(Throwable throwable) {
        super(throwable);
    }
}
