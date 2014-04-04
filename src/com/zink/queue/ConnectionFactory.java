package com.zink.queue;

import com.zink.fly.kit.FlyFactory;


/**
* The start point for gaining a connection to a Fly based message queue.
* 
* Simply attempt to connect to a running Fly instance on the default localhost
* or on the given host name or ip address
*/

public class ConnectionFactory {
	
    public static Connection connect() {
         return new Connection( FlyFactory.makeFly() );      
    }

    public static Connection connect(String host) {
        return new Connection( FlyFactory.makeFly(host) );              
    }
    
}
