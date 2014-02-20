package aritzh.myDiary.util.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by aritzh on 20/02/14.
 */
public class DialogBuilder {

    private final Context context;
    private final boolean useMessageId;
    private final EnumMap<DialogButton, Listener> buttons = Maps.newEnumMap(DialogButton.class);
    private final Set<DialogButton.ButtonType> addedButtonTypes = new HashSet<>();
    private DialogButton cancelButton;
    private Listener cancelListener;
    private String message, title;
    private int messageId;
    private View view;

    public DialogBuilder(Context context, int messageId) {
        this.context = context;
        this.messageId = messageId;
        this.useMessageId = true;
    }

    public DialogBuilder(Context context, String message) {
        this.context = context;
        this.message = message;
        this.useMessageId = false;
    }

    public DialogBuilder(Context context, Object message) {
        this(context, String.valueOf(message));
    }

    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogBuilder addButton(DialogButton button, Listener listener) {
        this.buttons.put(button, listener);
        if (this.addedButtonTypes.contains(button.getType()))
            throw new IllegalArgumentException("Cannot add two buttons of the same type (i.e. you can only add one of each POSITIVE, NEUTRAL and/or NEGATIVE)");
        else this.addedButtonTypes.add(button.getType());
        return this;
    }

    public DialogBuilder setCancelListener(Listener listener) {
        this.cancelListener = listener;
        if (cancelButton != null)
            throw new IllegalStateException("A cancel button has already been set, either a cancel button or listener must be chosen, but not both");
        return this;
    }

    public DialogBuilder setCancelButton(DialogButton button) {
        this.cancelButton = button;
        if (cancelListener != null)
            throw new IllegalStateException("A cancel listener has already been set, either a cancel button or listener must be chosen, but not both");
        return this;
    }

    public DialogBuilder setView(View view) {
        this.view = view;
        return this;
    }

    public AlertDialog create() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        if (this.useMessageId) builder.setMessage(this.messageId);
        else if (!Strings.isNullOrEmpty(this.message)) builder.setMessage(this.message);

        if (!Strings.isNullOrEmpty(this.title)) builder.setTitle(this.title);

        for (Map.Entry<DialogButton, Listener> entry : this.buttons.entrySet()) {
            switch (entry.getKey().getType()) {
                case POSITIVE:
                    builder.setPositiveButton(entry.getKey().getMessageId(), entry.getValue());
                    break;
                case NEUTRAL:
                    builder.setNeutralButton(entry.getKey().getMessageId(), entry.getValue());
                    break;
                case NEGATIVE:
                    builder.setNegativeButton(entry.getKey().getMessageId(), entry.getValue());
                    break;
            }
        }

        if (this.cancelButton != null) builder.setOnCancelListener(this.buttons.get(cancelButton));
        else if (this.cancelListener != null) builder.setOnCancelListener(cancelListener);

        if (this.view != null) builder.setView(view);

        return builder.create();
    }

    public void show() {
        this.create().show();
    }

    public static abstract class Listener implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {

        public abstract void onAction(DialogInterface dialog);

        @Override
        public void onCancel(DialogInterface dialog) {
            this.onAction(dialog);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            this.onAction(dialog);
        }
    }
}
