package g.api.views.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class G3ScrollView extends XScrollView {
    private LinearLayout ll_content;
    private LinearLayout ll_header;
    private LinearLayout ll_footer;
    private RelativeLayout rl_body;

    public G3ScrollView(Context context) {
        super(context);
        setup(context, null);
    }

    public G3ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    protected void setup(Context context, AttributeSet attrs) {
        ll_content = new LinearLayout(context);
        ll_content.setOrientation(LinearLayout.VERTICAL);
        ll_header = new LinearLayout(context);
        ll_header.setOrientation(LinearLayout.VERTICAL);
        ll_footer = new LinearLayout(context);
        ll_footer.setOrientation(LinearLayout.VERTICAL);
        rl_body = new RelativeLayout(context);
        ll_content.addView(ll_header);
        ll_content.addView(rl_body);
        ll_content.addView(ll_footer);
        addView(ll_content);
    }

    public LinearLayout getHeader() {
        return ll_header;
    }

    public LinearLayout getFooter() {
        return ll_footer;
    }

    public RelativeLayout getBody() {
        return rl_body;
    }
}
