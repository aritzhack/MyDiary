package aritzh.myDiary.diary;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aritzh on 15/02/14.
 */
public class Diary {

    private final String password;
    private final Map<LocalDate, Entry> entries = new HashMap<>();

    private Diary(Map<LocalDate, Entry> entries, String password) {
        this.entries.clear();
        this.entries.putAll(entries);
        this.password = password;
    }

    public static Diary newDiary(String password) {
        return new Diary(new HashMap<LocalDate, Entry>(), password);
    }

    public Entry getEntry(LocalDate date) {
        return this.entries.get(date);
    }

    public boolean hasDate(LocalDate date) {
        return this.entries.containsKey(date);
    }
}
