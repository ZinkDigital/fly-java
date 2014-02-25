package com.zink.fly.example;


import com.zink.cache.*;


public class CacheShim {

	public static void main(String[] args) throws InterruptedException {
				
		Cache cache = CacheFactory.connect();
		
		cache.set("BBC1","http://www.bbc.co.uk/iplayer/tv/bbc_one");
		
		System.out.println(cache.get("BBC1"));
		System.out.println(cache.get("BBC2"));
		
		cache.setnx("BBC1","junk");
		System.out.println(cache.get("BBC1"));
		
		cache.del("BBC1");
		System.out.println(cache.get("BBC1"));
		
		cache.setnx("BBC1","http://www.bbc.co.uk/iplayer/tv/bbc_one");
		cache.expire("BBC1",100);
		System.out.println(cache.get("BBC1"));
		Thread.sleep(100);
		System.out.println(cache.get("BBC1"));
		
		System.out.println(Hasher.sha1("dennis"));
		System.out.println(Hasher.sha1("dennis"));
		System.out.println(Hasher.sha1("gnasher"));
		System.out.println(Hasher.md5("gnasher"));
		System.out.println(Hasher.md5("dennis"));
		
	}

}
