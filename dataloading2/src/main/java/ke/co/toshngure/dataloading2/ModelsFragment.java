package ke.co.toshngure.dataloading2;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ModelsFragment<M> extends AbstractItemsFragment<M> {

    private static final String TAG = "ModelFragment";

    @Override
    boolean loadsSingleItem() {
        return false;
    }
}
