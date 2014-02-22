package aritzh.myDiary.diary;

import com.google.common.base.Preconditions;

import aritzh.myDiary.util.Date;

/**
 * Created by aritzh on 15/02/14.
 */
public class Entry {

    private final Date date;
    private final String message;
    private final String title;

    public Entry(Date date) {
        this(date, null, null);
    }

    public Entry(Date date, CharSequence message) {
        this(date, message, null);
    }

    public Entry(Date date, CharSequence message, String title) {
        Preconditions.checkArgument(date != null, "Date must not be null");
        this.date = date;
        this.message = message != null ? message.toString() : "";
        this.title = title != null ? title : "";
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
