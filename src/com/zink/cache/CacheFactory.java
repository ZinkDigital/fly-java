package com.zink.cache;

import com.zink.fly.kit.FlyFinder;

/**
* The start point for gaining a connection to a Fly based message queue.
* 
* Simply attempt to connect to a running Fly instance on the default localhost
* or on the given host name or ip address
*/

public class CacheFactory {
	
    public static Cache connect() {
         FlyFinder finder = new FlyFinder();
         return new Cache(finder.find());      
    }

    public static Cache connect(String host)  {
        FlyFinder finder = new FlyFinder();
        return new Cache(finder.find(host));              
    }
    
}
