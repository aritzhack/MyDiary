package aritzh.myDiary;

import android.app.Activity;
import android.app.AlertDialog;
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

        new AlertDialog.Builder(this)
                .setTitle("Enter password")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Editable e = input.getText();
                        if (e != null && e.length() != 0) {
                            MainActivity.this.loggedIn(e.toString());
                            dialogInterface.dismiss();
                        } else {
                            MiscUtil.showAlertDialog(MainActivity.this, R.string.passMustNotBeEmptyMessage, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int button) {
                                    MainActivity.this.promptForPassword();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        MainActivity.this.finish();
                    }
                })
                .setView(input)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(final DialogInterface dialog) {
                        MiscUtil.yesNoDialog(MainActivity.this, R.string.mustEnterPasswordMessage, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.this.finish();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        MainActivity.this.promptForPassword();
                                    }
                                }
                        );
                    }
                }).create()
                .show();
    }

    public void loggedIn(String pass) {
    }
}
