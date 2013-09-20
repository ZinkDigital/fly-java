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
 * Test weird field types.
 * @author nigel
 */
public class ExoticEntry {
    
    public String name1 = "public";
    
    private String name2 = "private";
    
    public static String name3 = "publicStatic";
    
    private static String name4 = "privateStatic";
    
    public final String name5 = "publicFinal";
    
    private final String name6 = "privateFinal";

    public ExoticEntry() {
        super();
    }
  
    
    public void setName2(String name2) {
        this.name2 = name2;
    }
    public String getName2() {
        return name2;
    }
    
    public String getName6() {
        return name6;
    }
    
}   
