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
 * FlyPrime is the simple (but powerful) interface to a Fly systems.
 * 
 * Use this interface to read, write and take java objects to and 
 * from the Fly server.
 * 
 * @author nigel
 *
 */
public interface FlyPrime {

    /**
     * The write method will write an entry into the fly space for the
     * amount of time given in the in the lease parameter. This is the time 
     * in milli-seconds that the object will 'live' in the space.
     * 
     * @param entry - The  object to put in the FlyPrime
     * @param leaseTime - The time in milliseconds the object will live in the Space
     * @return leaseTime - The space can return a shorter lease than the requested 
     * lease time. To be sure that your object has been leased for the given time
     * check that the returned lease is the same as the requested lease. 
     */
    long write(Object entry, long leaseTime);

    /**
     * The read method can be used to read but not remove an object from the 
     * space. The template will be matched on all of its non-null fields. If 
     * fields are set to null this means that the template will match any value 
     * in that field.
     * 
     * The waitTime is the time in milliseconds that this method will block, waiting
     * for an object to appear that matches the template.
     * 
     * @param template - The object template to match in the space.
     * @param waitTime - Time in milliseconds to wait before the object is matched.
     * @return The object tht has been matched or null if the template has not been 
     * matched in the given wait time.
     */
     <T> T read(T template, long waitTime);

    /**
     * The Take method uses the same matching strategy as the read method via the
     * template, however if an object is matched under this (take) method the object
     * is removed from the space and returned.
     * 
     * @param template - The object template to match in the space.
     * @param waitTime - The time to wait in milliseconds for the template to be matched.
     * @return The object tht has been matched or null if no object has been matched
     * in the given wait time.
     */
    <T> T take(T template, long waitTime);

    /** 
     * Snapshot will make a copy of an object which may help the stub prepare
     * the object for submission to the space. This is an non essential method 
     * but may be used for performance reasons, if for example a templae is not going 
     * to channge and is going to be submitted many times to the space in a read or
     * take method.  
     * 
     * 
     * @param template object 
     * @return snapshot object
     */
    Object snapshot(Object template);
}

