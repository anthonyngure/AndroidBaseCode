package ke.co.toshngure.androidbasecode.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.utils.PrefUtils;

public class PrefUtilsImplActivity extends BaseActivity {

    @BindView(R.id.textTV)
    TextView textTV;
    @BindView(R.id.actionBtn)
    Button actionBtn;

    private boolean nextActionIsWrite;

    public static void start(Context context) {
        Intent starter = new Intent(context, PrefUtilsImplActivity.class);
        context.startActivity(starter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_utils_impl);
        ButterKnife.bind(this);

        String currentText = PrefUtils.getInstance().getString(R.string.pref_test);
        nextActionIsWrite = TextUtils.isEmpty(currentText);
        textTV.setText(currentText);

        if (nextActionIsWrite) {
            actionBtn.setText("Tap to write");
        }
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.actionBtn)
    public void onActionBtnClicked() {
        Random random = new Random();
        if (nextActionIsWrite) {
            actionBtn.setText("Tap to read");
            PrefUtils.getInstance().writeString(R.string.pref_test, "Random number = " + random.nextInt(10));
        } else {
            actionBtn.setText("Tap to write");
            String currentText = PrefUtils.getInstance().getString(R.string.pref_test);
            textTV.setText(currentText);
        }
        nextActionIsWrite = !nextActionIsWrite;
    }
}
