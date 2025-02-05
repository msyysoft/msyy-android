package g.api.http;

import g.api.http.volley.Request;

public class GRequestData {
    private int method;
    private String url;
    private GRequestParams params;
    private String tag;

    public GRequestData(String url, GRequestParams params) {
        this(Request.Method.POST, url, params);
    }

    public GRequestData(int method, String url, GRequestParams params) {
        this.method = method;
        this.url = url;
        this.params = params;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GRequestParams getParams() {
        if (params == null)
            params = new GRequestParams();
        return params;
    }

    public void setParams(GRequestParams params) {
        this.params = params;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}