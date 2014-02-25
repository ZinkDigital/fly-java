package com.zink.cache;

import java.io.Serializable;

public class CacheEntry {

	public String k;
	public Serializable v;
	
	public CacheEntry(String key, Serializable value ) {
		this.k = key;
		this.v = value;
	}
	
	public CacheEntry(String key) {
		this(key, null);
	}

}
