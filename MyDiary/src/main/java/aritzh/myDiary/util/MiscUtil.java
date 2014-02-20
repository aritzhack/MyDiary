package aritzh.myDiary.util;

import android.content.Context;

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

}
