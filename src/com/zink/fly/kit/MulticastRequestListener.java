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

import com.zink.fly.FlyAccessException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

/**
 * The mulicast lisener listens for fly based requests from the fly client
 *
 *
 * @author nigel
 */
public class MulticastRequestListener implements Callable {

    private static final byte[] bytesAddress =
            new byte[]{(byte) 232, (byte) 43, (byte) 96, (byte) 232};
    private static final int MAX_BUFFER_SIZE = 512;
    private static final int port = 4396;
    
    private InetAddress multicastAddr = null;
    private RequestHandler requestHandler = null;
    private MulticastSocket multicastSocket = null;

    
    public MulticastRequestListener(RequestHandler handler) {
        try {
            this.requestHandler = handler;
            multicastAddr = InetAddress.getByAddress(bytesAddress);
        } catch (UnknownHostException ex) {
            new FlyAccessException(ex);
        }
    }

    
    public Object call() throws Exception {

        try {

            /* instantiate a MulticastSocket */
            multicastSocket = new MulticastSocket(port);
            multicastSocket.setReuseAddress(true);
            multicastSocket.joinGroup(multicastAddr);

            while (!Thread.currentThread().isInterrupted()) {

                /* listen for a new datagram packet */
                byte[] buf = new byte[MAX_BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(packet);
                String request = new String(packet.getData(), 0, packet.getLength());
                requestHandler.request(request);
            }

            multicastSocket.leaveGroup(multicastAddr);
            multicastSocket.close();

        } catch (IOException e) {
            throw new FlyAccessException(e);

        }
        return null;
    }
    
    
    /**
     * socket.recieve blocks even when the thread is signalled terminate
     * so we need to force close the socket from another thread. 
     */
    public void close() {
        try {
            multicastSocket.leaveGroup(multicastAddr);
        } catch (IOException ex) {
            // doesnt ever get here - apparently
            throw new FlyAccessException(ex);
        } finally {
            multicastSocket.close();
        }
    }

}
