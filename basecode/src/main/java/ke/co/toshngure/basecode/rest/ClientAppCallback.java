package ke.co.toshngure.basecode.rest;

import android.app.Application;

/**
 * Created by Anthony Ngure on 12/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public interface ClientAppCallback {

    Application getInstance();

    Client.Config getClientConfig();
}
