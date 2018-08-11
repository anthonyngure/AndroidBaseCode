package ke.co.toshngure.basecode.dataloading;

public class ModelFragment<M> extends AbstractModelFragment<M> {

    private static final String TAG = "ModelFragment";

    @Override
    boolean loadsSingleItem() {
        return true;
    }

}
