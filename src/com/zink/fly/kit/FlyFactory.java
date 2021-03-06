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

import com.zink.fly.FieldCodec;
import com.zink.fly.Fly;
import com.zink.fly.FlyAccessException;
import com.zink.fly.stub.FlyStub;
import com.zink.fly.stub.SerializingFieldCodec;


/**
 * @author nigel
 */
public class FlyFactory  {
    

    private static final String DEFAULT_HOST = "localhost";
    
    /*
     * Try to create a Fly stub connected to a Fly server running on 
     * this machine
     */
    public static Fly makeFly() {
        return makeFly(DEFAULT_HOST);
    }

    
    public static Fly makeFly(FieldCodec fieldCodec) {
        return makeFly(DEFAULT_HOST, fieldCodec);
    }
   
    
    public static Fly makeFly(String host)  {
        return makeFly(host, new SerializingFieldCodec());
    }
    
    
    public static Fly makeFly(String host, FieldCodec fieldCodec)  {
        Fly stub = null;
        try {
            stub =  new FlyStub(host, fieldCodec);
        } catch (Exception ex) {
            throw new FlyAccessException("Cant locate Fly Server running on ["+host+"]",ex);
        }
        return stub;   
    }

    
}
