package com.zink.cache;

import com.zink.fly.kit.FlyFactory;


/**
* The start point for gaining a connection to a Fly based message queue.
* 
* Simply attempt to connect to a running Fly instance on the default localhost
* or on the given host name or ip address
*/

public class CacheFactory {
	
    public static Cache connect() {
         return new Cache(FlyFactory.makeFly());      
    }

    public static Cache connect(String host)  {
        return new Cache(FlyFactory.makeFly(host));              
    }
    
}
