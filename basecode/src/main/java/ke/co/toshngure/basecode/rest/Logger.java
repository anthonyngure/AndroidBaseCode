package ke.co.toshngure.basecode.rest;

import android.util.Log;

/**
 * Created by Anthony Ngure on 10/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

final class Logger {

    private static final String TAG = "Logger";
    private static Logger mInstance;
    private static boolean debug = false;

    private Logger(boolean debuggable) {
        debug = debuggable;
    }

    /**
     * Should be initialized at @Link{Client.init()}
     *
     * @param debug
     */
    static void init(boolean debug) {
        if (mInstance == null) {
            mInstance = new Logger(debug);
        } else {
            throw new IllegalArgumentException("Client should only be initialized once," +
                    " most probably in the Application class");
        }
    }

    static void log(Object msg) {
        if (debug) {
            Log.d(TAG, String.valueOf(msg));
        }
    }
}
