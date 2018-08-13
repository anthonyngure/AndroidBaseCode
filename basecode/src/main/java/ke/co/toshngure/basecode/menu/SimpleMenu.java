package ke.co.toshngure.basecode.menu;

import android.os.Parcel;
import android.os.Parcelable;
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

public class SimpleMenu extends AbstractItem<SimpleMenu, SimpleMenu.ViewHolder> implements Parcelable {

    private String title;
    @DrawableRes
    private int idRes;
    @DrawableRes
    private int iconRes;
    @StringRes
    private int titleRes;

    public SimpleMenu(@StringRes int titleRes) {
        this(titleRes, 0);
    }

    public SimpleMenu(String title) {
        this(title, 0);
    }

    public SimpleMenu(@StringRes int titleRes, @DrawableRes int iconRes) {
        this(titleRes, iconRes, 0);
    }

    public SimpleMenu(String title, @DrawableRes int iconRes) {
        this(title, iconRes, 0);
    }

    public SimpleMenu(@StringRes int titleRes, @DrawableRes int iconRes, @IdRes int idRes) {
        this.titleRes = titleRes;
        this.iconRes = iconRes;
        this.idRes = idRes;
    }

    public SimpleMenu(String title, @DrawableRes int iconRes, @IdRes int idRes) {
        this.title = title;
        this.iconRes = iconRes;
        this.idRes = idRes;
    }

    @Override
    public long getIdentifier() {
        return this.idRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    static class ViewHolder extends FastAdapter.ViewHolder<SimpleMenu> {

        ImageView iconIV;
        TextView titleTV;

        ViewHolder(View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.iconIV);
            titleTV = itemView.findViewById(R.id.titleTV);
        }

        @Override
        public void bindView(SimpleMenu item, List<Object> payloads) {
            if (item.iconRes == 0) {
                iconIV.setVisibility(View.GONE);
            } else {
                iconIV.setImageResource(item.iconRes);
                iconIV.setVisibility(View.VISIBLE);
            }
            if (item.titleRes == 0) {
                titleTV.setText(item.title);
            } else {
                titleTV.setText(item.titleRes);
            }
        }

        @Override
        public void unbindView(SimpleMenu item) {

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

    protected SimpleMenu(Parcel in) {
        this.idRes = in.readInt();
        this.iconRes = in.readInt();
        this.titleRes = in.readInt();
    }

    public static final Creator<SimpleMenu> CREATOR = new Creator<SimpleMenu>() {
        @Override
        public SimpleMenu createFromParcel(Parcel source) {
            return new SimpleMenu(source);
        }

        @Override
        public SimpleMenu[] newArray(int size) {
            return new SimpleMenu[size];
        }
    };
}
