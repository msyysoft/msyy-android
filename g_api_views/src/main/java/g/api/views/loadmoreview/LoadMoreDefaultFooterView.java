package g.api.views.loadmoreview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import g.api.views.R;


public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;

    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.cube_views_load_more_default_footer, this);
        mTextView = (TextView) findViewById(R.id.cube_views_load_more_default_footer_text_view);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        mTextView.setVisibility(VISIBLE);
        mTextView.setText(R.string.cube_views_load_more_loading);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (hasMore) {
            mTextView.setVisibility(VISIBLE);
        } else {
            mTextView.setVisibility(GONE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        mTextView.setVisibility(VISIBLE);
        mTextView.setText(R.string.cube_views_load_more_click_to_load_more);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        mTextView.setText(R.string.cube_views_load_more_error);
    }
}
