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
package com.zink.fly;



import com.zink.fly.kit.FlyFinder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;



/**
 *
 * @author nigel
 */
public class FlyEntryTest {

    public FlyEntryTest() {
    }

   
    
    @Before
    public void setUp() {   
    }

    @After
    public void tearDown() {
    }
    
    

    
    
    @Test
    public void emptyEntry() throws Exception {
       // open the connection
        FlyPrime fly = new FlyFinder().find();
       
       // set up the empty entry
       final long leaseTime = 60 * 1000;
       EmptyEntry entry = new EmptyEntry();
        
       
       long lease = fly.write(entry, leaseTime);
       assertTrue(lease == leaseTime);
       
       // then read it back 
       entry = fly.take(entry, 0);
       
       assertNotNull(entry);
       assertTrue(entry instanceof EmptyEntry);
    }
    

     
    @Test
    public void exoticEntry() throws Exception {
        
       final String FIRST_NAME = "Dan";
       final String LAST_NAME = "Tucker";
        
        
       // open the connection
       FlyPrime fly = new FlyFinder().find();
        // open the connection
       // set up the empty entry
       final long leaseTime = 500;
       ExoticEntry entry = new ExoticEntry();
       entry.name1 = FIRST_NAME;
       entry.setName2(LAST_NAME);
       
       final String name5 = entry.name5;
       final String name6 = entry.getName6();
       
       fly.write(entry, leaseTime);
       
       // then read it back 
       ExoticEntry reply = fly.read(entry, 0);
       
       assertNotNull(reply);
       assertTrue(reply instanceof ExoticEntry);
       assertTrue(reply.name1.equals(FIRST_NAME));
       assertTrue(reply.getName2().equals(LAST_NAME));
       assertTrue(reply.name5.equals(name5));
       assertTrue(reply.getName6().equals(name6));
       
   
    }


    // test an inherited entry
    @Test
    public void superEntry() throws Exception {
       // open the connection
       FlyPrime fly = new FlyFinder().find();

       Integer index = new Integer(10);
       Integer reference = new Integer(20);
       String name = "Dennis";
       String narrative = "The Menace";
       String payload = "ZZZZZZZZZZZZZZZZZZZZZZZ";

       // set up the inheriting entry
       final long leaseTime = 60 * 1000;
       SuperEntry entry = new SuperEntry();

       entry.index = index;
       entry.reference = reference;
       entry.name = name;
       entry.narrative = narrative;
       entry.payload = payload;

       long lease = fly.write(entry, leaseTime);
       assertTrue(lease == leaseTime);

       // then read it back
       SuperEntry reply = fly.take(entry, 0);

       assertNotNull(reply);
       assertTrue(entry instanceof SuperEntry);
       assertEquals(entry.index, index);
       assertEquals(entry.reference , reference);
       assertEquals(entry.name , name);
       assertEquals(entry.narrative , narrative);
       assertEquals(entry.payload , payload);
    }


}
