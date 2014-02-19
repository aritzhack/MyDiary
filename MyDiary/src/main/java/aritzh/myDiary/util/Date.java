package aritzh.myDiary.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by aritzh on 15/02/14.
 */
public class Date implements Serializable {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private final int year;
    private final int month;
    private final int day;
    private final Calendar calendar = new GregorianCalendar();
    private final String toString;

    public Date() {
        this(Calendar.getInstance());
    }

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        calendar.set(year, month-1, day);
        this.toString = DATE_FORMAT.format(this.calendar.getTime());
    }

    public Date(java.util.Date date) {
        this(date.getYear(), date.getMonth() + 1, date.getDay());
    }

    public Date(Calendar calendar) {
        this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public Date(long unixTime) {
        calendar.setTimeInMillis(unixTime);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.toString = DATE_FORMAT.format(this.calendar.getTime());
    }

    public static Date parse(String dateString) throws ParseException {
        return new Date(DATE_FORMAT.parse(dateString));
    }

    public static Date parseUnix(String unixDateString) throws NumberFormatException {
        return new Date(Long.parseLong(unixDateString));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getJavaMonth() {
        return month - 1;
    }

    public Month getMonthEnum() {
        return Month.withIndex(this.calendar.get(Calendar.MONTH));
    }

    public int getDay() {
        return day;
    }

    public long getUnixTime(){
        return this.calendar.getTimeInMillis();
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.withIndex(this.calendar.get(Calendar.DAY_OF_WEEK));
    }

    public Calendar getCalendar() {
        return (Calendar) this.calendar.clone();
    }

    @Override
    public String toString() {
        return this.toString;
    }

    public String toUnixString() {
        return String.valueOf(this.calendar.getTimeInMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        return day == date.day && month == date.month && year == date.year;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    public static enum DayOfWeek {
        MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY),
        THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY),
        SUNDAY(Calendar.SUNDAY);

        private final int index;

        private DayOfWeek(int index) {
            this.index = index;
        }

        public static DayOfWeek withIndex(int index) {
            for (DayOfWeek w : DayOfWeek.values()) {
                if (w.getIndex() == index) return w;
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    public static enum Month {
        JANUARY(Calendar.JANUARY), FEBRUARY(Calendar.FEBRUARY), MARCH(Calendar.MARCH), APRIL(Calendar.APRIL),
        MAY(Calendar.MAY), JUNE(Calendar.JUNE), JULY(Calendar.JULY), AUGUST(Calendar.AUGUST),
        SEPTEMBER(Calendar.SEPTEMBER), OCTOBER(Calendar.OCTOBER), NOVEMBER(Calendar.NOVEMBER), DECEMBER(Calendar.DECEMBER);

        private int index;

        private Month(int index) {
            this.index = index;
        }

        public static Month withIndex(int index) {
            for (Month w : Month.values()) {
                if (w.getIndex() == index) return w;
            }
            return null;
        }

        public int getIndex() {
            return index;
        }
    }
}
