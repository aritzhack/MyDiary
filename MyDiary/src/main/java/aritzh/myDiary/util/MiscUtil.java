package aritzh.myDiary.util;

import android.content.Context;

import org.joda.time.LocalDate;

import aritzh.myDiary.util.dialogs.DialogBuilder;
import aritzh.myDiary.util.dialogs.DialogButton;

/**
 * Created by aritzh on 19/02/14.
 */
public class MiscUtil {

    public static void showOkDialog(Context context, int messageId, DialogBuilder.Listener listener) {
        new DialogBuilder(context, messageId)
                .addButton(DialogButton.OK, listener)
                .setCancelButton(DialogButton.OK)
                .show();
    }

    public static LocalDate parseDate(String dateString) {
        String[] tokens = dateString.split("-");
        int day = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int year = Integer.parseInt(tokens[2]);
        return new LocalDate(year, month, day);
    }

    public static String dateToString(LocalDate date) {
        return String.format("%02d", date.getDayOfMonth()) + "-" + String.format("%02d", date.getMonthOfYear()) + "-" + date.getYear();
    }
}
