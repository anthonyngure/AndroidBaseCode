package ke.co.toshngure.androidbasecode.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import ke.co.toshngure.androidbasecode.R;

/**
 * Created by Anthony Ngure on 09/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class Post extends AbstractItem<Post, Post.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_post;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_post;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
