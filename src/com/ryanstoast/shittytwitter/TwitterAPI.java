package com.ryanstoast.shittytwitter;

import java.util.HashMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.oauth.OAuth;
import android.util.Base64;
import android.util.Log;

public class TwitterAPI {

public static String signIn(String username, String password) {
		
		
		return "";
	}
	
	public static String getSignature(String url, HashMap<String, String> map) {
		
		// TODO Make percent encoder
		
		String result = "POST";
		String holder = "";
		
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		
		for(String key : sortedMap.keySet()) {
			holder += "&";
			holder += OAuth.percentEncode(key);
			holder += "=" + OAuth.percentEncode(sortedMap.get(key));
		}
		
		result += "&" + OAuth.percentEncode(url);
		result += OAuth.percentEncode(holder);
		
		Log.d("getSignature Result", result);
		return result;
	}
	
	public static String getNONCE() {
		
        String nonce = "";
        		
		//Generate and encode a pseudo random string
		nonce = OAuth.percentEncode(Settings.random());
		return nonce;
	}
	
	public static String getKey() {
		
		return"";
	}
	
	public static String getHMAC(String data, String key) {
		String result = "";
		
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(secretKey);
			
			byte[] rawHmac = mac.doFinal(data.getBytes());
			
			result = Base64.encodeToString(rawHmac, Base64.URL_SAFE);
			result = OAuth.percentEncode(result);
			
		} catch (Exception e){
			
			Log.d("HMAC Error", e.getMessage());
		}
		Log.d("HMAC Hash: ", result);
		return result;
	}
	 
}
