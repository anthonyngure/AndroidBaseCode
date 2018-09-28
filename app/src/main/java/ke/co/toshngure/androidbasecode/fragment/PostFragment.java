package ke.co.toshngure.androidbasecode.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.loopj.android.http.RequestParams;

import java.util.List;

import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.model.Post;
import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelsFragment;
import ke.co.toshngure.views.NetworkImage;

public class PostFragment extends ModelsFragment<Post> {

    public static PostFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PostFragment fragment = new PostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DataLoadingConfig<Post> getDataLoadingConfig() {
        return super.getDataLoadingConfig()
                .withFab(R.drawable.ic_add_black_24dp)
                .withUrl("https://jsonplaceholder.typicode.com/posts", Post.class, true)
                .withRefreshEnabled()
                .withTopViewCollapsible();
    }

    @Override
    protected void onDataReady(Post data) {
        super.onDataReady(data);
    }

    @Override
    protected void onDataReady(List<Post> data) {
        super.onDataReady(data);
    }

    @Override
    protected void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, topViewContainer);
        NetworkImage topViewNI = topViewContainer.findViewById(R.id.topViewNI);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }

    @Override
    protected void onSetUpContentView(FrameLayout contentView) {
        super.onSetUpContentView(contentView);
        LayoutInflater.from(getActivity()).inflate(R.layout.item_post, contentView);
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
