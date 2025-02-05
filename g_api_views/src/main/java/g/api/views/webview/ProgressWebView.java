package g.api.views.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import g.api.views.R;

/**
 * 带进度条的WebView
 *
 * @author 农民伯伯
 * @see "http://www.cnblogs.com/over140/archive/2013/03/07/2947721.html"
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
    private ProgressBar progressbar;

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar_states));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(context, 3), 0, 0));
        addView(progressbar);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new ProgressBarWebChromeClient(progressbar));
        WebSettings settings = getSettings();
        if (settings != null) {
            //settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置不使用缓存
            settings.setJavaScriptEnabled(true);//支持js
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setBuiltInZoomControls(true);
            //自适应屏幕
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setDefaultTextEncodingName("utf-8");
            if (Build.VERSION.SDK_INT < 17)//系统有漏洞
                removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }

    public ProgressBar getProgressbar() {
        return progressbar;
    }

    public static class ProgressBarWebChromeClient extends MyWebChromeClient {
        private ProgressBar progressbar;

        public ProgressBarWebChromeClient(ProgressBar progressbar) {
            this.progressbar = progressbar;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public static class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}