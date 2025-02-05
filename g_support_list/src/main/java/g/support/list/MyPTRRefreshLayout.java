package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import g.api.views.pull2refreshview.PtrClassicFrameLayout;

public class MyPTRRefreshLayout extends PtrClassicFrameLayout {
    public static final int STATE_LOADING = 99;
    public static final int STATE_SUCC = 100;
    public static final int STATE_NO_DATA = 101;
    public static final int STATE_NET_ERROR = 102;
    public static final int STATE_OTHER_ERROR = 103;

    private int refreshAsyncTime = 600;
    private boolean childRequestEvent = false;
    private int resultState;

    private String msg_loading;
    private String result_no_data;
    private String result_net_error;
    private String result_ohter_error;

    private MyLoadingLayout loadingLayout;

    public MyPTRRefreshLayout(Context context) {
        super(context);
    }

    public MyPTRRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //先隐藏内容视图
        getContentView().setVisibility(GONE);
        //在顶部附件结果视图
        addResultView(getContext());
    }

    private void addResultView(Context context) {
        loadingLayout = new MyLoadingLayout(context);
        //结果视图默认的点击事件：再次刷新
        setOnResultClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRefreshFirst();
            }
        });
        addView(loadingLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //默认状态
        setResultState(STATE_SUCC);
        refreshComplete();
    }

    public MyLoadingLayout getResultView() {
        return loadingLayout;
    }

    @Override
    protected boolean childRequestTouchEvent(MotionEvent e) {
        return isInitFinished() && childRequestEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return childRequestEvent ? false : super.onInterceptTouchEvent(ev);
    }

    public void requestMyDisallowInterceptTouchEvent(boolean b) {
        childRequestEvent = b;
    }

    public void doRefreshFirst() {
        //避免重复执行刷新
        if (getResultState() != STATE_LOADING) {
            setResultState(STATE_LOADING);
            refreshComplete();
            doRefresh(false);
        }
    }

    public void doRefresh(boolean useRefreshHeader) {
        if (useRefreshHeader) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoRefresh();
                }
            }, refreshAsyncTime);
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPtrHandler != null)
                        mPtrHandler.onRefreshBegin(MyPTRRefreshLayout.this);
                }
            }, refreshAsyncTime);
        }

    }

    @Override
    public void refreshComplete() {
        super.refreshComplete();
        switch (resultState) {
            case STATE_LOADING://请求状态
                requestMyDisallowInterceptTouchEvent(true);
                loadingLayout.setVisibility(VISIBLE);
                loadingLayout.showLoading(msg_loading);
                getContentView().setVisibility(GONE);
                break;
            case STATE_SUCC://成功状态和初始状态
                requestMyDisallowInterceptTouchEvent(false);
                loadingLayout.setVisibility(INVISIBLE);
                getContentView().setVisibility(VISIBLE);
                break;
            case STATE_NO_DATA://无数据状态
                requestMyDisallowInterceptTouchEvent(true);
                loadingLayout.setVisibility(VISIBLE);
                loadingLayout.showResultNoData(result_no_data);
                getContentView().setVisibility(VISIBLE);
                break;
            case STATE_NET_ERROR://网络错误状态
                requestMyDisallowInterceptTouchEvent(true);
                loadingLayout.setVisibility(VISIBLE);
                loadingLayout.showResultNetError(result_net_error);
                getContentView().setVisibility(GONE);
                break;
            case STATE_OTHER_ERROR://其他错误状态
                requestMyDisallowInterceptTouchEvent(true);
                loadingLayout.setVisibility(VISIBLE);
                loadingLayout.showResultOtherError(result_ohter_error);
                getContentView().setVisibility(GONE);
                break;
            default:
                //未知状态，说明代码调用错误
                requestMyDisallowInterceptTouchEvent(false);
                loadingLayout.setVisibility(INVISIBLE);
                getContentView().setVisibility(GONE);
        }
    }

    public void setResultState(int state) {
        this.resultState = state;
    }

    public int getResultState() {
        return resultState;
    }

    public void setMsgLoading(String msg_loading) {
        this.msg_loading = msg_loading;
    }

    public void setResultNoData(String result_no_data) {
        this.result_no_data = result_no_data;
    }

    public void setResultNetError(String result_net_error) {
        this.result_net_error = result_net_error;
    }

    public void setResultOtherError(String result_ohter_error) {
        this.result_ohter_error = result_ohter_error;
    }

    /////////////代理
    //
    public void setOnResultClickListener(View.OnClickListener onResultClickListener) {
        loadingLayout.setOnResultClickListener(onResultClickListener);
    }

    public void setRefreshAsyncTime(int refreshAsyncTime) {
        this.refreshAsyncTime = refreshAsyncTime;
    }
}
