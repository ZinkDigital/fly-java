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


/**
 * @author nigel
 */
public class MulticastResponder {
    
   private final  byte [] bytesAddress = 
            new byte [] { (byte)232, (byte)43, (byte)96, (byte)232 };
   
   private final InetAddress multicastAddr; 
   private final int port = 4397;
   private final int ttl = 1;
  
    
   public MulticastResponder() {
        try {
            multicastAddr = InetAddress.getByAddress(bytesAddress); 
        } catch (UnknownHostException ex) {
            throw new FlyAccessException(ex);
        }

    }

    public void sendResponse(String tags) {
        try {
            MulticastSocket sock = new MulticastSocket();
            sock.setTimeToLive(ttl);
            byte[] sendBytes = tags.getBytes();
            DatagramPacket packet = new DatagramPacket(sendBytes, sendBytes.length, multicastAddr, port);
            sock.send(packet);
            sock.close();
        } catch (IOException ex) {
            throw new FlyAccessException(ex);
        }
    }
  
    
}
