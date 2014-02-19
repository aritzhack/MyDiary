package aritzh.myDiary.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import aritzh.myDiary.R;

/**
 * Created by aritzh on 19/02/14.
 */
public class MiscUtil {

    public static void showAlertDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setNeutralButton(context.getString(R.string.ok), null)
                .create()
                .show();
    }

    public static void showAlertDialog(Context context, int messageId) {
        new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setNeutralButton(context.getString(R.string.ok), null)
                .create()
                .show();
    }

    public static void showAlertDialog(Context context, int messageId, DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setNeutralButton(context.getString(R.string.ok), clickListener)
                .create()
                .show();
    }

    public static void yesNoDialog(Context context, int messageId, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setPositiveButton(R.string.yes, yesListener)
                .setNegativeButton(R.string.no, noListener)
                .create()
                .show();
    }

    public static void yesNoDialog(Context context, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.yes, yesListener)
                .setNegativeButton(R.string.no, noListener)
                .create()
                .show();
    }
}
