/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Anthony Ngure on 27/02/2017.
 * Email : anthonyngure25@gmail.com.
 *
 */

public abstract class BaseAdapterDataObserver extends RecyclerView.AdapterDataObserver {

    @Override
    public void onChanged() {
        super.onChanged();
        update();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        update();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        update();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        update();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        update();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        update();
    }

    protected abstract  void update();
}
