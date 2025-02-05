package g.support.loading;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.thin.downloadmanager.ThinDownloadManager;

import g.api.download.DownloadUtils;
import g.api.tools.T;

public class FileDownloadDialog {
    private FileDownloadDialog() {
    }

    /**
     * 创建文件下载对话框
     *
     * @param context
     * @param title
     * @param msg
     * @return
     */
    public static AlertDialog createDialog(Context context, CharSequence title, CharSequence msg, final DownloadUtils.DownloadListener downloadListener, final String downloadUrl, final String fileDir, final String fileName, final String tag) {

        if (context == null || !(context instanceof Activity) || ((Activity) context).isFinishing())
            return null;
        if (!downloadUrl.startsWith("http"))
        {
            T.showToast(context,"下载地址有误");
            return null;
        }

        final ThinDownloadManager downloadManager = new ThinDownloadManager(1);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, T.getTheme(context,R.attr.g_theme_dialog));
        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog_file_download, null);
        final TextView tv_info = (TextView) view.findViewById(R.id.tv_info);
        final NumberProgressBar npb_progress = (NumberProgressBar) view.findViewById(R.id.npb_progress);
        builder.setView(view);
        if (!T.isEmpty(title))
            builder.setTitle(title);
        if (!T.isEmpty(msg))
            builder.setMessage(msg);
        builder.setNegativeButton("取消下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (downloadManager != null) {
                    downloadManager.cancelAll();
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                DownloadUtils.download(downloadManager, new DownloadUtils.DownloadListener() {
                    @Override
                    public void onDownloadComplete(String downloadUrl, String tag) {
                        if (downloadListener != null)
                            downloadListener.onDownloadComplete(downloadUrl, tag);
                        dialog.dismiss();
                    }

                    @Override
                    public void onDownloadFailed(String downloadUrl, String tag, int errorCode) {
                        if (downloadListener != null)
                            downloadListener.onDownloadFailed(downloadUrl, tag, errorCode);
                        dialog.dismiss();
                    }

                    @Override
                    public void onProgress(long totalBytes, long downloadedBytes, int progress) {
                        if (downloadListener != null)
                            downloadListener.onProgress(totalBytes, downloadedBytes, progress);
                        tv_info.setText("已下载：" + DownloadUtils.getBytesDownloaded(progress, totalBytes));
                        npb_progress.setProgress(progress);
                    }
                }, downloadUrl, fileDir, fileName, tag);
            }
        });

        return dialog;
    }
}
