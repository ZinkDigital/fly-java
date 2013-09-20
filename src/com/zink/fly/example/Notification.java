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

import com.zink.fly.Fly;
import com.zink.fly.NotiFly;
import com.zink.fly.kit.FlyFinder;

/**
 * @author nigel
 */
    
class Notification {
 
    public static void main(String [] args) throws InterruptedException {
        
        // find if possible an instance pf fly running on the local 
        // netowrk.
        FlyFinder flyFinder = new FlyFinder();
        Fly fly = flyFinder.find();
  
        
        // set the notify template and associated handler into the fly
        // server with a lifetime of 1 second 
        System.out.println("Setting up notify handler");
        setUpWriteNotify(fly);
        
         // wont match so nothing printed from the callback handler
        writeNonMatchingEntry(fly);
        
        // write a matching entry so handler 'fires'
        writeMatchingEntry(fly);
        
        // let the callbacks threads write to the sys out
        Thread.yield();
        
        // wait just over a second for the lease to expire 
        System.out.println("Waiting for Notify handler's lease to expire");
        Thread.sleep( 1100L );
        
        // The lease has expired so this wont fire the callback
        writeMatchingEntry( fly ) ; 
    
        System.out.println("End.");
         
    }

    
    
    private static void setUpWriteNotify(NotiFly fly) {

        // The most simple of handlers 
        CallbackHandler handler = new CallbackHandler();
        
        FlyEntry template = new FlyEntry();

        template.name = "Example NotiFly Entry"; // match this string
        template.reference = null;              // match anything
        template.payload = null;                // match anything
        
        fly.notifyWrite(template, handler, 1 * 1000L);
    }

    
    private static void writeNonMatchingEntry(NotiFly fly) {
        FlyEntry entry = new FlyEntry();
        entry.name = "Not a matching entry";
        entry.reference = new Integer(7);
        entry.payload = new String("Seven");
        fly.write(entry, 1 * 1000);    
    }
    
    
    private static void writeMatchingEntry(NotiFly fly) {
        FlyEntry entry = new FlyEntry();
        entry.name = "Example NotiFly Entry";
        entry.reference = new Integer(11);
        entry.payload = new String("Eleven");
        fly.write(entry, 1 * 1000); 
    }
    

}