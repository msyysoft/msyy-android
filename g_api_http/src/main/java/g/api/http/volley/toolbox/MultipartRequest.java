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

import android.os.Build;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import g.api.http.GHttpUtils;
import g.api.http.volley.AuthFailureError;
import g.api.http.volley.Response;
import g.api.http.volley.ResponseDelivery;
import g.api.http.volley.http.protocol.HTTP;

/**
 * A canned request for uploading file and retrieving the response body at a given URL as a String.
 */
public class MultipartRequest extends StringRequest {
    public static final String FILE_PREFIX = "file#";
    protected final String end = "\r\n";
    protected final String twoHyphens = "--";
    private String boundary;
    private Response.Listener<String> mListener;
    private boolean useChunkMode = true;

    public MultipartRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.mListener = listener;
        generateBoundary();
    }

    public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.mListener = listener;
        generateBoundary();
    }

    protected void generateBoundary() {
        boundary = "----WebKitFormBoundary" + GHttpUtils.generateRandomString(16);
    }

    public String getBoundary() {
        return boundary;
    }

    protected String getMimeType(String docType) {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("", "application/octet-stream");
        h.put("bmp", "image/bmp");
        h.put("gif", "image/gif");
        h.put("ico", "image/x-icon");
        h.put("jpeg", "image/jpeg");
        h.put("jpg", "image/jpeg");
        h.put("png", "image/png");
        //......
        String mime = h.get(docType);
        if (mime == null) {
            mime = "application/octet-stream";
        }
        return mime;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;" + "boundary=" + boundary;
    }

    @Override
    public DataOutputStream getBody(HttpURLConnection connection, ResponseDelivery delivery) throws IOException, AuthFailureError {
        Map<String, String> bodyParams = getParams();
        if (bodyParams != null && bodyParams.size() > 0) {

            connection.setDoOutput(true);
            long mFilesLength = 0;
            if (useChunkMode) {
                for (String bodyKey : bodyParams.keySet()) {
                    boolean isFile = bodyKey.startsWith(FILE_PREFIX);
                    String value = bodyParams.get(bodyKey);
                    if (isFile) {
                        long fileLength = new File(value).length();
                        mFilesLength += fileLength;
                    }
                }
                connection.setChunkedStreamingMode(4096);//4k
            } else {
                long mContentLength = 0;
                for (String bodyKey : bodyParams.keySet()) {
                    boolean isFile = bodyKey.startsWith(FILE_PREFIX);
                    String key = new String(isFile ? GHttpUtils.getFirstSubString(bodyKey, FILE_PREFIX) : bodyKey);
                    String value = bodyParams.get(bodyKey);
                    mContentLength += getFormItemKeyBytes(isFile, key, value).length;
                    if (isFile) {
                        long fileLength = new File(value).length();
                        mContentLength += fileLength;
                        mFilesLength += fileLength;
                    } else
                        mContentLength += getFormItemValueBytes(value).length;
                    mContentLength += getFormItemEndBytes().length;
                }
                mContentLength += getFormEndBytes().length;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    connection.setFixedLengthStreamingMode(mContentLength);
                } else {
                    connection.setFixedLengthStreamingMode((int) mContentLength);
                }
            }

            connection.addRequestProperty(HTTP.CONTENT_TYPE, getBodyContentType());
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            long mUploadFilesLength = 0;
            for (String bodyKey : bodyParams.keySet()) {
                boolean isFile = bodyKey.startsWith(FILE_PREFIX);
                String key = new String(isFile ? GHttpUtils.getFirstSubString(bodyKey, FILE_PREFIX) : bodyKey);
                String value = bodyParams.get(bodyKey);
                dos.write(getFormItemKeyBytes(isFile, key, value));
                if (isFile) {
                    FileInputStream fis = new FileInputStream(value);
                    byte[] buffer = new byte[4096];//4k
                    int len = -1;
                    while ((len = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, len);
                        mUploadFilesLength += len;
                        if (delivery != null) {
                            delivery.postProgress(this, mFilesLength, mUploadFilesLength, true);
                        }
                    }
                    fis.close();
                } else
                    dos.write(getFormItemValueBytes(value));
                dos.write(getFormItemEndBytes());
            }
            dos.write(getFormEndBytes());
            dos.flush();
            return dos;
        } else {
            return null;
        }
    }

    private byte[] getFormItemKeyBytes(boolean isFile, String key, String value) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        sb.append(twoHyphens + boundary + end);
        sb.append("Content-Disposition: form-data; name=\"" + key + "\"");
        if (isFile) {
            sb.append("; filename=\"" + GHttpUtils.getSimpleFileName(value) + "\"" + end);
            sb.append("Content-Type: " + getMimeType(GHttpUtils.getFileDocType(value)));
        }
        sb.append(end);
        sb.append(end);
        return sb.toString().getBytes(getParamsEncoding());
    }

    private byte[] getFormItemValueBytes(String value) throws UnsupportedEncodingException {
        return value.getBytes(getParamsEncoding());
    }

    private byte[] getFormItemEndBytes() throws UnsupportedEncodingException {
        return end.getBytes(getParamsEncoding());
    }

    private byte[] getFormEndBytes() throws UnsupportedEncodingException {
        return (twoHyphens + boundary + twoHyphens + end).getBytes(getParamsEncoding());
    }

    public void setUseChunkMode(boolean useChunkMode) {
        this.useChunkMode = useChunkMode;
    }

    @Override
    protected void deliverProgress(long total, long current, boolean isUploading) {
        if (mListener != null)
            mListener.onProgress(total, current, isUploading);
    }
}
