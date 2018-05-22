package ke.co.toshngure.basecode.rest;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ke.co.toshngure.basecode.R;

/**
 * Created by Anthony Ngure on 10/04/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class DefaultResponseDefinition implements ResponseDefinition {

    @Nullable
    @Override
    public String message(int statusCode, JSONObject response) {
        if (response != null) {
            try {
                if (statusCode == 422 && response.getJSONObject(Response.DATA) != null) {
                    JSONObject data = response.getJSONObject(Response.DATA);
                    StringBuilder sb = new StringBuilder();
                    Iterator<String> iterator = data.keys();
                    while (iterator != null && iterator.hasNext()) {
                        String name = iterator.next();
                        try {
                            JSONArray valueErrors = data.getJSONArray(name);
                            sb.append(name.toUpperCase()).append("\n");
                            for (int i = 0; i < valueErrors.length(); i++) {
                                sb.append(valueErrors.get(i)).append("\n");
                            }
                        } catch (JSONException e) {
                            return String.valueOf(response);
                        }
                    }

                    return sb.toString();

                } else {
                    JSONObject meta = response.getJSONObject(Response.META);
                    return meta.getString(Response.MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Client.getConfig().getContext().getString(R.string.connection_timed_out);
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
