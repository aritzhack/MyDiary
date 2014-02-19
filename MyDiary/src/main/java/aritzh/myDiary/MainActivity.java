package aritzh.myDiary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import aritzh.myDiary.fragments.DatePickerDialogFragment;
import aritzh.myDiary.fragments.PasswordDialogFragment;
import aritzh.myDiary.util.Date;

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
        if(!isLoggedIn) new PasswordDialogFragment().show(getFragmentManager(), "passwordLogin");
    }
}
