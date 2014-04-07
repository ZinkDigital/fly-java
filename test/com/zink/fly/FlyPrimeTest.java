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
public class FlyPrimeTest {

    public FlyPrimeTest() {
    }


    final String HOST = "localhost";

    FlyPrime fly;
    
    @Before
    public void setUp() {   
    }

    @After
    public void tearDown() {
    }
    
    
   
    

     
    @Test
    public void writeTake() throws Exception {
        // open the connection
        fly = (FlyPrime) new FlyFinder().find();
       
        TestEntry entry  = createTestEntry(200);
        
        // write the entry
        fly.write(entry, 1000L);
        
        // set up a tempate to match the above entry and
        TestEntry template = createTestTemplate();
    
        // now take the entry
        TestEntry result = (TestEntry)fly.take(template, 0L);
        
        // check the return is good
        assertNotNull(result); 
        assertTrue(result != entry);
        assertTrue(result.equals(entry));
        
        // now try and takem it again to prove that
        // there is nothing there
        result = (TestEntry)fly.take(template, 0L);
        assertTrue( result == null);   
    }


    @Test
    public void writeRead() throws Exception {
        // open the connection
        fly = (FlyPrime) new FlyFinder().find();
      
        TestEntry entry = createTestEntry(200);
      
        // write the entry
        fly.write(entry, 1000L);
        
        TestEntry template = createTestTemplate();
    
        // now read the entry
        TestEntry result = (TestEntry)fly.read(template, 0L);
        assertNotNull(result);
        assertTrue(result != entry);
        assertTrue(result.equals(entry));

        // to test the read left a copy do a take
        // make sure it is the same again 
        result = (TestEntry)fly.take(template, 0L);
        assertNotNull(result);
        assertTrue(result != entry);
        assertTrue(result.equals(entry));
   
    }


    @Test
    public void snapShot() throws Exception {
        // open the connection
        fly = (FlyPrime) new FlyFinder().find();
       
        // write 
        TestEntry entry = createTestEntry(200);
        fly.write(entry, 1000L);

        TestEntry template = createTestTemplate();
        
        // snap the template and do a take to test it 
        TestEntry snapshot = (TestEntry)fly.snapshot(template);
        TestEntry result = (TestEntry)fly.take(snapshot, 0L);
        assertNotNull(result);
        assertTrue(result != entry);
        assertTrue(result.equals(entry));
    
    }
  
    @Test
    public void largeObject() throws Exception {
        // open the connection
        fly = (FlyPrime) new FlyFinder().find();
      
        TestEntry entry = createTestEntry(512);
        TestEntry template = createTestTemplate();
        
        for (int i = 0; i < 6; i++ ) {
            entry.payload = entry.payload + entry.payload;
            System.out.println("Payload size is " + entry.payload.length());
            fly.write(entry, 1000L);
            
            TestEntry result = (TestEntry)fly.take(template, 0L);
            assertNotNull(result);
            assertTrue(result != entry);
            assertTrue(result.equals(entry));
        }
    }
    
 
    @Test
    public void waitingTake() throws Exception {
        
        // open the connection
        fly = (FlyPrime) new FlyFinder().find();

        TestEntry entry = createTestEntry(512);
        TestEntry template = createTestTemplate();

        final long TAKE_TIME = 576;

        TestEntry result = (TestEntry) fly.take(template, TAKE_TIME);
        assertNull(result);

    }   
    
    private TestEntry createTestEntry(int size) {
        // set up an entry to write to the space
        TestEntry entry = new TestEntry();
        entry.name = "Fab 1";
        entry.reference = new Integer(17);
        entry.setPayloadOfSize(size);
        return entry;   
    }
    
    
    private TestEntry createTestTemplate() {
        TestEntry template = new TestEntry();
        template.name = "Fab 1";
        template.reference = null;    // match any value in this template
        template.payload = null;      // ditto
        return template;
     }
     
     
     
}

