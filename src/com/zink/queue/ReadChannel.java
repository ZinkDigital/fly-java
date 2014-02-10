package com.zink.queue;

import java.io.Serializable;

import com.zink.fly.Fly;

public class ReadChannel {

   private static final long READ_LEASE = 60*1000;
   
   private Fly fly;
   private Message tmpl;
	
   ReadChannel(Fly fly, String channelName) {
		super();
		this.fly = fly;
		this.tmpl = new Message(channelName);
	}
	
	public Serializable read() {
		return read(READ_LEASE) ;
	}
	
	public Serializable read(long timeOut) {
		Serializable payload = null;
		Message msg = fly.take(tmpl, timeOut);
		if ( msg != null) payload = msg.payload;
		return payload;
	}

}
