/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/main/java/org/apache/http/message/AbstractHttpMessage.java $
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

import g.api.http.volley.http.Header;
import g.api.http.volley.http.HttpMessage;

/**
 * Basic implementation of {@link HttpMessage}.
 * 
 * 
 * @version $Revision: 744527 $
 * 
 * @since 4.0
 */
abstract class AbstractHttpMessage implements HttpMessage {

	private HeaderGroup headergroup;

	protected AbstractHttpMessage() {
		this.headergroup = new HeaderGroup();
	}

	// non-javadoc, see interface HttpMessage
	public Header[] getAllHeaders() {
		return this.headergroup.getAllHeaders();
	}

	// non-javadoc, see interface HttpMessage
	public void addHeader(final Header header) {
		this.headergroup.addHeader(header);
	}

	// non-javadoc, see interface HttpMessage
	public void setHeader(final Header header) {
		this.headergroup.updateHeader(header);
	}

	// non-javadoc, see interface HttpMessage
	public void setHeaders(final Header[] headers) {
		this.headergroup.setHeaders(headers);
	}
}
