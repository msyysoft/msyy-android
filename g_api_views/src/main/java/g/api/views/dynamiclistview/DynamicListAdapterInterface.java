package g.api.views.dynamiclistview;

public interface DynamicListAdapterInterface {
    /**
     * Determines how to reorder items dragged from <code>originalPosition</code> to <code>newPosition</code>
     */
    void reorderItems(int originalPosition, int newPosition);

    /**
     * Determines whether the item in the specified <code>tab_id</code> can be reordered.
     */
    boolean canReorder(int position);

}
