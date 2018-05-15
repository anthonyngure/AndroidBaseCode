/*
 * Copyright (c) 2017.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.model;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.views.NetworkImage;

/**
 * Created by Anthony Ngure on 23/11/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class User extends AbstractItem<User, User.ViewHolder> {


    private int id;
    private String name;
    private String email;
    private String username;
    private String avatar;

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.user_item_type_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.cell_user;
    }


    static class ViewHolder extends FastAdapter.ViewHolder<User> {

        @BindView(R.id.avatarNI)
        NetworkImage avatarNI;
        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.usernameTV)
        TextView usernameTV;
        @BindView(R.id.emailTV)
        TextView emailTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindView(User item, List<Object> payloads) {
            avatarNI.loadImageFromNetwork(item.getAvatar());
            nameTV.setText("ID = " + item.getId() + " - " + item.getName());
            usernameTV.setText(item.getUsername());
            emailTV.setText(item.getEmail());
        }

        @Override
        public void unbindView(User item) {
            //Glide.clear(avatarNI);
            nameTV.setText(null);
            usernameTV.setText(null);
            emailTV.setText(null);
        }
    }
}
