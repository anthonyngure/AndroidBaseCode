package ke.co.toshngure.androidbasecode.model;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.basecode.util.Spanny;
import ke.co.toshngure.views.NetworkImage;

/**
 * Created by Anthony Ngure on 09/03/2018.
 * Email : anthonyngure25@gmail.com.
 */

public class Post extends AbstractItem<Post, Post.ViewHolder> {

    @Expose
    @SerializedName("body")
    private String body;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("userId")
    private int userId;

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

    static class ViewHolder extends FastItemAdapter.ViewHolder<Post> {

        private Unbinder unbinder;
        @BindView(R.id.avatarNI)
        NetworkImage avatarNI;
        @BindView(R.id.titleTV)
        TextView titleTV;
        @BindView(R.id.bodyTV)
        TextView bodyTV;
        private Random random;

        ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            this.random = new Random();
        }

        @Override
        public void bindView(Post item, List<Object> payloads) {
            avatarNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?" + random.nextInt());
            titleTV.setText(new Spanny(String.valueOf(item.id))
                    .append(itemView.getContext().getString(R.string.one_tab))
                    .append(item.title));
            bodyTV.setText(item.body);
        }

        @Override
        public void unbindView(Post item) {
            //unbinder.unbind();
        }
    }
}
