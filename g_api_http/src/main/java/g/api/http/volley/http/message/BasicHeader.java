/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/main/java/org/apache/http/message/BasicHeader.java $
 * $Revision: 728464 $
 * $Date: 2008-12-21 18:59:05 +0100 (Sun, 21 Dec 2008) $
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

import g.api.http.volley.http.Header;

/**
 * 
 * Basic implementation of {@link Header}.
 * 
 * @version $Revision: 728464 $
 * 
 * @since 4.0
 */
public class BasicHeader implements Header, Cloneable {

    private final String name;
    private final String value;
    
    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     */
    public BasicHeader(final String name, final String value) {
        super();
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
 
}
