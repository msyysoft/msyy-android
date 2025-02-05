/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/main/java/org/apache/http/message/HeaderGroup.java $
 * $Revision: 744527 $
 * $Date: 2009-02-14 18:06:25 +0100 (Sat, 14 Feb 2009) $
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package g.api.http.volley.http.message;

import java.util.ArrayList;
import java.util.List;

import g.api.http.volley.http.Header;

/**
 * A class for combining a set of headers.
 * This class allows for multiple headers with the same name and
 * keeps track of the order in which headers were added.
 * 
 *
 * @since 4.0
 */
class HeaderGroup implements Cloneable {

    /** The list of headers for this group, in the order in which they were added */
    private List<Header> headers;

    /**
     * Constructor for HeaderGroup.
     */
    public HeaderGroup() {
        this.headers = new ArrayList<Header>(16);
    }
    
    /**
     * Removes any contained headers.
     */
    private void clear() {
        headers.clear();
    }
    
    /**
     * Adds the given header to the group.  The order in which this header was
     * added is preserved.
     * 
     * @param header the header to add
     */
    void addHeader(Header header) {
        if (header == null) {
            return;
        }
        headers.add(header);
    }
    

    /**
     * Replaces the first occurence of the header with the same name. If no header with 
     * the same name is found the given header is added to the end of the list.
     * 
     * @param header the new header that should replace the first header with the same 
     * name if present in the list.
     */
    void updateHeader(Header header) {
        if (header == null) {
            return;
        }
        for (int i = 0; i < this.headers.size(); i++) {
            Header current = (Header) this.headers.get(i);
            if (current.getName().equalsIgnoreCase(header.getName())) {
                this.headers.set(i, header);
                return;
            }
        }
        this.headers.add(header);
    }

    /**
     * Sets all of the headers contained within this group overriding any
     * existing headers. The headers are added in the order in which they appear
     * in the array.
     * 
     * @param headers the headers to set
     */
    public void setHeaders(Header[] headers) {
        clear();
        if (headers == null) {
            return;
        }
        for (int i = 0; i < headers.length; i++) {
            this.headers.add(headers[i]);
        }
    }
    
    /**
     * Gets all of the headers contained within this group.
     * 
     * @return an array of length >= 0
     */
    public Header[] getAllHeaders() {
        return (Header[]) headers.toArray(new Header[headers.size()]);
    }
    
    public Object clone() throws CloneNotSupportedException {
        HeaderGroup clone = (HeaderGroup) super.clone();
        clone.headers = new ArrayList<Header>(this.headers);
        return clone;
    }
    
}
