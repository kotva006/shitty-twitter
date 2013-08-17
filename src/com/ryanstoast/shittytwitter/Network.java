package com.ryanstoast.shittytwitter;

import java.util.HashMap;
import java.util.TreeMap;

import net.oauth.OAuth;

import android.util.Log;

public class Network {

	static String url = TwitterAPI.BASE_URL;
	
	public static String[] getToken() {
		
		String tokenUrl = url + "/oauth/request_token";
				
		HashMap<String,String> map = new HashMap<String, String>();
		
		map.put("oauth_callback"          , TwitterAPI.CALLBACK);
		map.put("oauth_consumer_key"      , TwitterAPI.CONSUMER_KEY);
		map.put("oauth_nonce"             , TwitterAPI.getNONCE());
		map.put("oauth_signature_method"  , TwitterAPI.SIGNATURE_METHOD);
		map.put("oauth_timestamp"         , TwitterAPI.getTime());
		map.put("oauth_version"           , TwitterAPI.OAUTH_VER);
		
		String signature = TwitterAPI.getSignature(tokenUrl, map, TwitterAPI.getKey());
		
		map.put("oauth_signature"         , signature);
		
		String headerString = "OAuth ";
		
		NetworkClient client = new NetworkClient(tokenUrl);
		client.AddHeader("POST", "/oauth/request_token HTTP/1.1");
		client.AddHeader("User-Agent", "HTTP Client");
		client.AddHeader("Host", "api.twitter.com");
		client.AddHeader("Accept", "*/*");
		client.AddHeader("Authorization", headerString + mapToHeaderString(map));
			
		client.SetTimeout(10000, 10000);
		
		try {
			client.Execute(NetworkClient.RequestMethod.POST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (client.getResponseCode() != 200) {
	        
			Log.d("Response Code", Integer.toString(client.getResponseCode()));
			Log.d("Error Mesg:", client.getErrorMessage());
			Log.d("Network.getToken: ", (client.getResponse() == null) ? "" : client.getResponse());
		    
		
		} else if(client.getResponseCode() == 200) {
			Log.d("Network.getToken: ", (client.getResponse() == null) ? "" : client.getResponse());
			String[] t = client.getResponse().split("&");
			String[]  tokens = {t[0].split("=")[1], t[1].split("=")[1], t[2].split("=")[1]};
			return tokens;
		}
		return new String[]{""};
		
	}
	
	private static String mapToHeaderString(HashMap<String,String> map) {
		
		String result = "";
        TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		
		for(String key : sortedMap.keySet()) {
			result += OAuth.percentEncode(key) + "=\"" + OAuth.percentEncode(sortedMap.get(key)) + "\", ";
		}
		return result.substring(0, result.length() - 2);
	}
	
	
}
