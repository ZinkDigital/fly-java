package com.zink.cache;

import java.io.Serializable;

import com.zink.fly.Fly;

public class Cache {
	
	private static final long DEFAULT_LEASE = 24*60*60*1000; 
	
	private Fly fly;
	
	Cache(Fly fly) {
		this.fly = fly;
	}
	
	public final boolean set(String key, Serializable value) {
		setWithTimeout(key,value,DEFAULT_LEASE);
		return true;
	}
	
	
	public final Serializable get(String key) {
		CacheEntry tmpl = new CacheEntry(key);
		CacheEntry res = fly.read(tmpl, 0);
		if ( res != null) return res.v;
		return null;	
	}
	
	public final boolean del(String key) {
		CacheEntry tmpl = new CacheEntry(key);
		if ( fly.take(tmpl, 0) != null ) return true;
		return false;
	}
	
	public boolean setnx(String key, Serializable value) {
		if ( get(key) == null) {
			set(key,value);
			return true;
		}
		return false;
	}

	public boolean expire(String key, long timeOut) {
		Serializable value = get(key);
		if ( value != null) {
			setWithTimeout(key, value, timeOut);
			return true;
		}
		return false;
	}
	
	
	private final void setWithTimeout(String key, Serializable value, long timeout) {
		// evict a match to guarantee one only
		del(key);
		CacheEntry entry = new CacheEntry(key,value);
		fly.write(entry, timeout);	
	}
}
