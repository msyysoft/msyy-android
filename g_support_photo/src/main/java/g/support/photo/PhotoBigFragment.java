package g.support.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import g.api.adapter.GFragmentPagerAdapter;
import g.api.tools.T;
import g.api.views.viewpager.HackyViewPager;

public class PhotoBigFragment extends Fragment {

    private TabLayout tab_title;

    private ViewPager vp_photo_frame;

    private ArrayList<String>[] photos = new ArrayList[2];
    private int[] position = new int[2];
    private String[] title = new String[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            ArrayList<String> list_1 = args.getStringArrayList("PHOTOS_LIST_1");
            ArrayList<String> list_2 = args.getStringArrayList("PHOTOS_LIST_2");
            photos[0] = new ArrayList<>();
            photos[1] = new ArrayList<>();
            if (list_1 != null)
                photos[0].addAll(list_1);
            if (list_2 != null)
                photos[1].addAll(list_2);
            title[0] = args.getString("TITLE_1");
            title[1] = args.getString("TITLE_2");
            position[0] = args.getInt("POSITION_1", 0);
            position[1] = args.getInt("POSITION_2", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment_big_photo, container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    private void setup(View view) {
        tab_title = view.findViewById(R.id.tab_title);
        vp_photo_frame = (HackyViewPager) view.findViewById(R.id.vp_photo_frame);
        GFragmentPagerAdapter adapter = new GFragmentPagerAdapter(view.getContext(), getChildFragmentManager());
        vp_photo_frame.setAdapter(adapter);
        List<GFragmentPagerAdapter.GFragmentPageInfo> pageList = new ArrayList<>();
        int count = 0;
        if (photos[0].size() > 0)
            count++;
        if (photos[1].size() > 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            Bundle args = new Bundle();
            if (i == 0) {
                args.putBoolean("TAB_IS_DEFAULT", true);
            }
            args.putStringArrayList("PHOTOS_LIST", photos[i]);
            args.putInt("POSITION", position[i]);

            pageList.add(new GFragmentPagerAdapter.GFragmentPageInfo(i, title[i] + "(" + photos[i].size() + ")", PhotoBigItemFragment.class, args));
        }
        adapter.setDatas(pageList);
        adapter.notifyDataSetChanged();

        tab_title.setupWithViewPager(vp_photo_frame);

        if (title[0] != null || title[1] != null) {
            tab_title.setVisibility(View.VISIBLE);
        } else {
            tab_title.setVisibility(View.GONE);
        }
    }

}
