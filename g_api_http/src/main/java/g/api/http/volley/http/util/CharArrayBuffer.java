/*
 * $HeadURL: https://svn.apache.org/repos/asf/httpcomponents/httpcore/tags/4.0.1/httpcore/src/main/java/org/apache/http/util/CharArrayBuffer.java $
 * $Revision: 744533 $
 * $Date: 2009-02-14 18:15:18 +0100 (Sat, 14 Feb 2009) $
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

package g.api.http.volley.http.util;

/**
 * A resizable char array.
 * 
 * 
 * @version $Revision: 744533 $
 * 
 * @since 4.0
 */
public final class CharArrayBuffer {

	private char[] buffer;
	private int len;

	/**
	 * Creates an instance of {@link CharArrayBuffer} with the given initial
	 * capacity.
	 * 
	 * @param capacity
	 *            the capacity
	 */
	public CharArrayBuffer(int capacity) {
		super();
		if (capacity < 0) {
			throw new IllegalArgumentException(
					"Buffer capacity may not be negative");
		}
		this.buffer = new char[capacity];
	}

	private void expand(int newlen) {
		char newbuffer[] = new char[Math.max(this.buffer.length << 1, newlen)];
		System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
		this.buffer = newbuffer;
	}

	/**
	 * Appends chars of the given string to this buffer. The capacity of the
	 * buffer is increased, if necessary, to accommodate all chars.
	 * 
	 * @param str
	 *            the string.
	 */
	public void append(String str) {
		if (str == null) {
			str = "null";
		}
		int strlen = str.length();
		int newlen = this.len + strlen;
		if (newlen > this.buffer.length) {
			expand(newlen);
		}
		str.getChars(0, strlen, this.buffer, this.len);
		this.len = newlen;
	}

	/**
	 * Appends <code>ch</code> char to this buffer. The capacity of the buffer
	 * is increased, if necessary, to accommodate the additional char.
	 * 
	 * @param ch
	 *            the char to be appended.
	 */
	public void append(char ch) {
		int newlen = this.len + 1;
		if (newlen > this.buffer.length) {
			expand(newlen);
		}
		this.buffer[this.len] = ch;
		this.len = newlen;
	}


	/**
	 * Sets the length of the buffer. The new length value is expected to be
	 * less than the current capacity and greater than or equal to
	 * <code>0</code>.
	 * 
	 * @param len
	 *            the new length
	 * @throws IndexOutOfBoundsException
	 *             if the <code>len</code> argument is greater than the current
	 *             capacity of the buffer or less than <code>0</code>.
	 */
	public void setLength(int len) {
		if (len < 0 || len > this.buffer.length) {
			throw new IndexOutOfBoundsException();
		}
		this.len = len;
	}

	/**
	 * Returns <code>true</code> if this buffer is empty, that is, its
	 * {@link #length()} is equal to <code>0</code>.
	 * 
	 * @return <code>true</code> if this buffer is empty, <code>false</code>
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return this.len == 0;
	}

	/**
	 * Returns <code>true</code> if this buffer is full, that is, its
	 * {@link #length()} is equal to its {@link #capacity()}.
	 * 
	 * @return <code>true</code> if this buffer is full, <code>false</code>
	 *         otherwise.
	 */
	public boolean isFull() {
		return this.len == this.buffer.length;
	}

	public String toString() {
		return new String(this.buffer, 0, this.len);
	}

}
