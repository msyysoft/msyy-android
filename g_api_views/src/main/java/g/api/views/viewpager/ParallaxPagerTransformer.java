package g.api.views.viewpager;

import android.os.Build;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ParallaxPagerTransformer implements ViewPager.PageTransformer {

    private int id;
    private int border = 0;
    private float speed = 2f / 3f;
    private boolean useBorder = false;

    public ParallaxPagerTransformer(int id) {
        this.id = id;
    }

    @Override
    public void transformPage(View view, float position) {

        View parallaxView = view.findViewById(id);

        if (parallaxView != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (position > -1 && position < 1) {
                float width = parallaxView.getWidth();
                parallaxView.setTranslationX(-(position * width * speed));
                if (useBorder) {
                    float sc = ((float) view.getWidth() - border) / view.getWidth();
                    if (position == 0) {
                        view.setScaleX(1);
                        view.setScaleY(1);
                    } else {
                        view.setScaleX(sc);
                        view.setScaleY(sc);
                    }
                }
            }
        }
    }

    public void setBorder(int px) {
        border = px;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setUseBorder(boolean useBorder) {
        this.useBorder = useBorder;
    }
}
