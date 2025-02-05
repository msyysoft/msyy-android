/*
 * Copyright (C) 2015 The Android API Project
 */
package g.api.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;

public class GHttpUtils {
    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetWorkInfo(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo();
    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkOK(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 是否使用的WIFI网络，应先判断isNetworkOK
     *
     * @param context
     * @return
     */
    public static boolean isUseWifi(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);
        if (networkInfo != null) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    /**
     * 是否使用的手机网络，应先判断isNetworkOK
     *
     * @param context
     * @return
     */
    public static boolean isUseMobile(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);
        if (networkInfo != null) {
            return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    /**
     * 手机网络进行详细区分
     *
     * @param context
     * @return -1表示没有使用手机网络，其余请参照TelephonyManager的具体类型
     */
    public static int getUseMobileSubType(Context context) {
        NetworkInfo networkInfo = getNetWorkInfo(context);
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return networkInfo.getSubtype();
        }
        return -1;
    }

    /**
     * first substring
     *
     * @param uri
     * @param indexString
     * @return uri第一个字符串之后的子串
     */
    public static String getFirstSubString(String uri, String indexString) {
        if (isEmpty(uri) || isEmpty(indexString)) {
            return uri;
        }
        int index = uri.indexOf(indexString);
        if (index + indexString.length() > uri.length())
            return uri;
        return uri.substring(index + indexString.length());
    }

    /**
     * last substring
     *
     * @param uri
     * @param indexString
     * @return uri最后一个字符串之后的子串
     */
    public static String getLastSubString(String uri, String indexString) {
        if (isEmpty(uri) || isEmpty(indexString)) {
            return uri;
        }
        int index = uri.lastIndexOf(indexString);
        if (index + indexString.length() > uri.length())
            return uri;
        return uri.substring(index + indexString.length());
    }

    /**
     * uri2simple_fileName
     *
     * @param uri
     * @return uri最后一个反斜杠之后的fileName
     */
    public static String getSimpleFileName(String uri) {
        return getLastSubString(uri, "/");

    }

    /**
     * uri2simple_fileName
     *
     * @param uri
     * @return uri最后一个反斜杠之后的fileName
     */
    public static String getFileDocType(String uri) {
        if (isEmpty(uri)) {
            return "";
        }
        int index = uri.lastIndexOf(".");
        if (index + 1 > uri.length())
            return "";
        return uri.substring(index + 1);

    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * inputStream 2 string
     *
     * @param inputStream inputStream
     * @param charsetName charsetName,default is "UTF-8"
     * @return string
     */
    public static String getStringFromInputStream(InputStream inputStream, String charsetName) {
        if (inputStream == null) {
            throw new NullPointerException();
        }
        if (charsetName == null) {
            charsetName = "UTF-8";
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, charsetName);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 获取T.class
     *
     * @param mClass    传入泛型所在类的引用
     * @param tPosition 泛型位置 一个泛型传0
     * @return 返回泛型的type需要再次转型“Class<T>”
     */
    public static Type getTClass(Object mClass, int tPosition) {
        Class<?> clazz = mClass.getClass();
        Type sType = clazz.getGenericSuperclass();
        while (!(sType instanceof ParameterizedType)) {
            //循环获取带泛型的父类
            clazz = clazz.getSuperclass();
            sType = clazz.getGenericSuperclass();
        }
        ParameterizedType pType = (ParameterizedType) sType;//在上面经过循环判断后，这一步是安全的
        return pType.getActualTypeArguments()[tPosition];//获取泛型类
    }

    /**
     * 生成固定长度的随机字符串
     *
     * @param length 生成字符串的长度
     * @return
     */
    public static String generateRandomString(int length) {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
