package ke.co.toshngure.basecode.dataloading;

public class ModelFragment<M> extends AbstractModelsFragment<M> {

    private static final String TAG = "ModelFragment";

    @Override
    boolean loadsSingleItem() {
        return true;
    }
}
