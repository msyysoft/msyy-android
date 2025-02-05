package g.api.adapter;

import android.view.View;

public class GTagUtil {
    public static void setTag(Object tag, View childView) {
        if (childView != null)
            childView.setTag(tag);
    }

    public static Object getTag(View childView) {
        if (childView != null)
            return childView.getTag();
        return null;
    }

    public static boolean isDifferentTag(Object tag, View childView) {
        if (tag != null && tag.equals(getTag(childView))) {
            return false;
        } else {
            setTag(tag, childView);
            return true;
        }
    }
}
