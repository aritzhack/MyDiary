package aritzh.myDiary.util.dialogs;

import aritzh.myDiary.R;

/**
 * Created by aritzh on 20/02/14.
 */
public enum DialogButton {
    YES(R.string.yes, ButtonType.POSITIVE), ACCEPT(R.string.accept, ButtonType.POSITIVE),
    CANCEL(R.string.cancel, ButtonType.NEGATIVE), NO(R.string.no, ButtonType.NEGATIVE),
    OK(R.string.ok, ButtonType.NEUTRAL);

    private final int messageId;
    private final ButtonType type;

    private DialogButton(int messageId, ButtonType type) {
        this.messageId = messageId;
        this.type = type;
    }

    public int getMessageId() {
        return messageId;
    }

    public ButtonType getType() {
        return type;
    }

    public static enum ButtonType {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}
