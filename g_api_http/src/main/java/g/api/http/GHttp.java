package g.api.http;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import g.api.http.volley.AuthFailureError;
import g.api.http.volley.DefaultRetryPolicy;
import g.api.http.volley.RequestQueue;
import g.api.http.volley.ResponseDelivery;
import g.api.http.volley.http.protocol.HTTP;
import g.api.http.volley.toolbox.MultipartRequest;
import g.api.http.volley.toolbox.RequestFuture;
import g.api.http.volley.toolbox.StringRequest;
import g.api.http.volley.toolbox.Volley;

public class GHttp {

    /**
     * 上传文件的bodyparams的key需要加文件前缀标识
     */
    public static final String FILE_PREFIX = MultipartRequest.FILE_PREFIX;
    public static boolean ALLOW_ALL_SC = false;
    public static DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(60 * 1000, 0, 1.0f);

    public void send(final GRequestData requestData, GRequestCallBack callBack) {
        if (requestData == null) return;

        if (callBack != null)
            callBack.setRequestData(requestData);//记录请求参数
        try {
            ProxyRequestCallBack proxyRequestCallBack = new ProxyRequestCallBack(callBack);
            StringRequest request = new StringRequest(requestData.getMethod(), requestData.getUrl(), proxyRequestCallBack, proxyRequestCallBack) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return requestData.getParams().getHeaders();
                }

                @Override
                public String getBodyContentType() {
                    if (requestData.getParams().isJsonParams()) {
                        return "application/json; charset=" + getParamsEncoding();
                    }
                    return super.getBodyContentType();
                }

                @Override
                public DataOutputStream getBody(HttpURLConnection connection, ResponseDelivery delivery) throws IOException, AuthFailureError {
                    if (requestData.getParams().isJsonParams()) {
                        try {
                            String mRequestBody = requestData.getParams().getJsonBody();
                            connection.setDoOutput(true);
                            connection.addRequestProperty(HTTP.CONTENT_TYPE, getBodyContentType());
                            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                            out.write(mRequestBody == null ? null : mRequestBody.getBytes(getParamsEncoding()));
                            return out;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    return super.getBody(connection, delivery);
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return requestData.getParams().getBodyParams();
                }
            };
            request.setRetryPolicy(retryPolicy);
            proxyRequestCallBack.onStart();
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            GHttpLog.getInstance().LogHttpError(e);
        }
    }

    public void sendFile(final GRequestData requestData, GRequestCallBack callBack, boolean chunkMode) {
        if (requestData == null) return;

        if (callBack != null)
            callBack.setRequestData(requestData);//记录请求参数
        try {
            ProxyRequestCallBack proxyRequestCallBack = new ProxyRequestCallBack(callBack);
            MultipartRequest request = new MultipartRequest(requestData.getMethod(), requestData.getUrl(), proxyRequestCallBack, proxyRequestCallBack) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return requestData.getParams().getHeaders();
                }

                @Override
                public String getBodyContentType() {
                    if (requestData.getParams().isJsonParams()) {
                        return "application/json; charset=" + getParamsEncoding();
                    }
                    return super.getBodyContentType();
                }

                @Override
                public DataOutputStream getBody(HttpURLConnection connection, ResponseDelivery delivery) throws IOException, AuthFailureError {
                    if (requestData.getParams().isJsonParams()) {
                        try {
                            String mRequestBody = requestData.getParams().getJsonBody();
                            connection.setDoOutput(true);
                            connection.addRequestProperty(HTTP.CONTENT_TYPE, getBodyContentType());
                            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                            out.write(mRequestBody == null ? null : mRequestBody.getBytes(getParamsEncoding()));
                            return out;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    return super.getBody(connection, delivery);
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return requestData.getParams().getBodyParams();
                }
            };
            request.setUseChunkMode(chunkMode);
            request.setRetryPolicy(retryPolicy);
            proxyRequestCallBack.onStart();
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            GHttpLog.getInstance().LogHttpError(e);
        }
    }

    public <T> T sendSync(final GRequestData requestData, Class<T> tClass, GConverter<T> converter) {
        if (requestData == null) return null;
        try {
            RequestFuture<String> requestFuture = RequestFuture.newFuture();
            StringRequest request = new StringRequest(requestData.getMethod(), requestData.getUrl(), requestFuture, requestFuture) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return requestData.getParams().getHeaders();
                }

                @Override
                public String getBodyContentType() {
                    if (requestData.getParams().isJsonParams()){
                        return "application/json; charset=" + getParamsEncoding();
                    }
                    return super.getBodyContentType();
                }

                @Override
                public DataOutputStream getBody(HttpURLConnection connection, ResponseDelivery delivery) throws IOException, AuthFailureError {
                    if (requestData.getParams().isJsonParams()){
                        try {
                            String mRequestBody = requestData.getParams().getJsonBody();
                            connection.setDoOutput(true);
                            connection.addRequestProperty(HTTP.CONTENT_TYPE, getBodyContentType());
                            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                            out.write(mRequestBody == null ? null : mRequestBody.getBytes(getParamsEncoding()));
                            return out;
                        }catch (Exception e){
                            return null;
                        }
                    }
                    return super.getBody(connection, delivery);
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return requestData.getParams().getBodyParams();
                }
            };
            request.setRetryPolicy(retryPolicy);
            mRequestQueue.add(request);
            String result = requestFuture.get();
            return converter.convert(tClass, result);
        } catch (Exception e) {
            e.printStackTrace();
            GHttpLog.getInstance().LogHttpError(e);
            return null;
        }
    }

    public <T> T sendSync(GRequestData requestData, Class<T> tClass) {
        return sendSync(requestData, tClass, new GJsonConverter<T>(null));
    }

    private RequestQueue mRequestQueue;

    public GHttp(Context context) {
        mRequestQueue = Volley.newRequestQueue(context, ALLOW_ALL_SC);
    }
}
