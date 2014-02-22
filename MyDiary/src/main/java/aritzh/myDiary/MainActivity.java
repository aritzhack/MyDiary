package aritzh.myDiary;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import aritzh.myDiary.db.DBHelper;
import aritzh.myDiary.db.DiaryTable;
import aritzh.myDiary.diary.Entry;
import aritzh.myDiary.fragments.DatePickerDialogFragment;
import aritzh.myDiary.util.Date;
import aritzh.myDiary.util.MiscUtil;
import aritzh.myDiary.util.dialogs.DialogBuilder;
import aritzh.myDiary.util.dialogs.DialogButton;

public class MainActivity extends Activity implements DatePickerDialogFragment.DatePickerListener {

    public static final String LOG_TAG = "MY-DIARY";

    private boolean isLoggedIn = false;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.dayTitleText)).setText((this.date = new Date()).toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_day:
                DatePickerDialogFragment.newInstance(new Date()).show(getFragmentManager(), "mainDatePickerDialog");
                return true;
            case R.id.action_save_entry:
                Entry entry = new Entry(this.date, ((EditText)findViewById(R.id.entryMessage)).getText());
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                if(DiaryTable.isEntryPresent(this.date, db)) DiaryTable.updateEntry(entry, db);
                else DiaryTable.putEntry(entry, db);
                db.close();
                return true;
            case R.id.action_debug_db:
                helper = new DBHelper(this);
                db = helper.getReadableDatabase();
                for(Entry e : DiaryTable.getAllEntries(db)) {
                    Log.i(LOG_TAG, e.getTitle() + "(" + e.getDate().toString() + "): " + e.getMessage());
                }
                db.close();
                return true;
            case R.id.action_clear_db:
                helper = new DBHelper(this);
                helper.onCreate(helper.getWritableDatabase());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dateSaved(Date date) {
        if(date == null) return;
        this.date = date;
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
