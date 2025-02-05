package g.api.http;

import g.api.http.volley.NetworkResponse;
import g.api.http.volley.Response;
import g.api.http.volley.toolbox.StringRequest;

/**
 * 附带headers的StringRequest
 */
public class GStringRequest extends StringRequest {
    private ProxyRequestCallBack listener;

    public GStringRequest(int method, String url, ProxyRequestCallBack listener, ProxyRequestCallBack errorListener) {
        super(method, url, listener, errorListener);
        this.listener = listener;
    }

    public GStringRequest(String url, ProxyRequestCallBack listener, ProxyRequestCallBack errorListener) {
        super(url, listener, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (listener != null) {
            listener.onResponseHeaders(response.apacheHeaders);
        }
        return super.parseNetworkResponse(response);
    }
}
