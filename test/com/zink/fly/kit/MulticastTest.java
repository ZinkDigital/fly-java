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


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class MulticastTest {
    
    
    
    public MulticastTest() {
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
     * Test of findNearest method, of class FlyFinder.
     */
    @Test
    public void testBasicMulti() throws InterruptedException {
        
        MulticastRequestor mcr = new MulticastRequestor();
        TestHandler handler = new TestHandler();
        MulticastResponseListener mcl = new MulticastResponseListener(handler);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.submit(mcl);
        Thread.sleep(100);
        mcr.sendRequest();
        Thread.sleep(100);
        
        assertTrue( handler.getCalled() > 0);
        
        //assertNotNull("Could not find local Space", fly);
    }
 
    
    class TestHandler implements FlyRepHandler {
        
        int called = 0;

        public void flyRepReply(FlyServerRep rep) {
            System.out.println(rep);
            ++called;
        }
        
        int getCalled() { return called; } 
    }
    
    
    
    
    
}
