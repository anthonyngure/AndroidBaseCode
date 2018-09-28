package ke.co.toshngure.basecode.menu;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ke.co.toshngure.basecode.R;

@Deprecated
public class BottomSheetMenu extends AbstractItem<BottomSheetMenu, BottomSheetMenu.ViewHolder> implements Parcelable {

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

    public void setIdRes(int idRes) {
        this.idRes = idRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idRes);
        dest.writeInt(this.iconRes);
        dest.writeInt(this.titleRes);
    }

    protected BottomSheetMenu(Parcel in) {
        this.idRes = in.readInt();
        this.iconRes = in.readInt();
        this.titleRes = in.readInt();
    }

    public static final Parcelable.Creator<BottomSheetMenu> CREATOR = new Parcelable.Creator<BottomSheetMenu>() {
        @Override
        public BottomSheetMenu createFromParcel(Parcel source) {
            return new BottomSheetMenu(source);
        }

        @Override
        public BottomSheetMenu[] newArray(int size) {
            return new BottomSheetMenu[size];
        }
    };
}
