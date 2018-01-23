/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.networking;

import org.json.JSONObject;

/**
 * Created by Anthony Ngure on 21/01/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */

public class JSONObjectResponse {


    private JSONObject data;

    private JSONObject meta;

    public JSONObjectResponse(JSONObject data, JSONObject meta) {
        this.data = data;
        this.meta = meta;
    }

    public JSONObject getData() {
        return data;
    }

    public JSONObject getMeta() {
        return meta;
    }
}
