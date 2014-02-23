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

import org.joda.time.LocalDate;

import aritzh.myDiary.db.DBHelper;
import aritzh.myDiary.db.DiaryTable;
import aritzh.myDiary.db.MetaTable;
import aritzh.myDiary.diary.Diary;
import aritzh.myDiary.diary.Entry;
import aritzh.myDiary.fragments.DatePickerDialogFragment;
import aritzh.myDiary.util.EncryptionUtil;
import aritzh.myDiary.util.MiscUtil;
import aritzh.myDiary.util.dialogs.DialogBuilder;
import aritzh.myDiary.util.dialogs.DialogButton;

public class MainActivity extends Activity implements DatePickerDialogFragment.DatePickerListener {

    public static final String LOG_TAG = "MY-DIARY";

    private boolean isLoggedIn = false;
    private LocalDate date;
    private boolean doNotAskAgain;
    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doNotAskAgain = false;
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.dayTitleText)).setText(MiscUtil.dateToString(this.date = new LocalDate()));
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
                DatePickerDialogFragment.newInstance(new LocalDate()).show(getFragmentManager(), "mainDatePickerDialog");
                return true;
            case R.id.action_save_entry:
                Entry entry = new Entry(this.date, ((EditText) findViewById(R.id.entryMessage)).getText());
                this.diary.entryChanged(entry);
                return true;
            case R.id.action_debug_db:
                this.debugDB();
                return true;
            case R.id.action_clear_db:
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                DiaryTable.initDB(db);
                MetaTable.initDB(db);
                return true;
            case R.id.action_save_db:
                this.storeDiaryToDB();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void debugDB() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        for (Entry e : DiaryTable.getAllEntries(db)) {
            Log.i(LOG_TAG, e.toString());
        }
        db.close();
    }

    public void storeDiaryToDB() {
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        this.diary.storeToDB(db);
        db.close();
    }

    @Override
    public void dateSet(LocalDate date) {
        if (date == null) return;
        this.date = date;
        ((TextView) findViewById(R.id.dayTitleText)).setText(MiscUtil.dateToString(date));
        Entry entry = this.diary.getDecodedEntry(date);
        ((EditText) findViewById(R.id.entryMessage)).setText(entry.getMessage() != null ? entry.getMessage() : "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        MetaTable.initDB(db);
        DiaryTable.initDB(db);
        boolean isFirstTime = !MetaTable.isPropertyPresent("password", db);
        if (isFirstTime) {
            MetaTable.initDB(db);
            DiaryTable.initDB(db);
        }
        db.close();

        if (isFirstTime) this.promptForFirstPassword();
        else if (!isLoggedIn) promptForPassword();
    }

    private void promptForPassword() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (doNotAskAgain) return;

        new DialogBuilder(this, "")
                .setTitle(getString(R.string.enterPassword))
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
        doNotAskAgain = true;
    }

    public void loggedIn(String pass) {
        doNotAskAgain = false;
        DBHelper helper = new DBHelper(this);
        this.diary = Diary.fromDB(helper.getReadableDatabase());
        this.diary.setUserPass(pass);
        for (Entry e : this.diary.getEntries()) {
            Log.d(LOG_TAG, e.toString());
        }
    }

    public void promptForFirstPassword() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (doNotAskAgain) return;

        new DialogBuilder(this, "")
                .setTitle(getString(R.string.enterDiaryPasssword))
                .addButton(DialogButton.OK, new DialogBuilder.Listener() {
                    @Override
                    public void onAction(DialogInterface dialog) {
                        Editable e = input.getText();
                        if (e != null && e.length() != 0) {
                            MainActivity.this.firstLogin(e.toString());
                            dialog.dismiss();
                        } else {
                            MiscUtil.showOkDialog(MainActivity.this, R.string.passMustNotBeEmptyMessage, new DialogBuilder.Listener() {
                                @Override
                                public void onAction(DialogInterface dialogInterface) {
                                    MainActivity.this.promptForFirstPassword();
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
                                        MainActivity.this.promptForFirstPassword();
                                    }
                                })
                                .setCancelButton(DialogButton.YES)
                                .show();
                    }
                })
                .setCancelButton(DialogButton.CANCEL)
                .setView(input)
                .show();
        doNotAskAgain = true;
    }

    private void firstLogin(String password) {
        Log.d(LOG_TAG, "Password: " + password);
        String encryptedPassword = EncryptionUtil.encodePassword(password);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        MetaTable.setProperty("password", encryptedPassword, db);
        this.diary = Diary.newDiary(encryptedPassword);
        db.close();

    }
}
