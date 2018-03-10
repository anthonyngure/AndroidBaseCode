package ke.co.toshngure.basecode.rest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Anthony Ngure on 11/02/2018.
 * Email : anthonyngure25@gmail.com.
 */

public final class Response {

    public static final String DATA = "data";
    public static final String META = "meta";
    public static final String MESSAGE = "message";
    public static final String ERROR_CODE = "code";

    public static final class ARRAY {
        private JSONArray data;

        private JSONObject meta;

        public ARRAY(JSONArray data, JSONObject meta) {
            this.data = data;
            this.meta = meta;
        }

        public JSONArray getData() {
            return data;
        }

        public JSONObject getMeta() {
            return meta;
        }
    }

    public static final class OBJECT {


        private JSONObject data;

        private JSONObject meta;

        public OBJECT(JSONObject data, JSONObject meta) {
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
}
