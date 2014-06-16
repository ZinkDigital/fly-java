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


package com.zink.fly.kit;

import com.zink.fly.stub.MethodCodec;
import com.zink.fly.stub.StringCodec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


/**
 *
 * @author nigel
 */
public class FlyPinger {
   
    
    private static final long DEFAULT_TIMEOUT = 50; // ms
    private static final int FLY_PORT = 4396;
    
    
    private StringCodec stringCodec = new StringCodec();
    private final long socketTimeout;
    
    
    // set the connection timeout to the default
    public FlyPinger() {
        this(DEFAULT_TIMEOUT);
    }   
    
    /**
     * Construct a FlyPinger with a bespoke timeout
     * @param pingTimeOut in ms
     */
    public FlyPinger(long pingTimeOut) {
        socketTimeout = pingTimeOut;
    }
    
    
    
    /**
     * Send a ping message to a Fly Space running on the given Address
     * prefer the method that takes an InetAddress. Name to address 
     * conversion can be very slow on some platforms
     * @param host the host to ping
     * @return String[] of tags or null array for failed ping.
     * @throws java.net.UnknownHostException if the host cannont be found
     */   
    public String[] ping(String host) throws UnknownHostException {
        InetAddress addr = InetAddress.getByName(host);
        return ping(addr);
    }
    
    
   
    /**
     * Send a ping message to a Fly Space running on the given InetAddress and 
     * port.
     * @param addr the address to ping
     * @return String[] of tags or null array for failed ping.
     */   
    public String[] ping(InetAddress addr) {
        String [] tags = null;
        Socket sock = null;
        try {
                
            sock = new Socket();
            sock.setKeepAlive(true);
            sock.bind(null);
            sock.connect(new InetSocketAddress(addr, FLY_PORT),(int)socketTimeout);

            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            dos.writeInt(MethodCodec.FLY_HEADER);
            
            // wait for the reply for the specified time.
            // we use a timer in case the socket timeout fails on the
            // given client platform.
            long start = System.currentTimeMillis();
            while (dis.available() == 0  && (System.currentTimeMillis() - start) < socketTimeout   )  {
                Thread.sleep(1);
            }
        
            if (dis.available() == 0 ) {
                throw new TimeoutException("No reply from fly server");
            }
            
            long tagCount = dis.readLong();
            tags = new String[(int)tagCount];
            for (int i = 0; i < tagCount; i++) {
                tags[i] = stringCodec.readString(dis);
            }
            sock.setKeepAlive(Boolean.FALSE);
            sock.shutdownInput();
            sock.shutdownOutput();
            sock.close();
        } catch (Exception exp ) {
            // System.err.println(new Date() + " : FlyPinger : " + exp.getMessage() + " : " +addr.toString());
            // ignored
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch(Exception ex) {
                    // ignored
                }
            }
        }
        return tags;
    }
    
    
    
    public long getTimeout() {
        return socketTimeout;
    }
  
    
}
