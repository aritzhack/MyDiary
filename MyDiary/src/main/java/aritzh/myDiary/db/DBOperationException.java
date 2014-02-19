package aritzh.myDiary.db;

/**
 * Created by aritzh on 19/02/14.
 */
public class DBOperationException extends RuntimeException {

    public DBOperationException() {
    }

    public DBOperationException(String detailMessage) {
        super(detailMessage);
    }

    public DBOperationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DBOperationException(Throwable throwable) {
        super(throwable);
    }
}
