package com.zink.fly.example;

import com.zink.queue.Connection;
import com.zink.queue.ConnectionFactory;
import com.zink.queue.ReadChannel;
import com.zink.queue.WriteChannel;

public class MessageShim {

	public static void main(String[] args) {
	
		Connection con = ConnectionFactory.connect();
		
		WriteChannel wc = con.publish("BBC7");
		wc.write("Hello Subscriber");
		
		ReadChannel rc = con.subscribe("BBC8");
		System.out.println(rc.read());

	}

}
