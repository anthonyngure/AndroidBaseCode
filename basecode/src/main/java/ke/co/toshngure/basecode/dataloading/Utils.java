package ke.co.toshngure.basecode.dataloading;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import ke.co.toshngure.basecode.R;
import ke.co.toshngure.basecode.util.DrawableUtils;

class Utils {

    public static void configureFab(FloatingActionButton fab, DataLoadingConfig config) {

        if (config.isFabShown()){
            fab.setSize(config.getFabSize());
            Drawable icon = ContextCompat.getDrawable(fab.getContext(), config.getFabIcon());
            fab.setImageDrawable(DrawableUtils.tintDrawable(fab.getContext(), icon, config.getFabIconTint()));

            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_enabled}, // enabled
                    new int[]{-android.R.attr.state_enabled}, // disabled
            };

            int[] colors = new int[]{
                    ContextCompat.getColor(fab.getContext(), config.getFabBackgroundTint()),
                    ContextCompat.getColor(fab.getContext(), config.getFabBackgroundTint())
            };
            ColorStateList stateList = new ColorStateList(states, colors);
            fab.setBackgroundTintList(stateList);
        }

        fab.setVisibility(config.isFabShown() ? View.VISIBLE : View.GONE);
    }

    public static void configureSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout,
                                                   DataLoadingConfig dataLoadingConfig) {
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(swipeRefreshLayout.getContext(), R.color.colorPrimary),
                ContextCompat.getColor(swipeRefreshLayout.getContext(), R.color.colorAccent),
                ContextCompat.getColor(swipeRefreshLayout.getContext(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setEnabled(dataLoadingConfig.isRefreshEnabled());
    }

}
