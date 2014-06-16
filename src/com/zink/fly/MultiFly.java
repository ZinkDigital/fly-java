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


import java.util.Collection;



/**
 * MultiFly is the interface to a Fly Space that can write, read and take 
 * multiple entries with single method calls. These methods can be used to
 * batch up objects to and from the space which may reduce total round trip 
 * times and network usage.
 * 
 * @author Nigel
 *
 */
public interface MultiFly extends FlyPrime {

    /**
     * The writeMany method will write a collection of entry objects into the space
     * for amount of time given in the in the lease parameter. This is the time 
     * in milli-seconds that the objects will 'live' in the Space.
     * 
     * @param entries - The object to put in the Space
     * @param lease - The time in milliseconds the objects will live in the Space
     * @return leaseTime - The space can return a shorter lease than the requested 
     * lease time. To be sure that your object has been leased for the given time
     * check that the returned lease is the same as the requested lease. 
     */
    long writeMany(Collection entries, long lease);

    
    /**
     * The readMany method can be used to read but not remove a number of objects
     * from the space. The template will be matched on all of its non-null fields. If 
     * fields are set to null this means that the template will match any value 
     * in that field.
     * 
     * The matchLimit is a limit to number of objects that the method will return in 
     * the Collection. For example if the the space contains 1000 objects that will 
     * match the template and the match limit is set to 200, then only the first 
     * 200 matching objects will be returned in the collection.
     * 
     * If on the other hand if the match limit is 200 and the space contains only
     * 135 matching entries then all 135 entries will be returned.
     * 
     * @param template - Template to match 
     * @param matchLimit - The upper limit of matched objects to return
     * @return A collection of objects that have been matched or empty if no matches
     */
     <T> Collection<T> readMany(T template, long matchLimit);
    
    
    /**
     * The takeMany method can be used to take (remove) a number of objects
     * from the space. The template will be matched on all of its non-null fields. If 
     * fields are set to null this means that the template will match any value 
     * in that field.
     * 
     * The matchLimit is a limit to number of objects that the method will return in 
     * the Collection. For example if the the space contains 1000 objects that will 
     * match the template and the match limit is set to 200, then only the first 
     * 200 matching objects will be returned in the collection.
     * 
     * If on the other hand if the match limit is 200 and the space contains only
     * 135 matching entries then all 135 entries will be returned.
     * 
     * @param template - Template to match 
     * @param matchLimit - The upper limit of matched objects to return
     * @return A collection of objects that have been matched or empty if no matches
     */ 
    <T> Collection<T> takeMany(T template, long matchLimit);
}

