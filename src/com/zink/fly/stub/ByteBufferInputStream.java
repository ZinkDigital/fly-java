/*
 * Copyright (c) 2006-2012 Zink Digital Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zink.fly.stub;


import java.io.IOException;
import java.nio.ByteBuffer;


public class ByteBufferInputStream extends java.io.InputStream {

    /**
     * Use the underlying byte buffer to maintain all state
     */
    private ByteBuffer bb;


  
    public ByteBufferInputStream(ByteBuffer byteBuffer) {
    	switchBuffer(byteBuffer);
    }

 
    public synchronized void switchBuffer(ByteBuffer byteBuffer) {
    	this.bb = byteBuffer;
    }


    public synchronized int read() {
    	int val = (int)bb.get()  & 0xFF;
    	return val;
    }


    public synchronized int read(byte b[], int off, int len) {
	bb.get(b, off, len);
	return len;
    }

    
    public synchronized long skip(long n) {
    	bb.position((int) (bb.position() + n));
    	return n;
    }

    
    public synchronized int available() {
    	return bb.remaining();
    }

    public boolean markSupported() {
    	return true;
    }


    public void mark(int readAheadLimit) {
    	bb.mark();
    }


    public synchronized void reset() {
		bb.reset();
    }

    /**
     * Doesnt mean anything in this context.
     */
    public void close() throws IOException {
    }

}
