package ke.co.toshngure.basecode.rest;

import android.app.Application;

/**
 * Created by Anthony Ngure on 12/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public abstract class ClientApp extends Application {

    public ClientApp() {
    }

    protected abstract Client.Config getClientConfig();
}
