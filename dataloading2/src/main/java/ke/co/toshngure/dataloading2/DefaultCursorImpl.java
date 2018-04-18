package ke.co.toshngure.dataloading2;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */
public class DefaultCursorImpl extends CursorImpl {

    private static final String AFTER = "after";

    private static final String BEFORE = "before";
    private static final String RECENT = "recent";
    private static final String PER_PAGE = "perPage";

    @Override
    public String getAfterKey() {
        return AFTER;
    }

    @Override
    public String getBeforeKey() {
        return BEFORE;
    }

    @Override
    public String getPerPageKey() {
        return PER_PAGE;
    }

    @Override
    protected String getRecentFlagKey() {
        return RECENT;
    }


}
