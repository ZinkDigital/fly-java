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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * When the cache is constructed it automatically starts listening for multicast
 * packets via the Executor.
 * 
 * It is possible to safely start and stop the Cache listening for requests
 * by using the 'start' and 'terminate' methods. See FlyFinder as an example.
 * 
 * 
 * @author nigel
 */
public class FlyCache implements FlyRepHandler  {
   
    
    // event handling
    private List<FlyDiscoveredHandler> discoveredHandlers = new ArrayList<FlyDiscoveredHandler>(2);
    private List<FlyLostHandler> lostHandlers = new ArrayList<FlyLostHandler>(2);       
    
    
    // the multicast helpers
    MulticastRequestor mcr = new MulticastRequestor();
    ExecutorService exec = null;
    MulticastResponseListener listener = null;
     
    // the cache of server reps 
    private HashMap<InetAddress,FlyServerRep> reps = new LinkedHashMap<InetAddress, FlyServerRep>();
        
    /**
     * When we create a cache we also start it by convention
     */
    public FlyCache()  {
        start();
    }
       
            
    public Collection<FlyServerRep> getAllReps() {
        return new ArrayList<FlyServerRep>(reps.values());
    }
    
    
    public Collection<FlyServerRep> getMatchingReps(String [] tags) {
        Collection matched = new ArrayList<FlyServerRep>();
        for (FlyServerRep rep : reps.values()) {
            if (rep.tagsMatch(tags)) {
                    matched.add(rep);
            }
        }
        return matched;
    }
    
    
    
    /**
     * Register a handler that wants to know if a fly instance has been started 
     * @param handler
     */
    public void registerDiscoveredHandler(FlyDiscoveredHandler handler) {
       discoveredHandlers.add(handler); 
    }
    
    /**
     * No longer inerested in discoveries
     * @param handler
     */
    public void removeDiscoveredHandler(FlyDiscoveredHandler handler) {
        discoveredHandlers.remove(handler);
    }
    
    /**
     * Register a handler that wants to know if a fly instance has been stopped
     * @param handler
     */
    public void registerLostHandler(FlyLostHandler handler) {
       lostHandlers.add(handler); 
    }
    
    /**
     * No longer inerested in removed events
     * @param handler
     */
    public void removeLostHandler(FlyLostHandler handler) {
        lostHandlers.remove(handler);
    }

    
    // gets called back when a server replies
    public void flyRepReply(FlyServerRep rep) {
        reps.put(rep.getFlyAddr(), rep);
    }
    
    
    
    public void issueRequest() {
        mcr.sendRequest();
    }
     
    
    public void start() {
        // start the listener that will call us back
        if (exec == null) {
            exec = Executors.newSingleThreadExecutor();
            listener = new MulticastResponseListener(this);
            exec.submit(listener);
        }
        // and multicast ping to find all the local running spaces
        issueRequest();  
    }
    
    
    public void terminate() {
        listener.close();
        exec.shutdownNow();
        exec = null;
    }
    
    
    
}
