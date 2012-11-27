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
import com.zink.fly.MultiFly;
import com.zink.fly.kit.FlyFinder;
import java.util.ArrayList;
import java.util.Collection;



/**
 * A very simple demo of how to use the MultiFly interface for 
 * writing, reading and taking Fly entries
 * 
 * @author nigel
 */
public class MultiOps {
    
    
    public static void main(String [] args) {
   
        // use the FlyFinder if you are not running the Fly server 
        // locally
        Fly space = new FlyFinder().find();
 
        
        // Set up a payload and the number of objects to write and
        // read - tune these for your testing/deployment purposes
        final int payloadSize = 20;
        final int entryCount = 1000;
        
        
        // set up an list of entries that we can use to write to the space
        Collection<FlyEntry> entries = new ArrayList<FlyEntry>(entryCount);
        for (int i = 0; i < entryCount; i++) {
            FlyEntry object = new FlyEntry();
            object.name = "MultiFly";
            object.reference = new Integer(i);
            object.setPayloadOfSize(payloadSize);
            entries.add(object);          
        }
        
        
        // set up a tempate to match the above objects and
        // take it from the space.
        FlyEntry template = new FlyEntry();
        template.name = "MultiFly";
        template.reference = null;    // match any value in this template
        template.payload = null;      // ditto
        
        
        // do a single write read and and take and show the result
        System.out.println("Doing " + entryCount + " writes reads and takes");
        System.out.println("with single methods ...");
        
        long start = System.currentTimeMillis();
        writeReadAndTakeSingle(space, template, entries);
        long end = System.currentTimeMillis();
        
        float timeInSeconds = (float)(end-start)/1000.0f;
        System.out.println("Which took " + timeInSeconds + " seconds\n");
        
        
        // now do the same thing using multi 
        System.out.println("Doing " + entryCount + " writes reads and takes");
        System.out.println("with multi methods ...");
        
        start = System.currentTimeMillis();
        writeReadAndTakeMulti(space, template, entries);
        end = System.currentTimeMillis();
        
        timeInSeconds = (float)(end-start)/1000.0f;
        System.out.println("Which took " + timeInSeconds + " seconds");       
         
    }
    
    
    private static void writeReadAndTakeSingle(MultiFly space, FlyEntry template, Collection<FlyEntry> entries) {
        
        final int LEASE_TIME = 100*1000;
        
        // write the objects 
        for (FlyEntry entry : entries) {
            space.write(entry, LEASE_TIME);
        } 
        // read the objects
        for (FlyEntry entry : entries) {
            space.read(template,0);
        }
        // take th obejcts
        for (FlyEntry entry : entries) {
            space.take(template, 0);
        }
    }
        
   
   private static void writeReadAndTakeMulti(MultiFly space, FlyEntry template, Collection<FlyEntry> entries) {
        
        final int LEASE_TIME = 100*1000;
        
        // write the objects 
        space.writeMany( entries, LEASE_TIME);
        
        // read the objects
        Collection reads = space.readMany(template, entries.size());
       
        // take th obejcts
        Collection takes = space.takeMany(template, entries.size());
        }
 }
    
 
    

