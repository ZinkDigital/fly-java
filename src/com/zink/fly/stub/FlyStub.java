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

import com.zink.fly.FieldCodec;
import com.zink.fly.Fly;


import com.zink.fly.Notifiable;
import com.zink.fly.NotifyHandler;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Collection;




public class FlyStub implements Fly  {

    private static final int FLY_PORT = 4396;
   
    
    private MethodCodec codec;  
    private static long sampleTime = 100;


    
    public FlyStub(String hostname, FieldCodec fieldCodec) throws ConnectException
        {
        Remoter remoter = new Remoter(hostname,FLY_PORT);
        codec = new MethodCodec(remoter, fieldCodec);
        }

    
    public FlyStub(InetAddress addr, FieldCodec fieldCodec) throws ConnectException
        {
        Remoter remoter = new Remoter(addr,FLY_PORT);
        codec = new MethodCodec(remoter, fieldCodec);
        }
 
    
	
    public Object read(Object template, long timeout) {
        // first try a read if there is a repsonse it doesnt matter about the 
        // timeout
        long endTime = System.currentTimeMillis() + timeout;
        
        Object ret = codec.read(template, 0L);
        if (ret != null || timeout == 0) {
            return ret;        
        }
             
        
        long nyquist = (timeout + 1L) >> 1;
        if (nyquist > sampleTime) {
            nyquist = sampleTime;
        }
        
        while (ret == null && endTime > System.currentTimeMillis()) {
            try {
                Thread.sleep(nyquist);
            } catch (InterruptedException e) {
                // do nothing
            }
            ret = codec.read(template, 0L);
        }
        return ret;
    }


	
    public Object take(Object template, long timeout) {
        // first try a take if there is a repsonse it doesnt matter about the
        // timeout
        long endTime = System.currentTimeMillis() + timeout;
        
        Object ret = codec.take(template, 0L);
        if (ret != null || timeout == 0) {
            return ret;        
        }
        
        long nyquist = (timeout + 1L) >> 1;
        if (nyquist > sampleTime) {
            nyquist = sampleTime;
        }
       

        while (ret == null && endTime > System.currentTimeMillis()) {
            try {
                Thread.sleep(nyquist);
            } catch (InterruptedException e) {
                // do nothing 
            }
            ret = codec.take(template, 0L);
        }
        return ret;
    }
	

    public long write(Object entry,  long lease) {
		return codec.write( entry, lease);
	}
	
	
    public Object snapshot(Object template) {
		return codec.snapshot(template);
	}

       
    public long writeMany(Collection entries, long lease) {
        long lastLease = 0;
        for (Object entry : entries) {
            lastLease = codec.write( entry, lease );
        }
        return lastLease;
    }

    
    public Collection readMany(Object template, long matchLimit) {
        // zero means do ingnore and initial matches
        return codec.readMany(template, matchLimit, 0L);
    }

 
    public Collection takeMany(Object template, long matchLimit) {
        return codec.takeMany(template, matchLimit);
    }

      
    @Deprecated
    public boolean notify(Object template, NotifyHandler handler, long leaseTime) {
        return codec.notifyWrite(template, handler, leaseTime);
        //throw new UnsupportedOperationException("Deprecated - Please use notifyWrite method.");
    }
   
    
    public boolean notifyWrite(Object template, Notifiable handler, long leaseTime) {
        return codec.notifyWrite(template, handler, leaseTime);
        
    }

    public boolean notifyTake(Object template, Notifiable handler, long leaseTime) {
        return codec.notifyTake(template, handler, leaseTime);
    }
	
}   
