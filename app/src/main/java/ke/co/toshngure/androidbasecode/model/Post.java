package ke.co.toshngure.androidbasecode.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;

import ke.co.toshngure.basecode.rest.annotations.Resource;

/**
 * Created by Anthony Ngure on 09/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

@Resource(relativeUrl = "posts")
public class Post extends AbstractItem<Post, Post.ViewHolder> {
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
