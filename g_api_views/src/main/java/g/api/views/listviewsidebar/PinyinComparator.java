package g.api.views.listviewsidebar;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        return getCompareKey(o1).compareTo(getCompareKey(o2));
    }

    public String getCompareKey(SortModel o) {
        return o.getSortletter();
    }

}
