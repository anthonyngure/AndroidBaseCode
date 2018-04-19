package ke.co.toshngure.dataloading2;

import android.os.AsyncTask;

import com.mikepenz.fastadapter.items.AbstractItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */
class DataParserTask<M extends AbstractItem<M, ?>> extends AsyncTask<Object, Void, List<M>> {

    private static final String DATA = "data";
    private static final String CURSORS = "cursors";
    private DataLoadingFragmentImpl<M> mDataLoadingFragmentImpl;

    DataParserTask(DataLoadingFragmentImpl<M> dataLoadingFragment) {
        this.mDataLoadingFragmentImpl = dataLoadingFragment;
    }

    @Override
    protected List<M> doInBackground(Object... objects) {
        List<M> items = new ArrayList<M>();
        try {

            JSONArray itemsJSONArray;

            //PARSE JSON OBJECT RESPONSE
            if (objects[0] instanceof JSONObject) {
                JSONObject dataJSONObject = (JSONObject) objects[0];
                if (mDataLoadingFragmentImpl.mDataLoadingConfig.isRefreshEnabled() ||
                        mDataLoadingFragmentImpl.mDataLoadingConfig.isLoadingMoreEnabled()) {
                    JSONObject cursors = dataJSONObject.getJSONObject(CURSORS);
                    long after = cursors.getLong(mDataLoadingFragmentImpl.mDataLoadingConfig.getCursorImpl().getAfterKey());
                    long before = cursors.getLong(mDataLoadingFragmentImpl.mDataLoadingConfig.getCursorImpl().getBeforeKey());
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
                        mDataLoadingFragmentImpl.mDataLoadingConfig.getModelClass());
                mDataLoadingFragmentImpl.mListener.onSaveItem(item);
                items.add(item);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    protected void onPostExecute(List<M> items) {
        super.onPostExecute(items);
        mDataLoadingFragmentImpl.onDataParsed(items);

    }
}
