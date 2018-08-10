package ke.co.toshngure.basecode.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ke.co.toshngure.basecode.R;

public class BottomSheetMenu extends AbstractItem<BottomSheetMenu, BottomSheetMenu.ViewHolder> {

    @DrawableRes
    private int idRes;
    @DrawableRes
    private int iconRes;
    @StringRes
    private int titleRes;

    public BottomSheetMenu(@DrawableRes int iconRes, @StringRes int titleRes, @IdRes int idRes) {
        this.iconRes = iconRes;
        this.titleRes = titleRes;
        this.idRes = idRes;
    }

    @Override
    public long getIdentifier() {
        return this.idRes;
    }

    public int getIdRes() {
        return idRes;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_type_bottom_sheet_menu;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_bottom_sheet_menu;
    }

    static class ViewHolder extends FastAdapter.ViewHolder<BottomSheetMenu> {

        ImageView iconIV;
        TextView titleTV;

        ViewHolder(View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.iconIV);
            titleTV = itemView.findViewById(R.id.titleTV);
        }

        @Override
        public void bindView(BottomSheetMenu item, List<Object> payloads) {
            if (item.iconRes == 0){
                iconIV.setVisibility(View.GONE);
            } else {
                iconIV.setImageResource(item.iconRes);
                iconIV.setVisibility(View.VISIBLE);
            }
            titleTV.setText(item.titleRes);
        }

        @Override
        public void unbindView(BottomSheetMenu item) {

        }



    }
}
