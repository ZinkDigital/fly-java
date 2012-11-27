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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author nigel
 */
public class MultiFlyTest {
    
    public MultiFlyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    /**
     */
    @Test
    public void testWriteMany() throws InterruptedException {
        
        MultiFly fly =  new FlyFinder().find();
        
        final String TEST_CODE = "MultiFly1";
        
        // set up a collection to contian the enires to be written
        List<TestEntry> entries = new ArrayList<TestEntry>();
 
        final int numEntries = 100;
        for (int i = 0; i < numEntries; i++ ) {
            entries.add(new TestEntry(TEST_CODE, i, "payload"));
        }
        
        fly.writeMany(entries, 10*1000);
        
        // make the notify template anb
        TestEntry template = new TestEntry();
        template.name = TEST_CODE;
        template.reference = null;   
        template.payload = null;                 // match anything
        int countBack = 0;
        
        
        // count the entries back out with a standard take
        while (null != fly.take(template, 0L)) {
            countBack++;
        }
        
        // check that it was set up ok
        assertEquals(numEntries, countBack);     
    }
   
    
    @Test
    public void testReadMany() throws InterruptedException {
        
        MultiFly fly =  new FlyFinder().find();
        final String TEST_CODE = "MultiFly2";
        
        final int numEntries = 35;
        for (int i = 0; i < numEntries; i++ ) {
            fly.write(new TestEntry(TEST_CODE, i, "payload"),1000);
        }
        
            
        // make the read template and multi read
        TestEntry template = new TestEntry();
        template.name = TEST_CODE;
        template.reference = null;   
        template.payload = null;     
        
        
        final int numToRead = 10;
        Collection entries = fly.readMany(template, numToRead);
  
        // Check the number of entries 
        assertEquals(numToRead, entries.size());
        Iterator itr = entries.iterator();
        assertEquals( ((TestEntry)itr.next()).reference, new Integer(0));
 
    }
    
    
      
    @Test
    public void testTakeMany() throws InterruptedException {
        
        MultiFly fly =  new FlyFinder().find();
        final String TEST_CODE = "MultiFly3";
        
        final int numEntries = 25;
        for (int i = 0; i < numEntries; i++ ) {
            fly.write(new TestEntry(TEST_CODE, i, "payload"),1000);
        }
        
            
        // make the read template and multi read
        TestEntry template = new TestEntry();
        template.name = TEST_CODE;
        template.reference = null;   
        template.payload = null;     
        
        
        final int numToTake = 10;
        Collection entries = fly.takeMany(template, numToTake);
  
        // Check the number of entries 
        assertEquals(numToTake, entries.size());
        Iterator itr = entries.iterator();
        assertEquals( ((TestEntry)itr.next()).reference, new Integer(0));
   
        // now test run agian
        entries = fly.takeMany(template, numToTake);
        
        assertEquals(numToTake, entries.size());
        itr = entries.iterator();
        assertEquals( ((TestEntry)itr.next()).reference, new Integer(numToTake));
        
        // try tl read more than the list
        entries = fly.takeMany(template, numToTake);
        
        assertEquals(numEntries - (numToTake*2), entries.size());
        itr = entries.iterator();
        assertEquals( ((TestEntry)itr.next()).reference, new Integer(numToTake*2));                
    }
            
}


