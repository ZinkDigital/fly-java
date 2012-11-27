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
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import sun.reflect.ReflectionFactory;

/**
 *
 * @author nigel
 */
public class EmptyObjectFactory {

    // make an object of the correct type without calling the
    // object's constructor. Because the object may not have a no-args
    // ctor (but may have others we simply construct in the
    // style of a serialisable class.   
    private  ReflectionFactory factory = ReflectionFactory.getReflectionFactory();
    private  Constructor objectCtor;

 
    private Map<Class, Constructor> ctors = new HashMap<Class, Constructor>();


    public EmptyObjectFactory()  {
        try {
            objectCtor = Object.class.getDeclaredConstructor();
        } catch (Throwable ex) {
           throw new FlyAccessException(ex);
        }
    }
 
    Object makeEmptyObject(Class type) throws Exception {
            Constructor typedCtr = ctors.get(type);
            if (typedCtr == null) {
                typedCtr = factory.newConstructorForSerialization(type, objectCtor);
                ctors.put(type,typedCtr);
            }
            return typedCtr.newInstance();
    }

}
