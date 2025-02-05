package g.api.http;

import android.util.Log;

import java.util.Map;


public class GHttpLog {
    private static GHttpLog instance;

    private GHttpLog() {}

    public static GHttpLog getInstance() {
        if (instance == null) {
            instance = new GHttpLog();
            instance.setLogger(new DefaultLogger());
        }
        return instance;
    }

    private boolean LOG_SWITCH = true;

    private Logger logger;

    public void setLogSwitch(boolean logSwitch) {
        LOG_SWITCH = logSwitch;
    }

    public boolean getLogSwitch() {
        return LOG_SWITCH;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void LogRequestData(GRequestData requestData) {
        if (requestData == null)
            return;
        if (LOG_SWITCH && logger != null) {
            LogRequestUrl(requestData.getUrl());
            if (requestData.getParams() != null) {
                LogRequestHeader(requestData.getParams().getHeaders());
                LogRequestParams(requestData.getParams().getBodyParams());
            }
        }
    }

    public void LogRequestUrl(String url) {
        if (url == null)
            return;
        if (LOG_SWITCH && logger != null) {
            logger.d("http-url->>>>>", url + " ###############################################################################");
        }
    }


    public void LogRequestHeader(Map<String, String> header) {
        if (header == null)
            return;
        if (LOG_SWITCH && logger != null) {
            StringBuffer headerBuf = new StringBuffer(200);
            for (String key : header.keySet()) {
                headerBuf.append(key);
                headerBuf.append(":");
                headerBuf.append(header.get(key));
                headerBuf.append(",");
            }
            logger.d("http-header->>", headerBuf.toString());
        }
    }

    public void LogRequestParams(Map<String, String> params) {
        if (params == null)
            return;
        if (LOG_SWITCH && logger != null) {
            StringBuffer paramsBuf = new StringBuffer(200);
            for (String key : params.keySet()) {
                paramsBuf.append(key);
                paramsBuf.append("=");
                paramsBuf.append(params.get(key));
                paramsBuf.append("&");
            }
            logger.d("http-params->>", paramsBuf.toString());
        }
    }

    public void LogResponseString(String result) {
        if (LOG_SWITCH && logger != null)
            if (result != null)
                logger.json("http-result->>", result);
    }

    public void LogHttpError(Exception e) {
        if (LOG_SWITCH && logger != null)
            logger.e(e, "http-error->>>>", e.toString());
    }

    public void LogJsonException(Exception e) {
        if (LOG_SWITCH && logger != null)
            logger.e(e, "json-exception->>>>", e.toString());
    }

    public interface Logger {
        public void v(String tag, String msg);

        public void d(String tag, String msg);

        public void i(String tag, String msg);

        public void w(String tag, String msg);

        public void e(Throwable throwable, String tag, String msg);

        public void json(String tag, String json);
    }

    public static class DefaultLogger implements Logger {

        @Override
        public void v(String tag, String msg) {
            Log.v(tag, msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        @Override
        public void i(String tag, String msg) {
            Log.i(tag, msg);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        @Override
        public void e(Throwable throwable, String tag, String msg) {
            Log.e(tag, msg);
        }

        @Override
        public void json(String tag, String json) {
            if (json != null) {
                int max = 3900;//不要超过控制台最大输出字符数量
                int num = json.length() / max + 1;
                for (int i = 0; i < num; i++) {
                    if (i == num - 1)
                        Log.d(tag, json.substring(i * max));
                    else
                        Log.d(tag, json.substring(i * max, (i + 1) * max));
                }
            }
        }
    }
}
