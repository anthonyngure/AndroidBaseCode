package ke.co.toshngure.editor;

import android.text.Editable;
import android.widget.EditText;

/**
 * Created by IRSHU on 27/2/2017.
 */

public interface EditorListener {
    void onTextChanged(EditText editText, Editable text);
}