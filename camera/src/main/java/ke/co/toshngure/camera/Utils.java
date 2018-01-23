package ke.co.toshngure.camera;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Anthony Ngure on 23/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class Utils {

    private static String getAppExternalDirectoryFolder(Context context) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), context.getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getPath();
    }

    static String getFolderInAppExternalDirectory(Context context, String folderName) {
        File file = new File(getAppExternalDirectoryFolder(context), folderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }
}
