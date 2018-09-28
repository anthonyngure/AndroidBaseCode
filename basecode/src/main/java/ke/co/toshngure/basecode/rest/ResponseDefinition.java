package ke.co.toshngure.basecode.rest;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Anthony Ngure on 10/04/2018.
 * Email : anthonyngure25@gmail.com.
 */

public interface ResponseDefinition {


    @Nullable
    String message(int statusCode, JSONObject response);

    @Nullable
    String dataKey(JSONObject response);

    @Nullable
    String message(int statusCode, JSONArray response);

    @Nullable
    String dataKey(JSONArray response);


}
