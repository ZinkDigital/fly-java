package com.zink.queue;

import java.io.Serializable;

import com.zink.fly.Fly;

public class WriteChannel {

   private static final long DAY_LEASE = 24*60*60*1000;
   
   private Fly fly;
   private String channelName;
	
   WriteChannel(Fly fly, String channelName) {
		super();
		this.fly = fly;
		this.channelName = channelName;
	}

	
	public void write(Serializable message) {
		fly.write(new Message(channelName,message),DAY_LEASE);
	}

}
