package g.api.tools.gsafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import g.api.tools.T;
import g.api.tools.cache.SCache;

public class GDeviceIDFactory {
    /**
     * 获取设备ANDROID_ID
     */
    public static String getAndroidID(Context context) {
        String id = null;
        if (context != null) {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return "9774d56d682e549c".equals(id) ? null : id;
    }

    /**
     * 获取设备IMEI[android.permission.READ_PHONE_STATE]
     * 安卓6.0以上系统获取不到
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    @Deprecated
    public static String getDeviceIMEI(Context context) {
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取设备指纹
     *
     * @return
     */
    public static String getDeviceFINGERPRINT(Context context) {
        String w_h_dp = "";
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            w_h_dp = dm.widthPixels + "_" + dm.heightPixels + "_" + dm.density;
        }
        return android.os.Build.FINGERPRINT + '/' + w_h_dp;
    }


    /**
     * 生成设备唯一标识
     *
     * @param context
     * @return
     */
    public static String getDeviceUniqueID(Context context) {
        String uniqueID = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && T.isEmpty(uniqueID)) {
            uniqueID = getDeviceIMEI(context);
        }

        if (T.isEmpty(uniqueID)) {
            uniqueID = getAndroidID(context);
        }

        if (!T.isEmpty(uniqueID)) {
            uniqueID += getDeviceFINGERPRINT(context);
            try {
                uniqueID = SecurityUtil.EncoderByMd5(uniqueID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (T.isEmpty(uniqueID)) {

            File extPath = new File(new File(Environment.getExternalStorageDirectory(), "Android"), ".gy2uuid");
            String id_gen_ext = null;
            try {
                if (extPath.exists())
                    id_gen_ext = readInstallationFile(extPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            SCache sc = new SCache(context);
            String id_gen_xml = sc.getString("DEVICE_CODE_GEN", "");

            if (!T.isEmpty(id_gen_ext)) {
                uniqueID = id_gen_ext;
            } else if (!T.isEmpty(id_gen_xml)) {
                uniqueID = id_gen_xml;
            } else {
                uniqueID = "gen" + T.generateRandomString(29);
            }

            try {
                sc.putString("DEVICE_CODE_GEN", uniqueID);
                if (!extPath.exists())
                    writeInstallationFile(extPath, uniqueID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uniqueID;
    }

    private static String readInstallationFile(File path) throws Exception {
        RandomAccessFile f = new RandomAccessFile(path, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File path, String id) throws Exception {
        FileOutputStream out = new FileOutputStream(path);
        out.write(id.getBytes());
        out.close();
    }
}
