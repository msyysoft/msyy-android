/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/main/java/org/apache/http/HttpMessage.java $
 * $Revision: 744522 $
 * $Date: 2009-02-14 17:56:03 +0100 (Sat, 14 Feb 2009) $
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

package g.api.http.volley.http;


/**
 * HTTP messages consist of requests from client to server and responses
 * from server to client.
 * <pre>
 *     HTTP-message   = Request | Response     ; HTTP/1.1 messages
 * </pre>
 * <p>
 * HTTP messages use the generic message format of RFC 822 for 
 * transferring entities (the payload of the message). Both types 
 * of message consist of a start-line, zero or more header fields 
 * (also known as "headers"), an empty line (i.e., a line with nothing 
 * preceding the CRLF) indicating the end of the header fields, 
 * and possibly a message-body.
 * </p>
 * <pre>
 *      generic-message = start-line
 *                        *(message-header CRLF)
 *                        CRLF
 *                        [ message-body ]
 *      start-line      = Request-Line | Status-Line
 * </pre>
 *
 *
 * @version $Revision: 744522 $
 * 
 * @since 4.0
 */
public interface HttpMessage {
    
    /**
     * Returns all the headers of this message. Headers are orderd in the sequence
     * they will be sent over a connection.
     * 
     * @return all the headers of this message
     */
    Header[] getAllHeaders();

    /**
     * Adds a header to this message. The header will be appended to the end of
     * the list.
     * 
     * @param header the header to append.
     */
    void addHeader(Header header);
        
}
