package com.ryanstoast.shittytwitter;

import java.util.HashMap;
import java.util.TreeMap;

import net.oauth.OAuth;

import android.util.Log;

public class Network {

	static String url = "https://api.twitter.com";
	
	public static String getToken() {
		String tokenUrl = url + "/oauth/request_token";
		Log.d("Here", "I issg");
		
		HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_callback"          , "oop");
		map.put("oauth_consumer_key"      , Settings.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , "HMAC-SHA1");
		map.put("oauth_timestamp"         , Integer.toString((int) 
				                            (System.currentTimeMillis() / 1000L)));
		map.put("oauth_version"           , "1.0");
		
		String baseSig   = TwitterAPI.getSignatureBase(tokenUrl, map);
		String signature = TwitterAPI.getSignature(baseSig, TwitterAPI.getKey());
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("Authorization", headerString + mapToString(map));
		
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d("Network.getToken: ", (client.getResponse() == null) ? "" : client.getResponse());
		
		return "";
	}
	
	
	private static String mapToString(HashMap<String,String> map) {
		
		String result = "";
        TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		
		for(String key : sortedMap.keySet()) {
			result += OAuth.percentEncode(key) + "=" + OAuth.percentEncode(sortedMap.get(key));
		}
		return result;
	}
	
	
}
