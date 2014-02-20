package aritzh.myDiary;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import aritzh.myDiary.fragments.DatePickerDialogFragment;
import aritzh.myDiary.util.Date;
import aritzh.myDiary.util.MiscUtil;
import aritzh.myDiary.util.dialogs.DialogBuilder;
import aritzh.myDiary.util.dialogs.DialogButton;

public class MainActivity extends Activity implements DatePickerDialogFragment.DatePickerListener {

    public static final String LOG_TAG = "MY-DIARY";

    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.dayTitleText)).setText(new Date().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_day) {
            DatePickerDialogFragment.newInstance(new aritzh.myDiary.util.Date()).show(getFragmentManager(), "mainDatePickerDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dateSaved(aritzh.myDiary.util.Date date) {
        ((TextView) findViewById(R.id.dayTitleText)).setText(date.toString());
        Log.i(MainActivity.LOG_TAG, "Date: " + date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLoggedIn) promptForPassword();
    }

    private void promptForPassword() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new DialogBuilder(this, "")
                .setTitle("Enter password")
                .addButton(DialogButton.OK, new DialogBuilder.Listener() {
                    @Override
                    public void onAction(DialogInterface dialog) {
                        Editable e = input.getText();
                        if (e != null && e.length() != 0) {
                            MainActivity.this.loggedIn(e.toString());
                            dialog.dismiss();
                        } else {
                            MiscUtil.showOkDialog(MainActivity.this, R.string.passMustNotBeEmptyMessage, new DialogBuilder.Listener() {
                                @Override
                                public void onAction(DialogInterface dialogInterface) {
                                    MainActivity.this.promptForPassword();
                                }
                            });
                        }
                    }
                })
                .addButton(DialogButton.CANCEL, new DialogBuilder.Listener() {
                    @Override
                    public void onAction(DialogInterface dialog) {
                        new DialogBuilder(MainActivity.this, R.string.mustEnterPasswordMessage)
                                .addButton(DialogButton.YES, new DialogBuilder.Listener() {
                                    @Override
                                    public void onAction(DialogInterface dialog) {
                                        MainActivity.this.finish();
                                    }
                                })
                                .addButton(DialogButton.NO, new DialogBuilder.Listener() {
                                    @Override
                                    public void onAction(DialogInterface dialog) {
                                        dialog.dismiss();
                                        MainActivity.this.promptForPassword();
                                    }
                                })
                                .setCancelButton(DialogButton.YES)
                                .show();
                    }
                })
                .setCancelButton(DialogButton.CANCEL)
                .setView(input)
                .show();
    }

    public void loggedIn(String pass) {
    }
}
