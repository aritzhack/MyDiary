package aritzh.myDiary.diary;

import com.google.common.base.Preconditions;

import org.joda.time.LocalDate;

import aritzh.myDiary.util.MiscUtil;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entry entry = (Entry) o;

        return date.equals(entry.date) && !(message != null ? !message.equals(entry.message) : entry.message != null) && !(title != null ? !title.equals(entry.title) : entry.title != null);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.title + " (" + MiscUtil.dateToString(this.date) + "): " + this.message;
    }
}
