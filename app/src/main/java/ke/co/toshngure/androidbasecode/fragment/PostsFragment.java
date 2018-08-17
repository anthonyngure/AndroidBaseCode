package ke.co.toshngure.androidbasecode.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.loopj.android.http.RequestParams;

import java.util.List;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.model.Post;
import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelListFragment;
import ke.co.toshngure.basecode.dataloading.ModelsFragment;
import ke.co.toshngure.basecode.rest.Response;
import ke.co.toshngure.views.NetworkImage;

public class PostsFragment extends ModelListFragment<Post> {

    public static PostsFragment newInstance() {

        Bundle args = new Bundle();

        PostsFragment fragment = new PostsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public DataLoadingConfig<Post> getDataLoadingConfig() {
        return super.getDataLoadingConfig()
                .withFab(R.drawable.ic_android_black_24dp, FloatingActionButton.SIZE_NORMAL,
                        android.R.color.holo_red_dark, android.R.color.holo_orange_dark)
                .withUrl("https://jsonplaceholder.typicode.com/posts", Post.class, true)
                .withTopViewCollapsible();
    }

    @Override
    public void onDataReady(List<Post> data) {
        super.onDataReady(data);
        Snackbar.make(mRecyclerView, "Data is ready", Snackbar.LENGTH_INDEFINITE).show();
    }


    @Override
    public void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, topViewContainer);
        NetworkImage topViewNI = topViewContainer.findViewById(R.id.topViewNI);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams params = super.getRequestParams();
        params.put("page", 200);
        params.put("limit", 1);
        return params;
    }
}
