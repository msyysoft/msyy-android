package g.api.tools.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.File;
import java.util.Locale;

public class CacheCleanManager {
    /**
     * 文件删除规则
     */
    public interface FileDeleteRule {
        boolean deleteThisFile(File file);

        boolean deleteThisDir(File file);
    }

    /**
     * 文件大小计算规则
     */
    public interface FileSizeRule {
        boolean sizeThisFile(File file);
    }

    public static long sizeFile(File file, FileSizeRule fileSizeRule) {
        long fileSize = 0;
        try {
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    File[] fileList = file.listFiles();
                    int size = fileList.length;
                    for (int i = 0; i < size; i++) {
                        fileSize = fileSize + sizeFile(fileList[i], fileSizeRule);
                    }
                } else if (file.isFile()) {
                    if (fileSizeRule.sizeThisFile(file))
                        return file.length();
                    else
                        return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSize;
    }

    /**
     * 不删除文件夹
     *
     * @param dirOrFile
     * @param fileDeleteRule
     */
    public static void deleteFileWithRull(File dirOrFile, FileDeleteRule fileDeleteRule) {
        try {
            if (dirOrFile != null && dirOrFile.exists()) {
                if (dirOrFile.isDirectory()) {
                    File files[] = dirOrFile.listFiles();
                    int size = files.length;
                    for (int i = 0; i < size; i++) {
                        deleteFileWithRull(files[i], fileDeleteRule);
                    }
                    if (dirOrFile.listFiles().length == 0) {
                        if (fileDeleteRule.deleteThisDir(dirOrFile))
                            dirOrFile.delete();
                    }
                } else if (dirOrFile.isFile()) {
                    if (fileDeleteRule.deleteThisFile(dirOrFile))
                        dirOrFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化单位
     *
     * @param s 单位byte
     * @return
     */
    public static String getFormatSize(double s) {
        String[] u = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB", "BB"};
        double c = s;
        int f = 0;
        while (c > 1024) {
            c /= 1024;
            f++;
            if (f >= (u.length - 1))
                break;
        }

        return String.format(Locale.CHINA, "%4.2f " + u[f], c);
    }


    /**
     * 异步显示缓存大小
     */
    public static class CacheSizeAsyncTask extends AsyncTask<File, Void, String> {
        protected Context context;
        private TextView textView;

        public CacheSizeAsyncTask(TextView textView) {
            this.textView = textView;
            context = textView.getContext();
        }

        @Override
        protected String doInBackground(File... params) {
            return getFormatSize(sizeFile(params[0], new FileSizeRule() {
                @Override
                public boolean sizeThisFile(File file) {
                    return file.getName().contains(".") || file.getName().contains("cache");
                }
            }));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }

    public static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }
}
