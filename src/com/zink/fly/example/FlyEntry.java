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


package com.zink.fly.example;


/**
 * @author nigel
 */
public class FlyEntry {
    
    // use this object in a Tuple/JavaSpace style
    public String name;
    public Integer reference;
    public String payload;
    
    
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("Name :");
        sb.append(name);
        sb.append("\n");
        sb.append("Refercence :");
        sb.append(reference.toString());
        sb.append("\n");
        sb.append("Payload size :");
        sb.append(payload.length());
        sb.append("\n"); 
        return sb.toString();
    }
    
    
    public void setPayloadOfSize(int payloadSize) {
        // set up the payload
        StringBuilder sb = new StringBuilder(payloadSize);
        for (int i = 0; i < payloadSize; i ++)
        sb.append('z');
        payload = sb.toString();
    }


}
