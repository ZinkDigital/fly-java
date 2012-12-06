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


import com.zink.fly.kit.FlyPinger;
import com.zink.fly.stub.MethodCodec;
import com.zink.fly.stub.Remoter;
import com.zink.fly.stub.StringCodec;
import java.io.DataInputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;



/**
 *
 * @author nigel
 */
public class FlyPingTest {

    public FlyPingTest() {
    }
    
    @Before
    public void setUp() {   
    }

    @After
    public void tearDown() {
    }
    
    
    private static final String HOST_ADDR = "localhost";
     
    @Test
    public void pingWithRemoter() throws Exception {
        // Use the remoter to send the ping reques 
        Remoter remoter = new Remoter(HOST_ADDR, 4396);
        StringCodec stringCodec = new StringCodec();
        
        ByteBuffer bb = ByteBuffer.allocate(4);
          
        // just the header is a valid ping message 
        bb.putInt(MethodCodec.FLY_HEADER);
        
        long reply = remoter.sendOperation(bb.array());
        
        DataInputStream dis = remoter.getDataInputStream();

        System.out.println("Number of tags [" + reply + "]");
        
        assertTrue( reply > 0);
        // get the tags from the reply
        for (int i = 0; i < reply; i++) {
            String tag = stringCodec.readString(dis);
            if (i == 0) {
                assertEquals("FlySpace2",tag );
            }
            System.out.println("Tag " + (i+1) + " [" + tag + "]");
        }  

    }


    @Test
    public void pingWithPinger() throws Exception {
           
        InetAddress addr = InetAddress.getByName(HOST_ADDR);   
            
        FlyPinger pgr = new FlyPinger();
        String [] tags = pgr.ping(addr);
        assertTrue( tags != null);    
        assertEquals("FlySpace2",tags[0] );    
    }
    
}


