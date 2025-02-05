package g.api.download;

import android.net.Uri;
import android.text.TextUtils;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

public class DownloadUtils {

    public interface DownloadListener {
        void onDownloadComplete(String downloadUrl, String tag);

        void onDownloadFailed(String downloadUrl, String tag, int errorCode);

        void onProgress(long totalBytes, long downloadedBytes, int progress);
    }

    public static void download(ThinDownloadManager downloadManager, final DownloadListener downloadListener, final String downloadUrl, String fileDir, String fileName, final String tag) {
        if (downloadManager == null || downloadListener == null || downloadUrl == null || fileDir == null)
            return;
        RetryPolicy retryPolicy = new DefaultRetryPolicy();

        new File(fileDir).mkdirs();

        final String downFilePath = getDownloadFilePath(downloadUrl, fileDir, fileName);
        final String downFilePathTemp = "temp_" + downFilePath;
        Uri downloadUri = Uri.parse(downloadUrl);
        Uri destinationUri = Uri.parse(downFilePathTemp);
        File file = new File(downFilePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        File fileTemp = new File(downFilePathTemp);
        if (fileTemp.exists() && fileTemp.isFile()) {
            fileTemp.delete();
        }
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setRetryPolicy(retryPolicy)
                .setDownloadContext("Download")
                .setStatusListener(new DownloadStatusListenerV1() {
                    @Override
                    public void onDownloadComplete(DownloadRequest downloadRequest) {
                        File fileTemp = new File(downFilePathTemp);
                        if (fileTemp.exists() && fileTemp.isFile()) {
                            if (fileTemp.renameTo(new File(downFilePath))) {
                                downloadListener.onDownloadComplete(downloadUrl, tag);
                                return;
                            }
                        }
                        downloadListener.onDownloadFailed(downloadUrl, tag, DownloadManager.ERROR_FILE_ERROR);
                    }

                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int i, String s) {
                        if (i == 1008) {
                            //下载取消
                            downloadListener.onDownloadFailed(downloadUrl, tag, i);
                        } else {
                            //下载失败
                            downloadListener.onDownloadFailed(downloadUrl, tag, i);
                        }
                    }

                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                        downloadListener.onProgress(totalBytes, downloadedBytes, progress);
                    }
                });
        downloadManager.add(downloadRequest);
    }

    public static String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted = (progress * totalBytes) / 100;
        //1024*1024 = 1048576
        if (totalBytes >= 1048576) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1048576)) + "/" + (String.format("%.1f", (float) totalBytes / 1048576)) + "MB");
        }
        if (totalBytes >= 1024) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1024)) + "/" + (String.format("%.1f", (float) totalBytes / 1024)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }

    public static boolean hasFile(String downloadUrl, String fileDir, String fileName) {
        boolean bm = new File(fileDir).mkdirs();
        if (bm)
            return false;
        String downFilePath = fileDir + "/" + (fileName == null ? getFileName(downloadUrl) : fileName);
        File file = new File(downFilePath);
        return file.exists() && file.isFile();
    }

    public static String getDownloadFilePath(String downloadUrl, String fileDir, String fileName) {
        return fileDir + "/" + (fileName == null ? getFileName(downloadUrl) : fileName);
    }

    /**
     * url2fileName
     *
     * @param url
     * @return url去掉特殊字符后的fileName
     */
    public static String getFileName(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String fileName = new String(url);
        if (fileName.contains("?"))
            fileName = fileName.substring(0, fileName.indexOf("?"));
        if (fileName.contains("&"))
            fileName = fileName.substring(0, fileName.indexOf("&"));
        if (fileName.contains("#"))
            fileName = fileName.substring(0, fileName.indexOf("#"));
        fileName = fileName.replaceAll("/", "");
        fileName = fileName.replaceAll(":", "");
        fileName = fileName.replaceAll("\\\\", "");
        fileName = fileName.replaceAll("\\*", "");
        fileName = fileName.replaceAll("\\?", "");
        fileName = fileName.replaceAll("\\<", "");
        fileName = fileName.replaceAll("\\>", "");
        fileName = fileName.replaceAll("\\|", "");
        if (fileName.length() > 200) {
            return fileName.substring(fileName.length() - 200);
        } else {
            return fileName;
        }
    }
}
