package g.api.tools.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import g.api.tools.T;

/**
 * SharedPreferences存储
 */
public class SCache {
    private SharedPreferences s;

    public SCache(Context c) {
        this(c, null);
    }

    public SCache(Context c, String name) {
        this(c, name, Context.MODE_PRIVATE);
    }

    public SCache(Context c, int mode) {
        this(c, "app_save_" + c.getPackageName(), mode);
    }

    public SCache(Context c, String name, int mode) {
        if (c != null)
            if (T.isEmpty(name))
                name = "app_save_" + c.getPackageName();
        s = c.getSharedPreferences(name, mode);
    }

    public void remove(String k) {
        if (s != null) {
            s.edit().remove(k).commit();
        }
    }

    public void putString(String k, String v) {
        if (s != null) {
            s.edit().putString(k, v).commit();
        }
    }

    public Set<String> getStringSet(String k, Set<String> defV) {
        if (s != null && s.contains(k)) {
            return s.getStringSet(k, defV);
        }
        return defV;
    }

    public void putStringSet(String k, Set<String> v) {
        if (s != null) {
            s.edit().putStringSet(k, v).commit();
        }
    }

    public String getString(String k, String defV) {
        if (s != null && s.contains(k)) {
            return s.getString(k, defV);
        }
        return defV;
    }

    public void putInt(String k, int v) {
        if (s != null) {
            s.edit().putInt(k, v).commit();
        }
    }

    public int getInt(String k, int defV) {
        if (s != null && s.contains(k)) {
            return s.getInt(k, defV);
        }
        return defV;
    }

    public void putLong(String k, long v) {
        if (s != null) {
            s.edit().putLong(k, v).commit();
        }
    }

    public long getLong(String k, long defV) {
        if (s != null && s.contains(k)) {
            return s.getLong(k, defV);
        }
        return defV;
    }

    public void putFloat(String k, float v) {
        if (s != null) {
            s.edit().putFloat(k, v).commit();
        }
    }

    public float getFloat(String k, float defV) {
        if (s != null && s.contains(k)) {
            return s.getFloat(k, defV);
        }
        return defV;
    }

    public void putBoolean(String k, boolean v) {
        if (s != null) {
            s.edit().putBoolean(k, v).commit();
        }
    }

    public boolean getBoolean(String k, boolean defV) {
        if (s != null && s.contains(k)) {
            return s.getBoolean(k, defV);
        }
        return defV;
    }
}
