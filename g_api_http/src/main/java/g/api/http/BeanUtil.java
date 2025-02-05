package g.api.http;

public class BeanUtil {
    public static boolean getBoolean(Boolean v) {
        return v == null ? false : v.booleanValue();
    }

    public static double getDouble(Double v) {
        return v == null ? 0 : v.doubleValue();
    }

    public static float getFloat(Float v) {
        return v == null ? 0 : v.floatValue();
    }

    public static int getInteger(Integer v) {
        return v == null ? 0 : v.intValue();
    }

    public static long getLong(Long v) {
        return v == null ? 0 : v.longValue();
    }
}
