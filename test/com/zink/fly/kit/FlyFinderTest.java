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

import com.zink.fly.Fly;
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
public class FlyFinderTest {

    public FlyFinderTest() {
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
    public void testNoTags() {
        
        // timeout in 78 ms
        FlyFinder instance = new FlyFinder();
        
        // test for a fail
        Fly fly = instance.find();
        
        assertNotNull("Could not find local Space", fly);
    }

    
     /**
     * Test of findNearest method, of class FlyFinder.
     */
    @Test
    public void testTagsMatch() {
        
        FlyFinder instance = new FlyFinder();
        
        // test for a fail
        Fly fly = instance.find(new String [] { "FlySpace2" } );
        
        assertNotNull("Could not match tag on local Space", fly);
    }

    
    /**
     * Test of findNearest method, of class FlyFinder.
     */
    @Test
    public void testTagsNoMatch() {
        
        FlyFinder instance = new FlyFinder();
        
        // test for a fail
        Fly fly = instance.find(new String[] { "NadaFlySpace" } );
        
        assertNull("Matched bad tags", fly);
    }
    

}