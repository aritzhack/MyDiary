package aritzh.myDiary.diary;

import java.util.HashMap;
import java.util.Map;

import aritzh.myDiary.util.Date;

/**
 * Created by aritzh on 15/02/14.
 */
public class Diary {

    private final String password;
    private final Map<Date, Entry> entries = new HashMap<>();

    private Diary(Map<Date, Entry> entries, String password) {
        this.entries.clear();
        this.entries.putAll(entries);
        this.password = password;
    }

    public static Diary newDiary(String password) {
        return new Diary(new HashMap<Date, Entry>(), password);
    }

    public Entry getEntry(Date date) {
        return this.entries.get(date);
    }

    public boolean hasDate(Date date) {
        return this.entries.containsKey(date);
    }
}
