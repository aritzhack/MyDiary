package aritzh.myDiary.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aritzh.myDiary.R;
import aritzh.myDiary.diary.Diary;

/**
 * Created by aritzh on 14/02/14.
 */
public class PasswordDialogFragment extends FramelessDialogFragment {

    public PasswordDialogFragment() {
        super(R.layout.password_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PasswordDialogFragment.this.dismiss();
                }
            });

            view.findViewById(R.id.cancelLoginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PasswordDialogFragment.this.activity.finish();
                }
            });
        }

        return view;
    }

    @Override
    public void onCancel(final DialogInterface dialog) {
        super.onCancel(dialog);
        assert getActivity() != null;
        final FragmentManager fm = getActivity().getFragmentManager();
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.mustEnterPasswordMessage))
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Dialog)dialog).getOwnerActivity().finish();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new PasswordDialogFragment().show(fm, "passwordLogin");
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ((Dialog)dialog).getOwnerActivity().finish();
            }
        }).create().show();
    }
}
