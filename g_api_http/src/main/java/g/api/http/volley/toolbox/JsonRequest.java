/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package g.api.http.volley.toolbox;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import g.api.http.volley.AuthFailureError;
import g.api.http.volley.NetworkResponse;
import g.api.http.volley.Request;
import g.api.http.volley.Response;
import g.api.http.volley.ResponseDelivery;
import g.api.http.volley.VolleyLog;
import g.api.http.volley.http.protocol.HTTP;


/**
 * A request for retrieving a T type response body at a given URL that also optionally sends along a
 * JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
public abstract class JsonRequest<T> extends Request<T> {
    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    /**
     * Lock to guard mListener as it is cleared on cancel() and read on delivery.
     */
    private final Object mLock = new Object();

    private Response.Listener<T> mListener;

    private final String mRequestBody;

    /**
     * Deprecated constructor for a JsonRequest which defaults to GET unless {@link #getPostBody()}
     * or {@link #getPostParams()} is overridden (which defaults to POST).
     *
     * @deprecated Use {@link #JsonRequest(int, String, String, Listener, ErrorListener)}.
     */
    @Deprecated
    public JsonRequest(
            String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, requestBody, listener, errorListener);
    }

    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param requestBody   The content to post as the body of the request. Null indicates no
     *                      parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public JsonRequest(
            int method,
            String url,
            String requestBody,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;
    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }

    @Override
    protected void deliverResponse(T response) {
        Response.Listener<T> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public DataOutputStream getBody(HttpURLConnection connection, ResponseDelivery delivery) throws IOException, AuthFailureError {
        try {
            connection.setDoOutput(true);
            connection.addRequestProperty(HTTP.CONTENT_TYPE, getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET));
            return out;
        } catch (Exception e) {
            VolleyLog.wtf(
                    "Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}