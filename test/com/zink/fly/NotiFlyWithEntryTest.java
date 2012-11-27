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
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author nigel
 */
public class NotiFlyWithEntryTest {

    public NotiFlyWithEntryTest() {
    }

 

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    /**
     *
     */
    @Test
    public void testNotifyWriteMatch() throws InterruptedException {
        
        Fly fly = new FlyFinder().find();
        
        // set up a notify counting handler
        ExampleNotifyHandler handler = new ExampleNotifyHandler();
        
        
        // make the notify template anb
        TestEntry template = new TestEntry();
        template.name = "Test Entry";
        template.reference = new Integer(5);   
        template.payload = null;                 // match anything
     
        
        // ask the Space to set up the notify for us
        boolean goodReg = fly.notifyWrite(template, handler, 10*1000);
        
        // check that it was set up ok
        assertEquals(goodReg, true);
        
        
        // make a matching test entry 
        TestEntry entry = template;
        entry.name = "Test Entry";
        entry.reference = new Integer(5);
        entry.payload = new String("Dennis");
        
        // Make a load of writes one of which matches the the notify template
        fly.write(entry, 1*1000);
        
        // allow the reader thread to get the message
        Thread.sleep(100);
        
        // check that the handler got call once and once only
        // and that the correct entry was returned
        assertEquals(1, handler.getMatchCalled());
        TestEntry result = (TestEntry) handler.getEntry();
        assertEquals(result, entry);

    }
    
    
    @Test
    public void testNotifyNoMatch() throws InterruptedException {
        
        Fly fly = new FlyFinder().find();
        
        // set up a notify counting handler
        ExampleNotifyHandler handler = new ExampleNotifyHandler();
        
        
        // make the notify template anb
        TestEntry template = new TestEntry();
        template.name = "Test 2 Entry";
        template.reference = new Integer(5);   
        template.payload = null;                 // match anything
     
        
        // ask the Space to set up the notify for us
        boolean goodReg = fly.notifyWrite(template, handler, 10*1000);
        
        // check that it was set up ok
        assertEquals(goodReg, true);
        
        
        // make a matching test entry 
        TestEntry entry = template;
        entry.name = "Test 2 Entry";
        entry.reference = new Integer(7);
        entry.payload = new String("Dennis");
        
        // Make a load of writes one of which matches the the notify template
        fly.write(entry, 1*1000);
        
        // allow the reader thread to get the message
        Thread.sleep(1000);
        
        // chack that the handler did not get called
        assertEquals(0, handler.getMatchCalled());
    }

    
    
    @Test
    public void notifyLease() throws Exception {
    
        Fly fly = new FlyFinder().find();
        
        TestEntry template = new TestEntry();
        template.name = "Test 3 Entry";
        template.reference = new Integer(5);   
        template.payload = null;                 // match anything
     
        // set up a counting handler
        ExampleNotifyHandler handler = new ExampleNotifyHandler();
        
        // ask the Space to set up the notify for us on which the lease will
        // expire quickly
        boolean goodReg = fly.notifyWrite(template, handler, 1000);
        
        // check that it was set up ok
        assertEquals(goodReg, true);
        
        // make an entry that matches the template
        TestEntry entry = new TestEntry();
        entry.name = "Test 3 Entry";
        entry.reference = new Integer(5);
        entry.payload = new String("Dennis");
       
        
        // write the entry to make sure that it is mathced 
        fly.write(entry, 1000);
        
        Thread.sleep(100);
        
        // chack that the handler got call once and once only
        // and that the correct entry was returned
        assertEquals(1, handler.getMatchCalled());
        TestEntry result = (TestEntry) handler.getEntry();
        assertEquals(result, entry);

        
        // now wait for the lease to expire 
        Thread.sleep(1200);
        
        // write the entry to make sure that it is mathced 
        fly.write(entry, 1000);
        
        // chack that the handler is still only one
        assertEquals(1, handler.getMatchCalled());              
    }




    @Test
    public void testNotifyTakeMatch() throws InterruptedException {

        Fly fly =  new FlyFinder().find();

        // set up a notify counting handler
        ExampleNotifyHandler handler = new ExampleNotifyHandler();


        // make the notify template anb
        TestEntry template = new TestEntry();
        template.name = "Test Entry";
        template.reference = new Integer(5);
        template.payload = null;                 // match anything


        // ask the Space to set up the Take notify for us
        boolean goodReg = fly.notifyTake(template, handler, 10*1000);

        // check that it was set up ok
        assertEquals(goodReg, true);


        // make a matching test entry
        TestEntry entry = template;
        entry.name = "Test Entry";
        entry.reference = new Integer(5);
        entry.payload = new String("Dennis");

       
        fly.write(entry, 1*1000);
        // check not in write queue
        // allow the reader thread to get the message
        Thread.sleep(100);
        assertEquals(0, handler.getMatchCalled());

        // check in take queue and that the hanlder is called.
        fly.take(entry, 0L);

        // allow the reader thread to get the message
        Thread.sleep(100);

                // chack that the handler got call once and once only
        // and that the correct entry was returned
        assertEquals(1, handler.getMatchCalled());
        TestEntry result = (TestEntry) handler.getEntry();
        assertEquals(result, entry);

    }

    
    @Test
    public void testMany() throws Exception {
          
          for (int i = 0; i < 10; i++) {
                testNotifyWriteMatch();
          }
      }

    

    class ExampleNotifyHandler implements NotifyHandlerReturningEntry {

        private int matchCalled = 0;
        private Object entry;
        
        public void templateMatched() {
            setMatchCalled(getMatchCalled() + 1);
        }

        public int getMatchCalled() {
            return matchCalled;
        }

        public void setMatchCalled(int matchCalled) {
            this.matchCalled = matchCalled;
        }

        public void templateMatched(Object entry) {
            this.entry = entry;
            setMatchCalled(getMatchCalled() + 1);
        }

 
        public Object getEntry() {
            return entry;
        }
                    
    }
    
    
    
}