package g.support.photo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import g.api.adapter.GViewHolder;
import g.api.tools.T;
import g.api.views.photoview.PhotoView;
import g.api.views.photoview.PhotoViewAttacher;
import g.api.views.photoview.multitouch.RotateGestureDetector;
import g.api.views.viewpager.HackyViewPager;
import g.api.views.viewpager.RecyclingPagerAdapter;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class PhotoBigItemFragment extends Fragment {

    private HackyViewPager vp_photo;
    private TextView tv_index;

    private List<String> photos = new ArrayList<>();
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> list = args.getStringArrayList("PHOTOS_LIST");
            if (list != null)
                photos.addAll(list);
            position = args.getInt("POSITION", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment_big_photo_item, container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    private void setup(View view) {
        vp_photo = (HackyViewPager) view.findViewById(R.id.vp_photo);
        PhotoPreviewPagerAdapter adapter = new PhotoPreviewPagerAdapter(getActivity());
        adapter.setDatas(photos);
        vp_photo.setAdapter(adapter);

        tv_index = view.findViewById(R.id.tv_index);
        tv_index.setText(getIndex(position, photos.size()));

        vp_photo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position;
                tv_index.setText(getIndex(position, photos.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (photos.size() > 0) {
            tv_index.setText(getIndex(position, photos.size()));
            vp_photo.setCurrentItem(position);
        }

    }

    private String getIndex(int position, int size) {
        return (position + 1) + "/" + size;
    }

    private static class PhotoPreviewPagerAdapter extends RecyclingPagerAdapter {
        private Activity activity;
        private List<String> datas;
        private DisplayImageOptions options;

        public PhotoPreviewPagerAdapter(Activity activity) {
            this.activity = activity;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new ColorDrawable(Color.TRANSPARENT))
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        public void setDatas(List<String> datas) {
            this.datas = datas;
        }

        public List<String> getDatas() {
            return datas;
        }

        public boolean hasDatas() {
            return datas != null && datas.size() > 0;
        }

        public int getRealCount() {
            if (hasDatas())
                return datas.size();
            return 0;
        }

        public String getItem(int i) {
            return datas.get(i);
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder(activity, new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        activity.finish();
                    }
                });
                view = holder.getConvertView();
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.showData(getItem(position), options, position);
            return view;
        }

        @Override
        public int getCount() {
            return getRealCount();
        }

        private static class ViewHolder extends GViewHolder {
            private PhotoView pv_photo;
            private ProgressBar pb_photo;
            private float mRotationDegreesOld = 0.f;
            private float mRotationDegrees = 0.f;
            private RotateGestureDetector mRotateDetector;

            public ViewHolder(Context context, PhotoViewAttacher.OnPhotoTapListener tapListener) {
                super(context);
                pv_photo = (PhotoView) findViewById(R.id.pv_photo);
                pb_photo = (ProgressBar) findViewById(R.id.pb_photo);
                pv_photo.setOnPhotoTapListener(tapListener);
                mRotateDetector = new RotateGestureDetector(context, new RotateListener());
                pv_photo.setOnNewTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent ev) {
                        mRotateDetector.onTouchEvent(ev);
                        pv_photo.setRotationBy(mRotationDegrees - mRotationDegreesOld);
                        mRotationDegreesOld = mRotationDegrees;
                        switch (ev.getAction()) {
                            case ACTION_DOWN:
                                break;
                            case ACTION_CANCEL:
                            case ACTION_UP:
                                int r = (int) (mRotationDegrees / 90);
                                double s = mRotationDegrees - r * 90;
                                if (Math.abs(s) > 45)
                                    r = r + (s >= 0 ? 1 : -1);
                                mRotationDegrees = (float) (r * 90);
                                pv_photo.setRotationBy(mRotationDegrees - mRotationDegreesOld);
                                pv_photo.setAllScaleByRotate90(r * 90);
                                mRotationDegreesOld = mRotationDegrees;
                                break;
                        }
                        return true; // indicate event was handled
                    }
                });
            }

            @Override
            protected int onSetConvertViewResId() {
                return R.layout.photo_adapter_pager_photo_preview;
            }

            protected void showData(String url, DisplayImageOptions options, int position) {
                ImageLoader.getInstance().displayImage(PhotoUtils.getDisplayUri(url), pv_photo, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        pb_photo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }

            private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
                @Override
                public boolean onRotate(RotateGestureDetector detector) {
                    mRotationDegrees -= detector.getRotationDegreesDelta();
                    return true;
                }
            }
        }
    }
}
