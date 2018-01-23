/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.cell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.logging.LogItem;
import ke.co.toshngure.views.SimpleListItemView;

/**
 * Created by Anthony Ngure on 22/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class LogItemCell extends SimpleCell<LogItem, LogItemCell.LogItemViewHolder> {
    public LogItemCell(@NonNull LogItem item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cell_log_item;
    }

    @NonNull
    @Override
    protected LogItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View view) {
        return new LogItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull LogItemViewHolder logItemViewHolder, int i, @NonNull Context context, Object o) {
        logItemViewHolder.bind(getItem());
    }

    public class LogItemViewHolder extends SimpleViewHolder {
        public LogItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(LogItem item) {
            ((SimpleListItemView) itemView).setTitle(item.getTitle());
            ((SimpleListItemView) itemView).setTitle(item.getDetails());
        }
    }
}
