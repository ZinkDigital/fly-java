package com.zink.fly.example;


import com.zink.queue.*;

public class MessageShim {

	public static void main(String[] args) {
				
		Connection con = ConnectionFactory.connect();
		
		WriteChannel wc = con.publish("BBC7");
		wc.write("Hello Subscriber");
		
		ReadChannel rc = con.subscribe("BBC7");
		System.out.println(rc.read());

	}

}
