package ke.co.toshngure.basecode.rest;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anthony Ngure on 10/04/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class DefaultResponseDefinition implements ResponseDefinition {

    @Nullable
    @Override
    public String message(int statusCode, JSONObject response) {
        String message = null;
        try {
            JSONObject meta = response.getJSONObject(Response.META);
            message = meta.getString(Response.MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Nullable
    @Override
    public String dataKey(JSONObject response) {
        return Response.DATA;
    }

    @Nullable
    @Override
    public String message(int statusCode, JSONArray response) {
        return null;
    }

    @Nullable
    @Override
    public String dataKey(JSONArray response) {
        return null;
    }

}
