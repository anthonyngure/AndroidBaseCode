package ke.co.toshngure.dataloading;

import android.os.AsyncTask;

import com.jaychang.srv.SimpleCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class DataParserTask<M, C extends SimpleCell<M, ?>> extends AsyncTask<Object, Void, List<C>> {

    private static final String DATA = "data";
    private static final String CURSORS = "cursors";
    private DataLoadingFragmentImpl<M, C> mDataLoadingFragmentImpl;

    DataParserTask(DataLoadingFragmentImpl<M, C> dataLoadingFragment) {
        this.mDataLoadingFragmentImpl = dataLoadingFragment;
    }

    @Override
    protected List<C> doInBackground(Object... objects) {
        List<C> cellList = new ArrayList<C>();
        try {

            JSONArray itemsJSONArray;

            //PARSE JSON OBJECT RESPONSE
            if (objects[0] instanceof JSONObject) {
                JSONObject dataJSONObject = (JSONObject) objects[0];
                if (mDataLoadingFragmentImpl.mDataLoadingConfig.isCursorsEnabled()) {
                    JSONObject cursors = dataJSONObject.getJSONObject(CURSORS);
                    long after = cursors.getLong(mDataLoadingFragmentImpl.mCursorImpl.getAfterKey());
                    long before = cursors.getLong(mDataLoadingFragmentImpl.mCursorImpl.getBeforeKey());
                    ModelCursor modelCursor = new ModelCursor();
                    modelCursor.setAfter(after);
                    modelCursor.setBefore(before);
                    mDataLoadingFragmentImpl.updateModelCursor(modelCursor);
                }
                itemsJSONArray = dataJSONObject.getJSONArray(DATA);
            } else if (objects[0] instanceof JSONArray) {
                itemsJSONArray = (JSONArray) objects[0];
            } else {
                itemsJSONArray = new JSONArray();
            }

            for (int i = 0; i < itemsJSONArray.length(); i++) {
                JSONObject itemObject = itemsJSONArray.getJSONObject(i);
                M item = Utils.getSafeGson().fromJson(itemObject.toString(),
                        mDataLoadingFragmentImpl.mListener.getModelClass());
                mDataLoadingFragmentImpl.mListener.onSaveItem(item);
                cellList.add(mDataLoadingFragmentImpl.mListener.onCreateCell(item));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cellList;
    }

    @Override
    protected void onPostExecute(List<C> cs) {
        super.onPostExecute(cs);
        mDataLoadingFragmentImpl.onDataParsed(cs);

    }
}
