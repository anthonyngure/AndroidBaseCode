package ke.co.toshngure.basecode.dataloading;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.Objects;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.util.DrawableUtils;

class Utils {

    public static void configureFab(FloatingActionButton fab, DataLoadingConfig mDataLoadingConfig) {

        fab.setSize(mDataLoadingConfig.getFabSize());
        Drawable icon = ContextCompat.getDrawable(fab.getContext(), mDataLoadingConfig.getFabIcon());
        fab.setImageDrawable(DrawableUtils.tintDrawable(fab.getContext(), icon, mDataLoadingConfig.getFabIconTint()));

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
        };

        int[] colors = new int[]{
                ContextCompat.getColor(fab.getContext(), mDataLoadingConfig.getFabBackgroundTint()),
                ContextCompat.getColor(fab.getContext(), mDataLoadingConfig.getFabBackgroundTint())
        };
        ColorStateList myList = new ColorStateList(states, colors);
        fab.setBackgroundTintList(myList);
        fab.setVisibility(mDataLoadingConfig.isFabShown() ? View.VISIBLE : View.GONE);
    }

    public static void configureSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout, DataLoadingConfig mDataLoadingConfig) {
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(mSwipeRefreshLayout.getContext(), R.color.colorPrimary),
                ContextCompat.getColor(mSwipeRefreshLayout.getContext(), R.color.colorAccent),
                ContextCompat.getColor(mSwipeRefreshLayout.getContext(), R.color.colorPrimaryDark)
        );
        mSwipeRefreshLayout.setEnabled(mDataLoadingConfig.isRefreshEnabled());
    }

}
