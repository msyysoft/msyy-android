package g.api.tools.gsafe;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtil {
    public static String toHMACSHA256String(String publicKey, String privateKey) {
        String mykey = privateKey;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(mykey.getBytes(),
                    "HmacSHA256");
            mac.init(secret);
            byte[] digest = mac.doFinal(publicKey.getBytes());
            return Hex.encodeHexStr(digest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String EncoderByMd5(String str)
            throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("md5");

        md5.update(str.getBytes());
        byte[] nStr = md5.digest();
        return bytes2Hex(nStr);
    }

    public static String EncoderBySHA1(String str)
            throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        sha1.update(str.getBytes());
        byte[] nStr = sha1.digest();
        return bytes2Hex(nStr);
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;

        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des = des + "0";
            }
            des = des + tmp;
        }
        return des;
    }
    // 加密
    public static String getBase64(String str, String charsetName) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = Base64.encodeToString(b,Base64.DEFAULT);
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s, String charsetName) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            try {
                b = Base64.decode(s,Base64.DEFAULT);
                result = new String(b, charsetName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}