package g.api.http;

public interface GRefreshListener {
    void onRefresh(boolean isLoadMore, boolean isFromPTR, int... flag);
}
