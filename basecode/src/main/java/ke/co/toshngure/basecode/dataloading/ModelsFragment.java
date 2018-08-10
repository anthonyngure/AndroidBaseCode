package ke.co.toshngure.basecode.dataloading;

public class ModelsFragment<M> extends AbstractModelFragment<M> {

    private static final String TAG = "ModelFragment";

    @Override
    boolean loadsSingleItem() {
        return false;
    }
}
