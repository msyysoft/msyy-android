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

package g.api.http.volley;

import java.util.Collections;
import java.util.Map;

import g.api.http.volley.http.Header;
import g.api.http.volley.http.HttpStatus;

/**
 * Data and headers returned from {@link Network#performRequest(Request, ResponseDelivery)}.
 */
public class NetworkResponse {
    /**
     * Creates a new network response.
     *
     * @param statusCode  the HTTP status code
     * @param data        Response body
     * @param headers     Headers returned with this response, or null for none
     * @param notModified True if the server returned a 304 and the data was already in cache
     */
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.apacheHeaders = null;
    }

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified, Header[] apacheHeaders) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
        this.apacheHeaders = apacheHeaders;
    }

    public NetworkResponse(byte[] data) {
        this(HttpStatus.SC_OK, data, Collections.<String, String>emptyMap(), false);
    }

    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this(HttpStatus.SC_OK, data, headers, false);
    }

    /**
     * The HTTP status code.
     */
    public final int statusCode;

    /**
     * Raw data from this response.
     */
    public final byte[] data;

    /**
     * Response headers.
     */
    public final Map<String, String> headers;

    /**
     * True if the server returned a 304 (Not Modified).
     */
    public final boolean notModified;

    /**
     * added by georgiecasey
     * /*  https://github.com/georgiecasey
     * /*  fix for getting duplicate header responses like multiple Set-Cookie
     * /*  http://stackoverflow.com/questions/18998361/android-volley-duplicate-set-cookie-is-overriden
     */
    public final Header[] apacheHeaders;

}