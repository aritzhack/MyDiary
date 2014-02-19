package aritzh.myDiary.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aritzh.myDiary.MainActivity;

/**
 * Created by aritzh on 14/02/14.
 */
public abstract class FramelessDialogFragment extends DialogFragment {

    private final int layout;
    protected MainActivity activity;

    public FramelessDialogFragment(int layout) {
        this.layout = layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(this.layout, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must be of type " + MainActivity.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }
}
