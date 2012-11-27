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

import com.zink.fly.FlyAccessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author nigel
 */
public class StringCodec {
    
     public final void writeString(DataOutputStream dos, String string) {
        byte [] strBytes = string.getBytes();
        try {
            // write the str length (in bytes)
            dos.writeInt(strBytes.length);
            dos.write(strBytes);
        } catch (IOException ex) { 
            throw new FlyAccessException(ex);
        }
    }
    
    
    public final String readString(DataInputStream dis) {
        byte [] bytes = null;
        try {
           //System.out.println("Available " + dis.available());
            int length = dis.readInt();
            bytes = new byte[length];
            dis.read(bytes);
        } catch (IOException ex) { 
            throw new FlyAccessException(ex);
        }
        return new String(bytes);
    }
   
    

}
