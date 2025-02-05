package g.api.views.menuview;

public interface OnMenuItemActionListener {
    /**
     * Weather consume this action.
     *
     * @param position tab_id
     * @return true or false
     */
    public boolean onMenuItemClick(int position);

    /**
     * MenuItem is selected.
     *
     * @param position tab_id
     */
    public void onMenuItemSelected(int position);
}
