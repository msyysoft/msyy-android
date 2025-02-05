package g.support.list;

/**
 * 处理下拉刷新控件冲突的子接口
 */
public interface MyPTRChildI {
    void requestFatherDisallowInterceptTouchEvent(boolean b);

    boolean isShouldMove();

    void setRefreshLayout(MyPTRRefreshLayout refreshLayout);

    void setMyPTRFatherI(MyPTRFatherI myPTRFatherI);
}
