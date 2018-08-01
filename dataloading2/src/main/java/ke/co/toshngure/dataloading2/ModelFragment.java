package ke.co.toshngure.dataloading2;

public class ModelFragment<M> extends AbstractItemsFragment<M> {

    private static final String TAG = "ModelFragment";

    @Override
    boolean loadsSingleItem() {
        return true;
    }
}
