package g.api.http;


import java.util.HashMap;
import java.util.Map;

import g.api.http.volley.Response;
import g.api.http.volley.VolleyError;
import g.api.http.volley.http.Header;


/**
 * http回调总代理，相当于用户和http底层的桥梁,
 * 用户不去直接使用http底层回调RequestCallBack,
 * 而是使用被代理的回调
 */
class ProxyRequestCallBack implements Response.Listener<String>, Response.ErrorListener {
    private GRequestCallBack requestCallBack;

    public ProxyRequestCallBack(GRequestCallBack requestCallBack) {
        this.requestCallBack = requestCallBack;
    }


    final public void onStart() {
        if (requestCallBack != null) {
            if (!requestCallBack.isStartUse()) {
                requestCallBack.setStartUse(true);
                requestCallBack.onStart();
            }
        }
    }

    @Override
    public void onProgress(long total, long current, boolean isUploading) {
        if (requestCallBack != null)
            requestCallBack.onLoading(total, current, isUploading);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        GHttpLog.getInstance().LogHttpError(volleyError);
        if (requestCallBack != null) {
            requestCallBack.onFailure(volleyError);
        }
    }

    @Override
    public void onResponse(String response) {
        GHttpLog.getInstance().LogResponseString(response);
        if (requestCallBack != null) {
            requestCallBack.onSuccess(response);
        }
    }

    public void onResponseHeaders(Header[] headers) {
        if (requestCallBack != null) {
            if (headers != null) {
                Map<String, String> headerMap = new HashMap<String, String>();
                for (Header h : headers) {
                    headerMap.put(h.getName(), h.getValue());
                }
                requestCallBack.onResponseHeader(headerMap);
            }
        }
    }
}
