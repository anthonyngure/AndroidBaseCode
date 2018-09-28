package ke.co.toshngure.androidbasecode.fragment;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.loopj.android.http.RequestParams;

import java.util.List;
import java.util.Random;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.model.Post;
import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelListFragment;
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
        //Snackbar.make(mRecyclerView, "Data is ready", Snackbar.LENGTH_INDEFINITE).show();
    }


    @Override
    public void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, topViewContainer);
        NetworkImage topViewNI = topViewContainer.findViewById(R.id.topViewNI);
        Random random = new Random();
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?"+random.nextInt());
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams params = super.getRequestParams();
        //params.put("_page", 100);
        //params.put("_limit", 1);
        params.put("q", "tosh_ngure");
        return params;
    }
}
