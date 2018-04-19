/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.androidbasecode.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import ke.co.toshngure.androidbasecode.model.Post;
import ke.co.toshngure.androidbasecode.model.User;
import ke.co.toshngure.androidbasecode.network.Client;
import ke.co.toshngure.dataloading2.DataLoadingConfig;
import ke.co.toshngure.dataloading2.DefaultCursorImpl;
import ke.co.toshngure.dataloading2.ModelListFragment;
import ke.co.toshngure.dataloading2.decoration.HorizontalDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment2 extends ModelListFragment<User> {


    private int requestsCount;

    public UsersFragment2() {
        // Required empty public constructor
    }

    public static UsersFragment2 newInstance() {
        Bundle args = new Bundle();
        UsersFragment2 fragment = new UsersFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public DataLoadingConfig<User> getDataLoadingConfig() {
        String url = "https://toshngure.co.ke/basecode/public/api/v1/users";
        return super.getDataLoadingConfig()
                .withUrl(url, Client.getInstance().getClient(), User.class)
                .withCursors(new DefaultCursorImpl(), true, true)
                .withDebugEnabled()
                .withTopViewCollapsible()
                .withPerPage(15);
    }

    /*@Override
    public void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, topViewContainer);
        NetworkImage topViewNI = topViewContainer.findViewById(R.id.topViewNI);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }*/

    @Override
    public void onSetUpRecyclerView(RecyclerView recyclerView) {
        super.onSetUpRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
    }

    @Override
    public void onSetUpAdapter(ItemAdapter<User> fastItemAdapter, FastAdapter fastAdapter) {
        super.onSetUpAdapter(fastItemAdapter, fastAdapter);
        ItemAdapter<Post> postItemAdapter = new ItemAdapter<>();
        fastAdapter.addAdapter(0, postItemAdapter);
    }

    /*@Override
    public void setUpBottomView(FrameLayout bottomViewContainer) {
        super.setUpBottomView(bottomViewContainer);
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, bottomViewContainer);
        NetworkImage topViewNI = bottomViewContainer.findViewById(R.id.topViewNI);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }*/

}
