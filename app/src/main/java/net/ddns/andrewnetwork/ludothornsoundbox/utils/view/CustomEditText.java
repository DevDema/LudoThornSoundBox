package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
/**
 * THIS CLASS ADDS A LISTENER TO CHECK IF THE USER HAS THE KEYBOARD OPEN IN EDITOR MODE
 * AND PRESSES BACK.
 */
public class CustomEditText extends AppCompatEditText {

    private OnBackPressedInEditor onBackPressedInEditor;
    private OnUserStoppedListener onUserStoppedListener;
    private long delay = 1000; // 1 seconds after user stops typing
    private boolean isUserSetting = true;
    private OnUserClearedListener onUserClearedListener;

    public interface OnMaxLengthReachedListener {

        void onLengthReached(CustomEditText editText, CharSequence s, int count);
    }

    public interface OnUserClearedListener {

        void onUserClearedField(CustomEditText editText);
    }


    private OnMaxLengthReachedListener onMaxLengthReachedListener;

    public CustomEditText(Context context) {
        super(context);

        configListener();
    }

    private void configListener() {
        Handler handler = new Handler();
        final long[] last_text_edit = {0};
        long delay = 1000; // 1 seconds after user stops typing
        Editable editable[] = new Editable[1];
        editable[0] = new SpannableStringBuilder("");
        Runnable input_finish_checker = () -> {
            if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                if (onUserStoppedListener != null) {
                    if (onUserStoppedListener instanceof OnUserProgramStoppedListener)
                        ((OnUserProgramStoppedListener) onUserStoppedListener).OnProgramStoppedWriting(this, editable[0]);
                    if (isUserSetting)
                        onUserStoppedListener.onUserStoppedWriting(this, editable[0]);
                    else isUserSetting = true;
                }
            }
        };
        addTextChangedListener(new TextWatcher() {
                                   @Override
                                   public void beforeTextChanged(CharSequence s, int start, int count,
                                                                 int after) {
                                   }

                                   @Override
                                   public void onTextChanged(final CharSequence s, int start, int before,
                                                             int count) {
                                       handler.removeCallbacks(input_finish_checker);
                                       if (s.length() >= getMaxLength() && onMaxLengthReachedListener != null) {
                                           onMaxLengthReachedListener.onLengthReached(CustomEditText.this, s, s.length());
                                       }

                                       if (s.length() == 0 && onUserClearedListener != null) {
                                           onUserClearedListener.onUserClearedField(CustomEditText.this);
                                           isUserSetting = true;
                                       }
                                   }

                                   @Override
                                   public void afterTextChanged(final Editable s) {
                                       //avoid triggering event when text is empty
                                       if (s.length() > 0) {
                                           last_text_edit[0] = System.currentTimeMillis();
                                           editable[0] = s;
                                           handler.postDelayed(input_finish_checker, delay);
                                       }
                                   }
                               }

        );
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        int[] attributes = new int[]{android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingBottom, android.R.attr.paddingRight};

        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);

        //and get values you need by indexes from your array attributes defined above

        if (getPaddingStart() == 0)
            setPaddingRelative(20, getPaddingTop(), -20, getPaddingBottom());

        arr.recycle();

        configListener();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && onBackPressedInEditor != null) {
            onBackPressedInEditor.onBackPressed();
            return true;
        }
        return false;
    }

    public void setOnBackPressedInEditorListener(OnBackPressedInEditor onBackPressedInEditor) {
        this.onBackPressedInEditor = onBackPressedInEditor;
    }

    public void setOnUserStoppedListener(OnUserStoppedListener onUserStoppedListener) {
        this.onUserStoppedListener = onUserStoppedListener;
    }

    /**
     * @param delay delay in milliseconds
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        super.setText(text, type);
        if(text != null && !text.toString().isEmpty())
            this.isUserSetting = false;
    }

    public void setText(CharSequence text, boolean isUserSetting) {
        setText(text);

        this.isUserSetting = isUserSetting;
    }

    public void setTextAsUser(CharSequence text) {
        setText(text,true);
    }

    public OnUserStoppedListener getOnUserStoppedListener() {
        return onUserStoppedListener;
    }

    public int getMaxLength() {
        int maxLength = Integer.MAX_VALUE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < getFilters().length; i++) {
                InputFilter filter = getFilters()[i];
                if (filter instanceof InputFilter.LengthFilter) {
                    maxLength = ((InputFilter.LengthFilter) filter).getMax();
                }
            }
        }
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        setFilters(fArray);
    }

    public void setOnMaxLengthReachedListener(OnMaxLengthReachedListener onMaxLengthReachedListener) {
        this.onMaxLengthReachedListener = onMaxLengthReachedListener;
    }

    public void setOnUserClearedListener(OnUserClearedListener onUserClearedListener) {
        this.onUserClearedListener = onUserClearedListener;
    }
}
