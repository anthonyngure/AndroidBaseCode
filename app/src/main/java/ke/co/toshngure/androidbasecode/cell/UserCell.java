/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.cell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.model.User;
import ke.co.toshngure.views.NetworkImage;

/**
 * Created by Anthony Ngure on 23/11/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class UserCell extends SimpleCell<User, UserCell.UserViewHolder> {


    public UserCell(@NonNull User item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.cell_user;
    }

    @NonNull
    @Override
    protected UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull View view) {
        return new UserViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull Context context, Object o) {
        userViewHolder.bind(getItem());
    }

    public class UserViewHolder extends SimpleViewHolder {

        @BindView(R.id.avatarNI)
        NetworkImage avatarNI;
        @BindView(R.id.nameTV)
        TextView nameTV;
        @BindView(R.id.emailTV)
        TextView emailTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(User item) {
            avatarNI.loadImageFromNetwork(item.getAvatar());
            nameTV.setText(item.getName() + "(" + item.getUsername() + ")");
            emailTV.setText(item.getEmail() + "?id=" + item.getId());
        }
    }
}
