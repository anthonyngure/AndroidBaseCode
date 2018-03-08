package ke.co.toshngure.androidbasecode.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ke.co.toshngure.androidbasecode.R;
import ke.co.toshngure.androidbasecode.network.Client;
import ke.co.toshngure.basecode.app.BaseAppActivity;
import ke.co.toshngure.basecode.networking.ConnectionHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionHandlerFragment extends Fragment {


    @BindView(R.id.connectBtn)
    Button connectBtn;
    @BindView(R.id.contentTV)
    TextView contentTV;
    Unbinder unbinder;
    private ConnectionHandler.Callback connectionCallback;

    public ConnectionHandlerFragment() {
        // Required empty public constructor
    }

    public static ConnectionHandlerFragment newInstance() {

        Bundle args = new Bundle();

        ConnectionHandlerFragment fragment = new ConnectionHandlerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connection_handler, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionCallback = new ConnectionHandler.Callback((BaseAppActivity) getActivity()) {
            @Override
            protected void onRetry() {
                super.onRetry();
                onConnectBtnClicked();
            }

            @Override
            protected void onResponse(JSONArray data, JSONObject meta) {
                super.onResponse(data, meta);
                contentTV.setText(String.valueOf(data));
            }
        };
    }

    @OnClick(R.id.connectBtn)
    public void onConnectBtnClicked() {
        String url = "https://toshngure.co.ke/basecode/public/api/v1/users";
        RequestParams requestParams = new RequestParams();
        requestParams.put("after", 0);
        requestParams.put("before", 0);
        requestParams.put("recent", true);
        requestParams.put("perPage", 10);
        Client.getInstance().getClient().get(url, requestParams, new ConnectionHandler(connectionCallback));
    }

}
