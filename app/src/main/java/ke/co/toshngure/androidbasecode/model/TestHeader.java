package ke.co.toshngure.androidbasecode.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;

/**
 * Created by Anthony Ngure on 30/04/2018.
 * Email : anthonyngure25@gmail.com.
 */
public class TestHeader extends AbstractItem<TestHeader, TestHeader.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
