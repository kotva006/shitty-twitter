package com.ryanstoast.shittytwitter;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.oauth.OAuth;
import android.util.Base64;
import android.util.Log;

public class TwitterAPI {
	
	final static String BASE_URL      = "https://api.twitter.com";
	final static String REQUEST_TOKEN = "/oauth/request_token";
	final static String AUTHENTICATE  = "/oauth/authenticate";
	
	final static String CALLBACK      = "x-shitty-twitter-oauth-twitter://callback";
	final static String CONSUMER_KEY  = Settings.CONSUMER_KEY;
	final static String OAUTH_VER     = "1.0";
	final static String SIGNATURE_METHOD    = "HMAC-SHA1";
	
	public static String getTime() {
		
		return Integer.toString((int) (System.currentTimeMillis() / 1000l));
		
	}
		
	private static String getSignatureBase(String url, HashMap<String, String> map) {
		
		String result = "POST&" + OAuth.percentEncode(url) + "&";
		String holder = "";
		
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		
		for(String key : sortedMap.keySet()) {
			
			holder += OAuth.percentEncode(key);
			holder += "=" + OAuth.percentEncode(sortedMap.get(key));
			holder += "&";
		}
		
		holder = holder.substring(0, holder.length() - 1);
		result += OAuth.percentEncode(holder);
		
		Log.d("getSignature Result", result);
		return result;
		
	}
	
	public static String getNONCE() {
		
        String rand = Base64.encodeToString(UUID.randomUUID().toString().getBytes(), Base64.DEFAULT);
		rand = rand.substring(0, 15);
		Log.d("String, ", rand);
		return rand.substring(0, rand.length() - 1);
		
	}
	
	public static String getKey() {
		
		return  Settings.CONSUMER_SECRET + "&" + MainActivity.getTwitterToken();
		
	}
	
	public static String getSignature(String url, HashMap<String, String> map, String key) {
		
		String data   = getSignatureBase(url, map);
		
		String result = "";
		
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(secretKey);
			
			byte[] rawHmac = mac.doFinal(data.getBytes());
			
			result = Base64.encodeToString(rawHmac, Base64.DEFAULT);
			
			
		} catch (Exception e){
			
			Log.d("HMAC Error", e.getMessage());
		}
		result = result.substring(0, result.length() - 1);
		Log.d("HMAC Hash: ", result);
		
		return result;
		
	}
}
