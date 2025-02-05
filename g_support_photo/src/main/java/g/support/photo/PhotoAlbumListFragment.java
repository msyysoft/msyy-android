package g.support.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import g.api.adapter.GBaseAdapter;
import g.api.event.GEvent;
import g.api.tools.T;
import g.support.app.FragmentShellActivity;
import g.support.app.permission.AndPermission;
import g.support.app.permission.PermissionListener;
import g.support.loading.SimpleDialog;

/**
 * 相册列表
 */
public class PhotoAlbumListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private PhotoAlbumListListAdapter adapter;
    private boolean isDataChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(GEvent event) {
        if (PhotoConfig.PHOTO_DELETE.equals(event.getTag())) {
            isDataChanged = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_fragment_photo_album_list, container, false);
        setup(rootView);
        return T.getNoParentView(rootView);
    }

    private void setup(View view) {
        TextView tv_top_title = (TextView) view.findViewById(R.id.tv_top_title);
        tv_top_title.setText("相册列表");
        view.findViewById(R.id.iv_top_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ListView lv_list = (ListView) view.findViewById(R.id.lv_list);
        adapter = new PhotoAlbumListListAdapter();
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(this);
        lv_list.setOnItemLongClickListener(this);
        onRefresh(false, false);
    }

    public void onRefresh(boolean isLoadMore, boolean isFromPTR, int... flag) {
        if (getActivity() == null)
            return;
        try {
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
            T.showToast(getActivity(), "读取外部存储权限获取失败");
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataChanged) {
            isDataChanged = false;
            onRefresh(false, false);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoAlbumListListAdapter adapter = (PhotoAlbumListListAdapter) parent.getAdapter();
        PhotoAlbumData photoAlbumData = adapter.getItem(position);
        Bundle args = new Bundle(getArguments());
        args.putSerializable(PhotoConfig.PHOTO_ALBUM_DATAS_STR, photoAlbumData);
        Intent intent = FragmentShellActivity.createIntent(parent.getContext(), PhotoAlbumFragment.class, args);
        if (intent != null)
            startActivityForResult(intent, PhotoConfig.REQUEST_CODE_ALBUM);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final PhotoAlbumData albumData = adapter.getItem(position);
        boolean canDelete = true;
        for (PhotoData p : albumData.datas) {
            if (p.isSelect) {
                canDelete = false;
                break;
            }
        }
        if (canDelete) {
            AlertDialog dialog = SimpleDialog.createDialog(parent.getContext(), "提示", "是否要删除相册【" + albumData.albumName + "】？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (PhotoData p : albumData.datas) {
                        File file = new File(p.photoFilePath);
                        file.delete();
                    }
                    File file = new File(albumData.firstData.photoFilePath);
                    File fileDir = file.getParentFile();
                    fileDir.delete();
                    adapter.getDatas().remove(albumData);
                    adapter.notifyDataSetChanged();
                }
            }, true);
            if (dialog != null)
                dialog.show();
        }
        return true;
    }

    private static class PhotoAlbumListListAdapter extends GBaseAdapter<PhotoAlbumData> {

        private DisplayImageOptions options;

        public PhotoAlbumListListAdapter() {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(new ColorDrawable(Color.TRANSPARENT))
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return getRealCount();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Context context = parent.getContext();
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.photo_adapter_list_photo_alubm_list, null);
                holder.iv_item_0 = (ImageView) convertView.findViewById(R.id.iv_item_0);
                holder.tv_item_0 = (TextView) convertView.findViewById(R.id.tv_item_0);
                holder.tv_item_1 = (TextView) convertView.findViewById(R.id.tv_item_1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoAlbumData data = getItem(position);

            holder.tv_item_0.setText(data.albumName);
            holder.tv_item_1.setText(data.datas.size() + "张");
            ImageLoader.getInstance().displayImage(PhotoUtils.getDisplayUri(data.firstData.photoFilePath), holder.iv_item_0, options);

            return convertView;
        }

        private static class ViewHolder {
            public ImageView iv_item_0;
            public TextView tv_item_0;
            public TextView tv_item_1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case PhotoConfig.REQUEST_CODE_ALBUM:
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, permissionListener);
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissonYES(int i, String[] strings) {
            adapter.setDatas(PhotoUtils.getAllAlbum(getActivity()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onPermissonNO(int i, String[] strings) {
            if (getActivity() != null) {
                T.showToast(getActivity(), "读取外部存储权限获取失败");
                getActivity().finish();
            }
        }

        @Override
        public void onPermissonDENY(int i, String[] strings) {
            if (getActivity() != null) {
                T.showToast(getActivity(), "该功能需要读取外部存储权限才能使用");
                T.openAppSettings(getActivity());
                getActivity().finish();
            }
        }
    };
}
