package com.zink.queue;

import java.io.Serializable;

public class Message {

	public String channelName;
	public Serializable payload;
	
	public Message(String channelName, Serializable payload ) {
		this.channelName = channelName;
		this.payload = payload;
	}
	
	public Message(String channelName) {
		this(channelName, null);
	}
	

}
