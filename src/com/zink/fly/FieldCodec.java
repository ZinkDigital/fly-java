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

/**
 *
 * @author nigel
 */
public interface FieldCodec {


     public static final int DEFAULT_BUFFER_SIZE = 1048;
    
    /**
     * Encode the given object into any byte array required
     * @param field
     * @return the encoded field as a byte array
     */
    public byte [] writeField(Object field);
  
    /**
     * Decode the byte array to create the object field
     * @param fieldBytes
     * @return the object created from the byte array
     */
    public Object readField(byte [] fieldBytes);
    

}
