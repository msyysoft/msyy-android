package g.support.loading;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.api.tools.T;


public class LoadingLayout extends RelativeLayout {

    private TextView tv_loading, tv_result;
    private RelativeLayout rl_loading_layout;
    private RelativeLayout rl_loading;

    public LoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.loading_layout, this);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        tv_result = (TextView) findViewById(R.id.tv_result);
        rl_loading_layout = findViewById(R.id.rl_loading_layout);
        rl_loading = findViewById(R.id.rl_loading);
    }

    public RelativeLayout getRl_loading() {
        return rl_loading;
    }

    public RelativeLayout getRl_loading_layout() {
        return rl_loading_layout;
    }

    public TextView getTv_result() {
        return tv_result;
    }

    public void setLoading(boolean isUseMessage, String message) {
        tv_result.setVisibility(GONE);
        if (isUseMessage) {
            tv_loading.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 11) {
                if (message != null && !message.equals("")) {
                    tv_loading.setText(message);
                } else {
                    tv_loading.setText("加载中");
                }
                JumpingBeans.with(tv_loading).appendJumpingDots().build();
            } else {
                if (message != null && !message.equals("")) {
                    tv_loading.setText(message + "...");
                } else {
                    tv_loading.setText("加载中...");
                }
            }
        } else {
            tv_loading.setVisibility(View.GONE);
        }
    }

    public void setResult(String result) {
        if (!T.isEmpty(result)){
            rl_loading.setVisibility(INVISIBLE);
            tv_result.setVisibility(VISIBLE);
            tv_result.setText(result);
        }else{
            rl_loading.setVisibility(VISIBLE);
            tv_result.setVisibility(GONE);
            tv_result.setText("");
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        tv_result.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(GONE);
                if (l != null) {
                    l.onClick(LoadingLayout.this);
                }
            }
        });
    }

    @Override
    public void setBackgroundColor(int color) {
        rl_loading_layout.setBackgroundColor(color);
    }
}