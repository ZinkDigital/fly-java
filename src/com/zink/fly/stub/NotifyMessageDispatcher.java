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
import com.zink.fly.NotifyHandler;
import com.zink.fly.NotifyHandlerReturningEntry;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author nigel
 */
class NotifyMessageDispatcher {

    static public final long NOTIFY_SIMPLE = -1L;
    static public final long NOTIFY_WITH_OBJECT = -2L;


    // Notify support with block message queue and 'token to handler' map   
    private Map<Long, ClientNotifyContext> notifyContextMap = new HashMap<Long, ClientNotifyContext>();


    // Remove the blocking queue and set up the an Executor
    ExecutorService threadPool = Executors.newCachedThreadPool();

    // we need the TypeChain to decode entries coming back from the new
    // notify methods that return an entry
    private TypeChain typeChain;

    public NotifyMessageDispatcher(TypeChain typeChain) {
        this.typeChain = typeChain;
    }


    // this must be leased at some point
    void registerHandler(long notifyToken, Notifiable handler, Class clss ) {
        ClientNotifyContext context = new ClientNotifyContext(handler, clss);
        notifyContextMap.put(notifyToken, context);
    }

    // Decode the message and put it on dispatch queue
    void decodeAndQueue( long notifyMode, DataInputStream dis) throws IOException {

        long notifyToken = dis.readLong();
        ClientNotifyContext cnc = notifyContextMap.get(notifyToken);
        Notifiable handler = cnc.getHandler();


        PendingNotify pending = null;
        // double check that it all joins up
        if (handler instanceof NotifyHandler &&
            notifyMode == NOTIFY_SIMPLE ) {
            pending = new PendingNotify(handler);
        }
        if (handler instanceof NotifyHandlerReturningEntry &&
            notifyMode == NOTIFY_WITH_OBJECT) {

            // read the object with template
            long size = dis.readLong();
            Object  entry = typeChain.readObject(dis, size, cnc.getClss());
            pending = new PendingNotifyWithObject(handler, entry);
        }

        if (pending != null ) {
            threadPool.submit(pending);
        } else {
            throw new FlyAccessException("Notify mode does not match type of notify handler");
        }

    }



   // immutable notify context
   class ClientNotifyContext {
       private Notifiable handler;
       private Class clss;

        private ClientNotifyContext(Notifiable handler, Class clss) {
            this.handler = handler;
            this.clss = clss;
        }


        public Notifiable getHandler() {
            return handler;
        }

        public Class getClss() {
            return clss;
        }
   }


   class PendingNotify implements Runnable {
                
         Notifiable notifiable;

         public PendingNotify(Notifiable notifiable) {
             this.notifiable = notifiable;
         }

         public void execute() {
            
         }

        public void run() {
             ((NotifyHandler)notifiable).templateMatched();
        }
   }
   

   class PendingNotifyWithObject extends PendingNotify {

        private Object entry;

        public PendingNotifyWithObject(Notifiable notifiable, Object entry) {
            super(notifiable);
            this.entry = entry;
        }

        
        @Override
        public void run() {
             ((NotifyHandlerReturningEntry)notifiable).templateMatched(entry);
        }
   }


       

}