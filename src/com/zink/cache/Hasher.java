package com.zink.cache;


import java.math.BigInteger;
import java.security.MessageDigest;

//  
public class Hasher {
	
	public static BigInteger sha1(String str)  {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new BigInteger(1,md.digest(str.getBytes("UTF-8")));
		}
		catch(Exception exp) {
			throw new RuntimeException("Could not hash string");
		}
    }
	
	public static BigInteger md5(String str)  {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			return new BigInteger(1,md.digest(str.getBytes("UTF-8")));
		}
		catch(Exception exp) {
			throw new RuntimeException("Could not hash string");
		}
    }
}
