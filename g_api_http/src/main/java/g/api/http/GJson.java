package g.api.http;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GJson {
    public static class GJsonObject extends JSONObject {
        @Override
        public JSONObject put(String name, boolean value) {
            try {
                return super.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
                return this;
            }
        }

        @Override
        public JSONObject put(String name, double value) {
            try {
                return super.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
                return this;
            }
        }

        @Override
        public JSONObject put(String name, int value) {
            try {
                return super.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
                return this;
            }
        }

        @Override
        public JSONObject put(String name, long value) {
            try {
                return super.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
                return this;
            }
        }

        @Override
        public JSONObject put(String name, Object value) {
            try {
                return super.put(name, value);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
                return this;
            }
        }

        public boolean getBoolean(String name, boolean def) {
            try {
                return super.getBoolean(name);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
            }
            return def;
        }

        public double getDouble(String name, double def) {
            try {
                return super.getDouble(name);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
            }
            return def;
        }

        public int getInt(String name, int def) {
            try {
                return super.getInt(name);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
            }
            return def;
        }

        public long getLong(String name, long def) {
            try {
                return super.getLong(name);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
            }
            return def;
        }

        public String getString(String name, String def) {
            try {
                return super.getString(name);
            } catch (JSONException e) {
                e.printStackTrace();
                GHttpLog.getInstance().LogJsonException(e);
            }
            return def;
        }
    }

    public static JSONArray getNewJsonArray(String json) {
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
            GHttpLog.getInstance().LogJsonException(e);
            return null;
        }
    }

    private static <T> T rawJson2Obj(Context context, int rawResId, Class<T> classOfT) {
        return new Gson().fromJson(GHttpUtils.getStringFromInputStream(context.getResources().openRawResource(rawResId), null), classOfT);
    }
}
