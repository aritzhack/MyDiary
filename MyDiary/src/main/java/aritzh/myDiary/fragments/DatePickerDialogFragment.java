package aritzh.myDiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import java.util.Arrays;
import java.util.List;

import aritzh.myDiary.R;
import aritzh.myDiary.util.Date;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link DatePickerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerDialogFragment extends FramelessDialogFragment {
    private static final String DATE_ARG = "DATE_ARG";
    private Date date;

    public DatePickerDialogFragment() {
        super(R.layout.fragment_date_picker_dialog);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided date.
     *
     * @param date The date, or null if nothing
     * @return A new instance of fragment DatePickerDialogFragment.
     */
    public static DatePickerDialogFragment newInstance(Date date) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATE_ARG, date);
        fragment.setArguments(args);
        return fragment;
    }

    private static int getDayAmount(int year, int month) {
        final List<Integer> thirties = Arrays.asList(4, 6, 9, 11);
        return (month == 2) ? year % 4 == 0 ? 29 : 28 : thirties.contains(month) ? 30 : 31;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);

        if (getArguments() != null && getArguments().containsKey(DATE_ARG)) {
            this.date = (Date) getArguments().getSerializable(DATE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = super.onCreateView(inflater, container, savedInstanceState);

        final NumberPicker dayPicker = ((NumberPicker) v.findViewById(R.id.dayPicker));
        final NumberPicker monthPicker = ((NumberPicker) v.findViewById(R.id.monthPicker));
        final NumberPicker yearPicker = ((NumberPicker) v.findViewById(R.id.yearPicker));

        v.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment.this.save();
            }
        });

        v.findViewById(R.id.today_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogFragment.this.setToToday();
            }
        });

        NumberPickerListener yearListener = new NumberPickerListener() {
            @Override
            public void onValueChanged(NumberPicker np) {
                if (yearPicker.getValue() % 4 == 0 && monthPicker.getValue() == 2)
                    dayPicker.setMaxValue(29);
            }
        };


        NumberPickerListener monthListener = new NumberPickerListener() {
            @Override
            public void onValueChanged(NumberPicker np) {
                dayPicker.setMaxValue(getDayAmount(yearPicker.getValue(), monthPicker.getValue()));
            }
        };

        NumberPickerListener dayListener = new NumberPickerListener() {
            @Override
            public void onValueChanged(NumberPicker np) {
                // Do Nothing
            }
        };

        yearPicker.setOnValueChangedListener(yearListener);
        monthPicker.setOnValueChangedListener(monthListener);
        dayPicker.setOnValueChangedListener(dayListener);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setWrapSelectorWheel(true);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(true);

        yearPicker.setMinValue(1970);
        yearPicker.setMaxValue(2150);
        yearPicker.setWrapSelectorWheel(true);

        yearPicker.setOnScrollListener(yearListener);
        monthPicker.setOnScrollListener(monthListener);
        dayPicker.setOnScrollListener(dayListener);

        if (this.date == null) this.date = new Date();
        yearPicker.setValue(this.date.getYear());
        monthPicker.setValue(this.date.getMonth());
        dayPicker.setValue(this.date.getDay());

        dayPicker.setMaxValue(getDayAmount(yearPicker.getValue(), monthPicker.getValue()));

        return v;
    }

    public void save() {
        NumberPicker dayPicker = (NumberPicker) getView().findViewById(R.id.dayPicker);
        NumberPicker monthPicker = (NumberPicker) getView().findViewById(R.id.monthPicker);
        NumberPicker yearPicker = (NumberPicker) getView().findViewById(R.id.yearPicker);

        this.date = new Date(yearPicker.getValue(), monthPicker.getValue(), dayPicker.getValue());
        this.activity.dateSaved(this.date);
        this.dismiss();
    }

    public void setToToday() {
        NumberPicker dayPicker = (NumberPicker) getView().findViewById(R.id.dayPicker);
        NumberPicker monthPicker = (NumberPicker) getView().findViewById(R.id.monthPicker);
        NumberPicker yearPicker = (NumberPicker) getView().findViewById(R.id.yearPicker);

        this.date = new Date();
        yearPicker.setValue(this.date.getYear());
        monthPicker.setValue(this.date.getMonth());
        dayPicker.setValue(this.date.getDay());
    }

    public interface DatePickerListener {
        public void dateSaved(Date date);
    }

    private abstract class NumberPickerListener implements NumberPicker.OnScrollListener, NumberPicker.OnValueChangeListener {

        public abstract void onValueChanged(NumberPicker np);

        @Override
        public void onScrollStateChange(NumberPicker numberPicker, int i) {
            this.onValueChanged(numberPicker);
        }

        @Override
        public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
            if (newValue > numberPicker.getMaxValue() || newValue < numberPicker.getMinValue()) {
                if (newValue > numberPicker.getMaxValue() || newValue < numberPicker.getMinValue())
                    numberPicker.setValue(numberPicker.getMinValue());
                else numberPicker.setValue(oldValue);
            }
            this.onValueChanged(numberPicker);
        }
    }
}
