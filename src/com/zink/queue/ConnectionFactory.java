package com.zink.queue;

import com.zink.fly.kit.FlyFinder;

/**
* The start point for gaining a connection to a Fly based message queue.
* 
* Simply attempt to connect to a running Fly instance on the default localhost
* or on the given host name or ip address
*/

public class ConnectionFactory {
	
    public static Connection connect() {
         FlyFinder finder = new FlyFinder();
         return new Connection(finder.find());      
    }

    public static Connection connect(String host)  {
        FlyFinder finder = new FlyFinder();
        return new Connection(finder.find(host));              
    }
    
}
