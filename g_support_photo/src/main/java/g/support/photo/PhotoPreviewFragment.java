package g.support.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import g.api.tools.T;
import g.api.views.photoview.PhotoView;
import g.api.views.viewpager.HackyViewPager;
import g.api.views.viewpager.RecyclingPagerAdapter;
import g.api.views.viewpager.ViewPagerIndicator;
import g.support.app.FragmentShellActivity;
import g.support.app.alistener.ActivityFinishListener;

public class PhotoPreviewFragment extends Fragment implements ViewPager.OnPageChangeListener, ActivityFinishListener {
    private HackyViewPager vp_photo;
    private TextView tv_top_title;
    private RelativeLayout rl_select;
    private CheckBox cb_select;
    private PhotoAlbumData selectData;
    private int mPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getActivity() instanceof FragmentShellActivity) {
            ((FragmentShellActivity) getActivity()).setActivityFinishListener(this);
        }
        super.onCreate(savedInstanceState);
        try {
            selectData = (PhotoAlbumData) getArguments().getSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR);
        } catch (Exception e) {
            selectData = new PhotoAlbumData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment_photo_preview, container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    private void setup(View view) {
        tv_top_title = (TextView) view.findViewById(R.id.tv_top_title);
        rl_select = (RelativeLayout) view.findViewById(R.id.rl_select);
        cb_select = (CheckBox) view.findViewById(R.id.cb_select);
        rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoData data = selectData.datas.get(mPosition);
                if (data.isSelect) {
                    data.isSelect = false;
                } else {
                    data.isSelect = true;
                }
                cb_select.setChecked(data.isSelect);
            }
        });
        view.findViewById(R.id.iv_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        vp_photo = (HackyViewPager) view.findViewById(R.id.vp_photo);
        PhotoPreviewPagerAdapter adapter = new PhotoPreviewPagerAdapter(vp_photo);
        adapter.setDatas(selectData.datas);
        vp_photo.setAdapter(adapter);

        ViewPagerIndicator vpi_photo = (ViewPagerIndicator) view.findViewById(R.id.vpi_photo);
        vpi_photo.setOnPageChangeListener(this);
        vpi_photo.setRadius(T.dip2px(view.getContext(), 3));
        vpi_photo.setCentered(true);
        vpi_photo.setSelectedColor(T.getThemeColor(view.getContext(), R.attr.gc_theme, Color.BLUE));
        vpi_photo.setUnselectColor(0xFFFFFFFF);
        vpi_photo.getPaintUnselect().setStrokeWidth(1f);
        vpi_photo.setViewPager(vp_photo);
        vpi_photo.setItemsCount(selectData.datas.size());
        if (selectData.datas.size() != 0)
            onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        cb_select.setChecked(selectData.datas.get(position).isSelect);
        tv_top_title.setText((position + 1) + "/" + selectData.datas.size());
    }

    @Override
    public void whenActivityBeforeFinish(AppCompatActivity absBaseActivity) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        List<PhotoData> photoDatas = new ArrayList<PhotoData>();
        for (PhotoData photoData : selectData.datas) {
            if (photoData.isSelect) {
                photoDatas.add(photoData);
            }
        }
        bundle.putSerializable(PhotoConfig.PHOTO_SELECT_DATAS_STR, new PhotoAlbumData(photoDatas));
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void whenActivityAfterFinish(AppCompatActivity absBaseActivity) {
    }

    private static class PhotoPreviewPagerAdapter extends RecyclingPagerAdapter {
        private ViewPager viewPager;
        private List<PhotoData> datas;
        private DisplayImageOptions options;

        public PhotoPreviewPagerAdapter(ViewPager viewPager) {
            this.viewPager = viewPager;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new ColorDrawable(Color.TRANSPARENT))
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        public void setDatas(List<PhotoData> datas) {
            this.datas = datas;
        }

        public List<PhotoData> getDatas() {
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

        public PhotoData getItem(int i) {
            return datas.get(i);
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(viewPager.getContext()).inflate(R.layout.photo_adapter_pager_photo_preview, container, false);
                holder.pv_photo = (PhotoView) view.findViewById(R.id.pv_photo);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ImageLoader.getInstance().displayImage(PhotoUtils.getDisplayUri(getItem(position).photoFilePath), holder.pv_photo, options);
            return view;
        }

        @Override
        public int getCount() {
            return getRealCount();
        }

        private static class ViewHolder {
            PhotoView pv_photo;
        }
    }

}
