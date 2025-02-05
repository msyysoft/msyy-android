package g.api.views.dynamiclistview;

import android.view.View;

import java.util.List;

/**
 * Author: alex askerov
 * Date: 9/7/13
 * Time: 10:14 PM
 */
public class DynamicListUtils {
    /**
     * Delete item in <code>list</code> from tab_id <code>indexFrom</code> and insert it to <code>indexTwo</code>
     *
     * @param list
     * @param indexFrom
     * @param indexTwo
     */
    public static void reorder(List list, int indexFrom, int indexTwo) {
        Object obj = list.remove(indexFrom);
        list.add(indexTwo, obj);
    }

    /**
     * Swap item in <code>list</code> at tab_id <code>firstIndex</code> with item at tab_id <code>secondIndex</code>
     *
     * @param list The list in which to swap the items.
     * @param firstIndex The tab_id of the first item in the list.
     * @param secondIndex The tab_id of the second item in the list.
     */
    public static void swap(List list, int firstIndex, int secondIndex) {
        Object firstObject = list.get(firstIndex);
        Object secondObject = list.get(secondIndex);
        list.set(firstIndex, secondObject);
        list.set(secondIndex, firstObject);
    }

    public static float getViewX(View view) {
        return Math.abs((view.getRight() - view.getLeft()) / 2);
    }

    public static float getViewY(View view) {
        return Math.abs((view.getBottom() - view.getTop()) / 2);
    }
}
