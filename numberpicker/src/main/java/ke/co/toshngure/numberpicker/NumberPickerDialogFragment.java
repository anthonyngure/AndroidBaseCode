package ke.co.toshngure.numberpicker;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;


/**
 * Dialog to set alarm time.
 */
public class NumberPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "NumberPickerDialogFragment_ReferenceKey";
    private static final String MIN_NUMBER_KEY = "NumberPickerDialogFragment_MinNumberKey";
    private static final String MAX_NUMBER_KEY = "NumberPickerDialogFragment_MaxNumberKey";
    private static final String PLUS_MINUS_VISIBILITY_KEY = "NumberPickerDialogFragment_PlusMinusVisibilityKey";
    private static final String DECIMAL_VISIBILITY_KEY = "NumberPickerDialogFragment_DecimalVisibilityKey";
    private static final String LABEL_TEXT_KEY = "NumberPickerDialogFragment_LabelTextKey";
    private static final String CURRENT_NUMBER_KEY = "NumberPickerDialogFragment_CurrentNumberKey";
    private static final String CURRENT_DECIMAL_KEY = "NumberPickerDialogFragment_CurrentDecimalKey";
    private static final String CURRENT_SIGN_KEY = "NumberPickerDialogFragment_CurrentSignKey";

    private NumberPicker mPicker;

    private int mReference = -1;
    private String mLabelText = "";

    private BigDecimal mMinNumber = null;
    private BigDecimal mMaxNumber = null;
    private Integer mCurrentNumber = null;
    private Double mCurrentDecimal = null;
    private Integer mCurrentSign = null;
    private int mPlusMinusVisibility = View.VISIBLE;
    private int mDecimalVisibility = View.VISIBLE;
    private Vector<NumberPickerDialogHandlerV2> mNumberPickerDialogHandlersV2 = new Vector<NumberPickerDialogHandlerV2>();
    private OnDialogDismissListener mDismissCallback;


    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference           an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param minNumber           (optional) the minimum possible number
     * @param maxNumber           (optional) the maximum possible number
     * @param plusMinusVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param decimalVisibility   (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param labelText           (optional) text to add as a label
     * @return a Picker!
     */
    public static NumberPickerDialogFragment newInstance(int reference,
                                                         BigDecimal minNumber,
                                                         BigDecimal maxNumber,
                                                         Integer plusMinusVisibility,
                                                         Integer decimalVisibility,
                                                         String labelText,
                                                         Integer currentNumberValue,
                                                         Double currentDecimalValue,
                                                         Integer currentNumberSign) {
        final NumberPickerDialogFragment frag = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        if (minNumber != null) {
            args.putSerializable(MIN_NUMBER_KEY, minNumber);
        }
        if (maxNumber != null) {
            args.putSerializable(MAX_NUMBER_KEY, maxNumber);
        }
        if (plusMinusVisibility != null) {
            args.putInt(PLUS_MINUS_VISIBILITY_KEY, plusMinusVisibility);
        }
        if (decimalVisibility != null) {
            args.putInt(DECIMAL_VISIBILITY_KEY, decimalVisibility);
        }
        if (labelText != null) {
            args.putString(LABEL_TEXT_KEY, labelText);
        }
        if (currentNumberValue != null) {
            args.putInt(CURRENT_NUMBER_KEY, currentNumberValue);
        }
        if (currentDecimalValue != null) {
            args.putDouble(CURRENT_DECIMAL_KEY, currentDecimalValue);
        }
        if (currentNumberSign != null) {
            args.putInt(CURRENT_SIGN_KEY, currentNumberSign);
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(REFERENCE_KEY)) {
            mReference = args.getInt(REFERENCE_KEY);
        }
        if (args != null && args.containsKey(PLUS_MINUS_VISIBILITY_KEY)) {
            mPlusMinusVisibility = args.getInt(PLUS_MINUS_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(DECIMAL_VISIBILITY_KEY)) {
            mDecimalVisibility = args.getInt(DECIMAL_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(MIN_NUMBER_KEY)) {
            mMinNumber = (BigDecimal) args.getSerializable(MIN_NUMBER_KEY);
        }
        if (args != null && args.containsKey(MAX_NUMBER_KEY)) {
            mMaxNumber = (BigDecimal) args.getSerializable(MAX_NUMBER_KEY);
        }
        if (args != null && args.containsKey(LABEL_TEXT_KEY)) {
            mLabelText = args.getString(LABEL_TEXT_KEY);
        }
        if (args != null && args.containsKey(CURRENT_NUMBER_KEY)) {
            mCurrentNumber = args.getInt(CURRENT_NUMBER_KEY);
        }
        if (args != null && args.containsKey(CURRENT_DECIMAL_KEY)) {
            mCurrentDecimal = args.getDouble(CURRENT_DECIMAL_KEY);
        }
        if (args != null && args.containsKey(CURRENT_SIGN_KEY)) {
            mCurrentSign = args.getInt(CURRENT_SIGN_KEY);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.number_picker_dialog, container, false);
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigDecimal number = mPicker.getEnteredNumber();
                if (mMinNumber != null && mMaxNumber != null && (isSmaller(number) || isBigger(number))) {
                    String errorText = getString(R.string.min_max_error, mMinNumber, mMaxNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                } else if (mMinNumber != null && isSmaller(number)) {
                    String errorText = getString(R.string.min_error, mMinNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                } else if (mMaxNumber != null && isBigger(number)) {
                    String errorText = getString(R.string.max_error, mMaxNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                }
                for (NumberPickerDialogHandlerV2 handler : mNumberPickerDialogHandlersV2) {
                    handler.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(), mPicker.getIsNegative(), number);
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof NumberPickerDialogHandlerV2) {
                    final NumberPickerDialogHandlerV2 act = (NumberPickerDialogHandlerV2) activity;
                    act.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(), mPicker.getIsNegative(), number);
                } else if (fragment instanceof NumberPickerDialogHandlerV2) {
                    final NumberPickerDialogHandlerV2 frag = (NumberPickerDialogHandlerV2) fragment;
                    frag.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(), mPicker.getIsNegative(), number);
                }
                dismiss();
            }
        });

        mPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setDecimalVisibility(mDecimalVisibility);
        mPicker.setPlusMinusVisibility(mPlusMinusVisibility);
        mPicker.setLabelText(mLabelText);
        if (mMinNumber != null) {
            mPicker.setMin(mMinNumber);
        }
        if (mMaxNumber != null) {
            mPicker.setMax(mMaxNumber);
        }
        mPicker.setNumber(mCurrentNumber, mCurrentDecimal, mCurrentSign);
        return view;
    }

    private boolean isBigger(BigDecimal number) {
        return number.compareTo(mMaxNumber) > 0;
    }

    private boolean isSmaller(BigDecimal number) {
        return number.compareTo(mMinNumber) < 0;
    }

    @Override
    public void onDismiss(DialogInterface dialoginterface) {
        super.onDismiss(dialoginterface);
        if (mDismissCallback != null) {
            mDismissCallback.onDialogDismiss(dialoginterface);
        }
    }

    public void setOnDismissListener(OnDialogDismissListener ondialogdismisslistener) {
        mDismissCallback = ondialogdismisslistener;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface NumberPickerDialogHandlerV2 {
        void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setNumberPickerDialogHandlersV2(Vector<NumberPickerDialogHandlerV2> handlers) {
        this.mNumberPickerDialogHandlersV2 = handlers;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}