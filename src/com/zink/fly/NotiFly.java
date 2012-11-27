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
 * NotiFly is the interface to the Fly Notify methods - please excuse the pun.
 * 
 * @author Nigel
 *
 */
public interface NotiFly extends FlyPrime {
 
    
    
    /**
     * @deprecated 
     * This method it will be removed at version 2.0
     */
    boolean notify(Object template, NotifyHandler handler, long leaseTime);	
		
    
    
    
    /**
     * The notifyWrite method sets up a template object in the Fly Space.
     * If an object is written to the Space in the lifetime of the 
     * notify template, that matches the template, then the method
     * templateMatched() will be called in the object that implements 
     * the interface NotifyHandler.
     * 
     * Take care when setting up notify templates; a large number of 
     * pending notifies can impead 'write' performance in the space.
     * Set the lease of the notify template to a reasonable time and renew
     * the template lease periodicaly.
     * 
     * Use the NotifyLeaseRenewer helper in the examples to periodicly renew leases.
     * 
     * @param template - the template to match to triger this notify
     * @param handler - An object that implements the NotifyHandler interface 
     * @param leaseTime - The time in milliseconds the template will live in the Space
     * @return boolen - Setup ok
     *  
     */
    boolean notifyWrite(Object template, Notifiable handler, long leaseTime);
    
    
    
    /**
     * The notifyTake method sets up a template object in the Fly Space.
     * If an object is taken from the Space in the lifetime of the 
     * notify template that matches the template, then the method 
     * templateMatched() will be called in the object that implements 
     * the interface NotifyHandler.
     * 
     * Take care when setting up notify templates; a large number of 
     * peneding notifies can impead 'take' performance in the space.
     * Set the lease of the notify template to a reasonable time and renew
     * the template lease periodicaly.
     * 
     * Use the NotifyLeaseRenewer helper in the examples to periodicly renew leases.
     * 
     * @param template - the template to match to triger this notify
     * @param handler - An object that implements the NotifyHandler interface 
     * @param leaseTime - The time in milliseconds the template will live in the Space
     * @return boolen - Setup ok
     *  
     */
    boolean notifyTake(Object template, Notifiable handler, long leaseTime);
    
    
}

