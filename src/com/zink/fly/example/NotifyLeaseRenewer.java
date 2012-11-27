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


package com.zink.fly.example;

import com.zink.fly.NotiFly;
import com.zink.fly.NotifyHandler;

/**
 * @author nigel
 */
public class NotifyLeaseRenewer {
    
    private NotiFly space;
    private Object template;
    private NotifyHandler handler;
    private long leaseTime;
    
    private Thread renewer; 
    private boolean running = true;  
            
    public NotifyLeaseRenewer(NotiFly space, Object template, NotifyHandler handler, long leaseTime)
    {
        this.space = space;
        this.template = template;
        this.handler = handler;
        this.leaseTime = leaseTime;
        renewer = new RenewThread();
        renewer.start();
    }
    
    
    public void cancel() {
        if (renewer.isAlive()) {
            renewer.interrupt();
        }
        running = false;
    }
    
    
    
    class RenewThread extends Thread {
    
       RenewThread() {
           this.setName("Notify Lease Renewer");
           this.setDaemon(true);
       }
        
            
       @Override
       public void run() {
           while (running) {
                try {
                    space.notifyWrite(template, handler, leaseTime);
                    Thread.sleep(leaseTime);
                } catch (InterruptedException ex) {
                    running = false;
                }
           }  
           
       }        
    }
    
}
