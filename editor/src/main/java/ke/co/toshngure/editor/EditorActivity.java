package ke.co.toshngure.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ke.co.toshngure.editor.models.EditorTextStyle;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TEXT = "extra_text";

    private Editor textEditor;


    public static void start(Activity activity, int requestCode, @Nullable String html) {
        Intent starter = new Intent(activity, EditorActivity.class);
        starter.putExtra(EXTRA_TEXT, html);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textEditor = findViewById(R.id.textEditor);
        if (getIntent().getExtras() != null) {
            String text = getIntent().getExtras().getString(EXTRA_TEXT);
            if (!TextUtils.isEmpty(text)) {
                textEditor.render(text);
            } else {
                textEditor.render();
            }
        } else {
            textEditor.render();
        }

        findViewById(R.id.actionBold).setOnClickListener(this);
        findViewById(R.id.actionItalic).setOnClickListener(this);
        findViewById(R.id.actionIndent).setOnClickListener(this);
        findViewById(R.id.actionOutdent).setOnClickListener(this);
        findViewById(R.id.actionBulletsList).setOnClickListener(this);
        findViewById(R.id.actionNumbersList).setOnClickListener(this);
        findViewById(R.id.actionBreak).setOnClickListener(this);
        findViewById(R.id.actionInsertLink).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId() == R.id.action_done) || (item.getItemId() == android.R.id.home)) {
            Intent data = new Intent();
            data.putExtra(EXTRA_TEXT, textEditor.getContentAsHTML());
            setResult(Activity.RESULT_OK, data);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent data = new Intent();
        data.putExtra(EXTRA_TEXT, textEditor.getContentAsHTML());
        setResult(Activity.RESULT_OK, data);
        this.finish();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.actionBold) {
            textEditor.updateTextStyle(EditorTextStyle.BOLD);
        } else if (view.getId() == R.id.actionItalic) {
            textEditor.updateTextStyle(EditorTextStyle.ITALIC);
        } else if (view.getId() == R.id.actionIndent) {
            textEditor.updateTextStyle(EditorTextStyle.INDENT);
        } else if (view.getId() == R.id.actionOutdent) {
            textEditor.updateTextStyle(EditorTextStyle.OUTDENT);
        } else if (view.getId() == R.id.actionBulletsList) {
            textEditor.insertList(false);
        } else if (view.getId() == R.id.actionNumbersList) {
            textEditor.insertList(true);
        } else if (view.getId() == R.id.actionBreak) {
            textEditor.insertDivider();
        } else if (view.getId() == R.id.actionInsertLink) {
            textEditor.insertLink();
        }
    }
}
