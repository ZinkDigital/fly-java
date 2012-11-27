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


import com.zink.fly.FlyAccessException;
import com.zink.fly.Notifiable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



// Also see notes on snapshot, which could be done by subsituting the direct output
// stream for a byte array output stream.
//

public class Remoter {
      

    private OutputStream os = null;
    private InputStream is = null;
    private DataInputStream dis = null;
       
  
    // Blocking queue to support the replies
    private BlockingQueue<Long> replyQueue = new LinkedBlockingQueue<Long>(1);
  
    // mark a message is still in the dis so dont read it yet.
    private boolean messagePending = false;
    
    
    // contains the notify call backs and deals with the replies
    private NotifyMessageDispatcher notifyDispatcher;

    
    public Remoter(String hostName, int port) throws ConnectException {
        try {
            initStreams(new Socket(hostName, port));
            new MessageListener().start();
        } catch (Exception exp) {
            throw new ConnectException("Failed connection to Fly on "+ hostName + ":" + port);
        }
    }
    
  
    public Remoter(InetAddress hostAddr, int port) throws ConnectException {
        try {
            initStreams(new Socket(hostAddr, port));
            new MessageListener().start();
        } catch (Exception exp) {
            throw new ConnectException("Failed connection to Fly on "+ hostAddr.getHostName() + ":" + port);
        }
    }
    
    
    
    private void initStreams(Socket clientSocket) throws IOException {
        //clientSocket.setTcpNoDelay(true);
        os = clientSocket.getOutputStream();
        is = clientSocket.getInputStream();
        dis = new DataInputStream(is);
    }


    public synchronized void setNotifyMessageDispatcher(NotifyMessageDispatcher nmd) {
        // call this only once
        if (notifyDispatcher == null) {
            notifyDispatcher = nmd;
        }
    }


    
    public DataInputStream getDataInputStream() {
        return dis;   
    }
    
    
    // replies wih the first int on the stream.
    public long sendOperation(byte [] method) {
        long firstLong = 0;
        try {
            // send the request
            os.write(method);
            // take on the reply buffer
            firstLong = replyQueue.take();

        } catch (Exception ex) {
            throw new FlyAccessException(ex);
        }
        return firstLong;
    }
   

    
    public synchronized void setMessagePending() {
        messagePending = true;
    }
    
    public synchronized void setMessageComplete() {
        messagePending = false;
    }
    

    // make this leased at some point
    void addNotifyDetails(long notifyToken, Notifiable handler, Class clss) {
       notifyDispatcher.registerHandler(notifyToken, handler, clss);
    }

 
     
    class MessageListener extends Thread {     
        
       public MessageListener() {
           setName("MessageListener");
           setDaemon(true);
       }
       
       
 
       @Override
       public void run() {
            
            messagePending = false;
            while (!this.isInterrupted()) {
                
                try {
                    // the codec is still reading the message so wait until the
                    // dis is cleared before getting for the next message
                    while (messagePending) {
                        Thread.yield(); 
                    }
                    
                    long firstLong = dis.readLong();

                    // now check the header for a notify
                    if (firstLong == NotifyMessageDispatcher.NOTIFY_SIMPLE ||
                        firstLong == NotifyMessageDispatcher.NOTIFY_WITH_OBJECT ) {
                        notifyDispatcher.decodeAndQueue( firstLong,  dis );
                    } else {
                        setMessagePending();
                        replyQueue.put( firstLong );  
                    }       

                 } catch (Exception ex) {
                        throw new FlyAccessException(ex);
                 }        
            }
        }   
    }
    
      
   
}


