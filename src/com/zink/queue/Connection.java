package com.zink.queue;

import com.zink.fly.Fly;

public class Connection {
	
	private Fly fly;
	
	Connection(Fly fly) {
		this.fly = fly;
	}
	
	public WriteChannel publish(String channelName) {
		return new WriteChannel(fly,channelName);
	}
	
	public ReadChannel subscribe(String channelName) {
		return new ReadChannel(fly,channelName);
	}

}
