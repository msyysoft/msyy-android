package g.api.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GFragmentPagerAdapter<MyAdapterData extends GFragmentPagerAdapter.GFragmentPageInfo> extends FragmentPagerAdapter implements GAdapter<MyAdapterData> {
    public static String TAB_ID = "TAB_ID";
    private List<MyAdapterData> datas = null;
    private Context context = null;

    public GFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = null;
        if (datas != null && pos < datas.size()) {
            GFragmentPageInfo tab = datas.get(pos);
            if (tab == null)
                return null;
            fragment = tab.createFragment();
        }
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        GFragmentPageInfo tab = datas.get(position);
        return tab.getId();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        GFragmentPageInfo tab = datas.get(position);
        return tab.getName();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0)
            return datas.size();
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GFragmentPageInfo tab = datas.get(position);
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tab.fragment = fragment;
        return fragment;
    }

    @Override
    public void setDatas(List<MyAdapterData> datas) {
        this.datas = datas;
    }

    @Override
    public void addDatas(List<MyAdapterData> datas) {
        if (this.datas != null)
            this.datas.addAll(datas);
    }

    @Override
    public List<MyAdapterData> getDatas() {
        if (datas == null)
            return new ArrayList<MyAdapterData>();
        return datas;
    }

    @Override
    public boolean hasDatas() {
        return datas != null && datas.size() > 0;
    }

    @Override
    public int getRealCount() {
        if (hasDatas())
            return datas.size();
        return 0;
    }

    /**
     * 存储fragment及其数据
     */
    public static class GFragmentPageInfo implements Parcelable {
        private int id;
        private String name = null;
        public Fragment fragment = null;
        @SuppressWarnings("rawtypes")
        public Class fragmentClass = null;
        private Bundle args = null;

        @SuppressWarnings("rawtypes")
        public GFragmentPageInfo(int id, String name, Class clazz, Bundle args) {
            super();
            this.name = name;
            this.id = id;
            fragmentClass = clazz;
            this.args = args;
            if (this.args == null)
                this.args = new Bundle();
            this.args.putInt(TAB_ID, id);
        }

        public GFragmentPageInfo(Parcel p) {
            this.id = p.readInt();
            this.name = p.readString();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @SuppressWarnings(
                {
                        "rawtypes", "unchecked"
                })
        public Fragment createFragment() {
            if (fragment == null) {
                Constructor constructor;
                try {
                    constructor = fragmentClass.getConstructor(new Class[0]);
                    fragment = (Fragment) constructor.newInstance(new Object[0]);
                    if (args != null)
                        fragment.setArguments(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return fragment;
        }

        public static final Creator<GFragmentPageInfo> CREATOR = new Creator<GFragmentPageInfo>() {
            public GFragmentPageInfo createFromParcel(Parcel p) {
                return new GFragmentPageInfo(p);
            }

            public GFragmentPageInfo[] newArray(int size) {
                return new GFragmentPageInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel p, int flags) {
            p.writeInt(id);
            p.writeString(name);
        }
    }
}