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
import ke.co.toshngure.basecode.rest.annotations.Resource;

/**
 * Created by Anthony Ngure on 09/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

@Resource(relativeUrl = "posts")
public class Post<Parent extends IItem & IExpandable & ISubItem & IClickable>
        extends AbstractItem<Post<Parent>, Post.ViewHolder> implements ISubItem<Post, Parent> {
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

    @Override
    public Parent getParent() {
        return null;
    }

    @Override
    public Post withParent(Parent parent) {
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
