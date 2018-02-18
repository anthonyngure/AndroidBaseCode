/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jaychang.srv.SimpleCell;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.cell.UserCell;
import ke.co.toshngure.androidbasecode.model.User;
import ke.co.toshngure.androidbasecode.network.Client;
import ke.co.toshngure.dataloading.DataLoadingConfig;
import ke.co.toshngure.dataloading.ModelListBottomSheetFragment;
import ke.co.toshngure.views.NetworkImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragmentBottomSheetFragment extends ModelListBottomSheetFragment<User, UserCell>
        implements SimpleCell.OnCellClickListener<User> {


    private int requestsCount;

    public UsersFragmentBottomSheetFragment() {
        // Required empty public constructor
    }

    public static UsersFragmentBottomSheetFragment newInstance() {
        Bundle args = new Bundle();
        UsersFragmentBottomSheetFragment fragment = new UsersFragmentBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public UserCell onCreateCell(User item) {
        UserCell userCell = new UserCell(item);
        userCell.setOnCellClickListener(this);
        return userCell;
    }

    @Override
    public DataLoadingConfig getDataLoadingConfig() {
        return new DataLoadingConfig()
                .withUrl("https://toshngure.co.ke/basecode/public/api/v1/users", Client.getInstance().getClient())
                .withCursorsEnabled()
                .withRefreshEnabled()
                .withLoadingMoreEnabled()
                .withDebugEnabled()
                .withPerPage(10);

        /*return new DataLoadingConfig()
                .withAutoRefreshDisabled()
                .withCacheEnabled(5)
                .withDebugEnabled();*/
    }


    @Override
    public Class<User> getModelClass() {
        return User.class;
    }

    @Override
    public void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, null);
        NetworkImage topViewNI = view.findViewById(R.id.topViewNI);
        topViewContainer.addView(view);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }


    @Override
    public void setUpBottomView(FrameLayout bottomViewContainer) {
        super.setUpBottomView(bottomViewContainer);
        /*View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, null);
        BaseNetworkImage topViewNI = view.findViewById(R.id.topViewNI);
        bottomViewContainer.addView(view);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");*/
    }


    @Override
    public void onCellClicked(@NonNull User user) {
        Toast.makeText(getContext(), user.getId() + " = " + user.getName(), Toast.LENGTH_SHORT).show();
    }
}
