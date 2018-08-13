/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.logging;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.menu.SimpleMenu;

/**
 * Created by Anthony Ngure on 9/11/2016.
 * Email : anthonyngure25@gmail.com.
 * Company : Laysan Incorporation
 */
public class LogItem extends AbstractItem<LogItem, LogItem.ViewHolder> {

    private String title, details;

    LogItem(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_type_log_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_log_item;
    }

    static class ViewHolder extends FastItemAdapter.ViewHolder<LogItem> {

        TextView detailsTV;
        TextView titleTV;

        ViewHolder(View itemView) {
            super(itemView);
            detailsTV = itemView.findViewById(R.id.detailsTV);
            titleTV = itemView.findViewById(R.id.titleTV);
        }

        @Override
        public void bindView(LogItem item, List<Object> payloads) {
            titleTV.setText(item.title);
            detailsTV.setText(item.details);
        }

        @Override
        public void unbindView(LogItem item) {

        }


    }
}
