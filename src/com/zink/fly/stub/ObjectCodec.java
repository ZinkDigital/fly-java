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
import com.zink.fly.FlyAccessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;


/**
 * @author nigel
 *
 */


public class ObjectCodec {
     
    FieldCodec fieldCodec = null;
    
    
    public ObjectCodec(FieldCodec fieldCodec) {
        this.fieldCodec = fieldCodec;
    }

    //
    // Write an object in the fly format to the output stream
    //
    public final void writeObject(DataOutputStream dos, Object object) {
        try {
            // if the object is not null, hence a valid java object then serialize it
            if (object != null) {
                // the object as an array of bytes in the form
                byte [] objBytes = fieldCodec.writeField(object);
                dos.writeLong((long)objBytes.length);
                dos.write(objBytes);
            } else  {
                dos.writeLong((long)0);
            }
        } catch (Exception ex) { 
            throw new FlyAccessException(ex);
        }
    }
    
    
    //
    // Read an object in the fly format from the input stream
    //
    public final Object readObject(DataInputStream dis) {
        Object object = null;
        try {
            long objectSize = dis.readLong();
            // read the size and if it is 0 then return a null object
            if (objectSize == (long)0) {
                return object;
            }
            
            // make the byte array for the object to be read into
            byte [] fieldBytes = new byte[(int)objectSize];
            dis.readFully(fieldBytes);
            object = fieldCodec.readField(fieldBytes);

            
        } catch (Exception ex) { 
            throw new FlyAccessException(ex);
        }
        return object;
    }
    
      
    
}
