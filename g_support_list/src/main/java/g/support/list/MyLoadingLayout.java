package g.support.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.support.list.R;

public class MyLoadingLayout extends RelativeLayout {
    private RelativeLayout rl_result;
    private RelativeLayout rl_loading;
    //private ImageView iv_result;
    private TextView tv_result;
    private ProgressBar pb_loading;
    private TextView tv_loading;

    private OnClickListener onResultClickListener;

    public MyLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public MyLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnResultClickListener(OnClickListener onResultClickListener) {
        this.onResultClickListener = onResultClickListener;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.g_layout_loading, this);
        rl_result = (RelativeLayout) findViewById(R.id.rl_result);
        rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
        rl_result.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onResultClickListener != null)
                    onResultClickListener.onClick(MyLoadingLayout.this);
            }
        });//绑定传入事件
        rl_loading.setOnClickListener(null);//屏蔽触摸事件

        //iv_result = (ImageView) findViewById(R.id.iv_result);
        tv_result = (TextView) findViewById(R.id.tv_result);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
    }


    private void startLoading() {
        pb_loading.setVisibility(VISIBLE);
    }

    private void stopLoading() {
        pb_loading.setVisibility(GONE);
    }

    public void showLoading(String msg) {
        rl_loading.setVisibility(VISIBLE);
        rl_result.setVisibility(GONE);
        tv_loading.setText(msg == null ? "加载中..." : msg);
        startLoading();
    }

    public void showResultNoData(String msg) {
        rl_loading.setVisibility(GONE);
        rl_result.setVisibility(VISIBLE);
        stopLoading();
        //iv_result.setImageResource(R.drawable.share_ic_error_no_data);
        tv_result.setText(msg == null ? "暂无数据" : msg);
    }

    public void showResultNetError(String msg) {
        rl_loading.setVisibility(GONE);
        rl_result.setVisibility(VISIBLE);
        stopLoading();
        //iv_result.setImageResource(R.drawable.share_ic_error_net);
        tv_result.setText(msg == null ? "您的网络似乎已经断开，请检查网络设置" : msg);
    }

    public void showResultOtherError(String msg) {
        rl_loading.setVisibility(GONE);
        rl_result.setVisibility(VISIBLE);
        stopLoading();
        //iv_result.setImageResource(R.drawable.share_ic_error_other);
        tv_result.setText(msg == null ? "请求失败了，请刷新试试" : msg);
    }

    /**
     * 重写改变背景颜色
     *
     * @param color
     */
    @Override
    public void setBackgroundColor(int color) {
        rl_loading.setBackgroundColor(color);
        rl_result.setBackgroundColor(color);
    }
}
