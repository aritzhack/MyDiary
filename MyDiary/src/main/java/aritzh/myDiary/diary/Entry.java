package aritzh.myDiary.diary;

import com.google.common.base.Preconditions;

import org.joda.time.LocalDate;

/**
 * Created by aritzh on 15/02/14.
 */
public class Entry {

    private final LocalDate date;
    private final String message;
    private final String title;

    public Entry(LocalDate date) {
        this(date, null, null);
    }

    public Entry(LocalDate date, CharSequence message) {
        this(date, message, null);
    }

    public Entry(LocalDate date, CharSequence message, String title) {
        Preconditions.checkArgument(date != null, "Date must not be null");
        this.date = date;
        this.message = message != null ? message.toString() : "";
        this.title = title != null ? title : "";
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
