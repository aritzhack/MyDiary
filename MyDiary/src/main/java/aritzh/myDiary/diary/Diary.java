package aritzh.myDiary.diary;

import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import org.joda.time.LocalDate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aritzh.myDiary.db.DiaryTable;
import aritzh.myDiary.db.MetaTable;
import aritzh.myDiary.util.EncryptionUtil;

/**
 * Created by aritzh on 15/02/14.
 */
public class Diary {

    private final String password;
    private final Map<LocalDate, Entry> entries = Maps.newHashMap();
    private final Map<LocalDate, Entry> changedEntries = Maps.newHashMap();
    private String userPass;

    private Diary(Map<LocalDate, Entry> entries, String password) {
        Preconditions.checkArgument(password != null, "Password cannot be null!");
        Preconditions.checkArgument("".equals(password), "Password cannot be empty!");
        this.entries.clear();
        if (entries != null) this.entries.putAll(entries);
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

    public Collection<Entry> getEntries() {
        return this.entries.values();
    }

    public void entryChanged(Entry e) {
        Preconditions.checkArgument(e != null, "Entry cannot be null!");
        e = Diary.encodeEntry(e, this.password);
        this.entries.put(e.getDate(), e);
        this.changedEntries.put(e.getDate(), e);
    }

    public static Entry encodeEntry(Entry e, String password) {
        String message = EncryptionUtil.encrypt(e.getMessage(), password);
        String title = EncryptionUtil.encrypt(e.getTitle(), password);
        return new Entry(e.getDate(), message, title);
    }

    public static Entry decodeEntry(Entry entry, String userPass) {
        String message = EncryptionUtil.decrypt(entry.getMessage(), userPass);
        String title = EncryptionUtil.decrypt(entry.getTitle(), userPass);
        return new Entry(entry.getDate(), message, title);
    }

    public static Diary fromDB(SQLiteDatabase db) {
        Preconditions.checkArgument(db != null, "DB cannot be null!");
        Map<LocalDate, Entry> entries = Maps.newHashMap();

        List<Entry> dbEntries = DiaryTable.getAllEntries(db);
        for (Entry e : dbEntries) {
            entries.put(e.getDate(), e);
        }
        String pass = MetaTable.getProperty("password", db);
        return new Diary(entries, pass);
    }

    public Entry getDecodedEntry(LocalDate date) {
        Entry encoded = this.getEntry(date);
        if (encoded == null) return null;
        return Diary.decodeEntry(encoded, this.userPass);
    }

    public void setUserPass(String userPass) {
        this.userPass = EncryptionUtil.encodePassword(userPass);
    }

    public String getUserPass() {
        return userPass;
    }

    public void storeToDB(SQLiteDatabase db) {
        for (Entry e : this.changedEntries.values()) {
            if (DiaryTable.isEntryPresent(e.getDate(), db)) DiaryTable.updateEntry(e, db);
            else DiaryTable.putEntry(e, db);
        }
    }
}
