package ke.co.toshngure.dataloading2;

/**
 * Created by Anthony Ngure on 25/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

public abstract class CursorImpl {

    /**
     * Applies when loading fresh data and refreshing from top
     *
     * @return Key to indicate where to start when loading from top or loading fresh
     */
    protected abstract String getAfterKey();

    /**
     * eg startId, Applies when loading at the bottom
     *
     * @return Key to indicate where to start when loading more
     */
    protected abstract String getBeforeKey();

    /**
     * eg limit
     *
     * @return Key for items to request per page
     */
    protected abstract String getPerPageKey();

    /**
     * Applies when doing a complete fresh load or when the user pulls to refresh
     *
     * @return Key to use to flag that you are requesting for recent data
     */
    protected abstract String getRecentFlagKey();
}
